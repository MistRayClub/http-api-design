package org.kaws.biz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.kaws.pojo.dto.AppKeyInfoDTO;
import org.kaws.pojo.dto.AuthenticationDTO;
import org.kaws.pojo.model.AppKeyInfo;
import org.kaws.service.AppKeyInfoService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Bosco
 * @date 2022/4/2 11:32 上午
 */

@Component
public class AuthenticationBiz {

    private static final Integer SECRET_LENGTH = 16;

    private static final String APP_NONCE_KEY = "appKey:nonce:%s";

    private static final String JWT_SECRET = "LebronJames";

    private static final Long EXPIRE = 30 * 60 * 1000L;

    @Resource
    private AppKeyInfoService appKeyInfoService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public AppKeyInfoDTO register(AppKeyInfoDTO appKeyInfoDTO) {
        appKeyInfoDTO.setAppSecret(RandomUtil.randomString(SECRET_LENGTH));
        AppKeyInfo entity = new AppKeyInfo();
        BeanUtil.copyProperties(appKeyInfoDTO, entity);
        boolean flag = appKeyInfoService.save(entity);
        if (flag) return appKeyInfoDTO;
        return null;
    }

    public Integer nonce(String appKey) {
        String key = String.format(APP_NONCE_KEY, appKey);
        String existedNonce = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotEmpty(existedNonce)) {
            return Integer.valueOf(existedNonce);
        } else {
            Integer nonce = RandomUtil.randomInt(Integer.MAX_VALUE);
            stringRedisTemplate.opsForValue().set(key, String.valueOf(nonce), EXPIRE, TimeUnit.MILLISECONDS);
            return nonce;
        }
    }

    public Boolean verify(AuthenticationDTO auth, String signature) {
        String key = String.format(APP_NONCE_KEY, auth.getAppKey());
        String existedNonce = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(existedNonce) || !Objects.equals(Integer.valueOf(existedNonce), auth.getNonce())) {
            return false;
        }
        AppKeyInfo entity = appKeyInfoService.getOne(Wrappers.<AppKeyInfo>lambdaQuery().eq(AppKeyInfo::getAppKey, auth.getAppKey()));
        if (ObjectUtil.isEmpty(entity)) {
            return false;
        }
        String createdSign = createSign(auth, entity.getAppSecret());
        if (signature.equals(createdSign)) {
            long now = System.currentTimeMillis();
            auth.setTimestamp(now + 5 * 60 * 1000);
            return true;
        }
        return false;
    }

    public String generateJWT(AuthenticationDTO auth) {
        Map<String, Object> payload = BeanUtil.beanToMap(auth, false, true);
        return JWTUtil.createToken(payload, JWT_SECRET.getBytes());
    }

    private String createSign(AuthenticationDTO auth, String appSecret) {
        String appSecretStr = appSecret + auth.getNonce();
        Map<String, Object> params = BeanUtil.beanToMap(auth, false, true);
        return params.keySet().stream().sorted()
                .map(key -> key + "=" + params.get(key) + "&")
                .reduce((x, y) -> x + y)
                .map(content -> content.substring(0, content.length() - 1))
                .map(content -> content.concat(appSecretStr))
                .map(SecureUtil::sha256)
                .map(String::toUpperCase)
                .orElse(null);
    }

}
