package org.kaws.schedule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kaws.schedule.domain.pojo.ScheduleJob;
import org.kaws.schedule.mapper.ScheduleJobMapper;
import org.kaws.schedule.service.ScheduleJobService;
import org.springframework.stereotype.Service;

/**
 * @author Bosco
 * @date 2022/4/13 7:34 下午
 */

@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {
}
