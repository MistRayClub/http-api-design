package org.kaws.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.kaws.schedule.domain.pojo.ScheduleJob;

/**
 * @author Bosco
 * @date 2022/4/13 7:24 下午
 */
@Mapper
public interface ScheduleJobMapper extends BaseMapper<ScheduleJob> {
}
