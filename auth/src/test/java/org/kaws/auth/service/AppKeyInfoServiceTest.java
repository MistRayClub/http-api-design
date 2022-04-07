package org.kaws.auth.service;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kaws.model.AppKeyInfo;
import org.kaws.service.AppKeyInfoService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Bosco
 * @date 2022/4/1 4:59 下午
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppKeyInfoServiceTest {


    @Resource
    private AppKeyInfoService appKeyInfoService;

    @Test
    public void save() {
        AppKeyInfo appKeyInfo = new AppKeyInfo();
        appKeyInfo.setAppKey("LebronJames");
        appKeyInfo.setAppSecret("Lakers");
        appKeyInfoService.save(appKeyInfo);
    }

    @Test
    public void update() {
        LambdaUpdateWrapper<AppKeyInfo> updateWrapper = Wrappers.<AppKeyInfo>lambdaUpdate().set(AppKeyInfo::getAppSecret, RandomUtil.randomString(16))
                .ne(AppKeyInfo::getAppKey, "lebron");
        appKeyInfoService.update(updateWrapper);
    }

    @Test
    public void query() {
        LambdaQueryWrapper<AppKeyInfo> queryWrapper = Wrappers.<AppKeyInfo>lambdaQuery().eq(AppKeyInfo::getAppKey, "LebronJames");
        AppKeyInfo appKeyInfo = appKeyInfoService.getOne(queryWrapper);
        System.out.println(appKeyInfo);


        String pwd = new BCryptPasswordEncoder().encode("123456");
        System.out.println(pwd);

    }


}