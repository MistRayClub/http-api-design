package org.kaws.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kaws.common.domain.model.SysLog;
import org.kaws.system.mapper.SysLogMapper;
import org.kaws.system.service.SysLogService;
import org.springframework.stereotype.Service;

/**
 * @author Bosco
 * @date 2022/4/9 4:11 下午
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
}
