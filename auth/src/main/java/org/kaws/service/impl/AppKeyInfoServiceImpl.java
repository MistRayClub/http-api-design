package org.kaws.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kaws.mapper.AppKeyInfoMapper;
import org.kaws.model.AppKeyInfo;
import org.kaws.service.AppKeyInfoService;
import org.springframework.stereotype.Service;

/**
 * @author Bosco
 * @date 2022/4/1 2:40 下午
 */

@Service
public class AppKeyInfoServiceImpl extends ServiceImpl<AppKeyInfoMapper, AppKeyInfo> implements AppKeyInfoService {
}
