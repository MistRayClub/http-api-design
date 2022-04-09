package org.kaws.controller;

import cn.hutool.core.util.ObjectUtil;
import org.kaws.biz.AuthenticationBiz;
import org.kaws.common.annotation.Logging;
import org.kaws.common.reponse.R;
import org.kaws.dto.AppKeyInfoDTO;
import org.kaws.dto.AuthenticationDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Bosco
 * @date 2022/4/1 5:48 下午
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Resource
    private AuthenticationBiz authBiz;

    @PostMapping("/register")
    public R<AppKeyInfoDTO> register(@RequestBody AppKeyInfoDTO appKeyInfoDTO) {
        AppKeyInfoDTO register = authBiz.register(appKeyInfoDTO);
        if (ObjectUtil.isNotEmpty(register)) {
            return R.success(appKeyInfoDTO);
        } else {
            return R.failure("AppKey register failure，can not get appSecret");
        }
    }

    @GetMapping("/nonce")
    @Logging(title = "获取随机数", describe = "获取随机数")
    public R<Integer> nonce(String appKey) {
        Integer nonce = authBiz.nonce(appKey);
        return R.success(nonce);
    }

    @PostMapping("/verify")
    @Logging(title = "验签", describe = "验签通过，并发放token")
    public R<String> verify(@RequestBody AuthenticationDTO authenticationDTO, HttpServletRequest request) {
        String uri = request.getRequestURI();
        authenticationDTO.setUrl(uri);
        String signature = request.getHeader("signature");
        Boolean flag = authBiz.verify(authenticationDTO, signature);
        if (flag) {
            String jwt = authBiz.generateJWT(authenticationDTO);
            return R.success("generate jwt succeed", jwt);
        }
        return R.failure("verify signature failed, please check your configuration");
    }


}
