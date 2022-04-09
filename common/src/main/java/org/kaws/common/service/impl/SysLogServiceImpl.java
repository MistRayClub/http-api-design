package org.kaws.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kaws.common.domain.model.SysLog;
import org.kaws.common.mapper.SysLogMapper;
import org.kaws.common.service.SysLogService;
import org.springframework.stereotype.Service;

/**
 * @author Bosco
 * @date 2022/4/9 4:11 下午
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
}
