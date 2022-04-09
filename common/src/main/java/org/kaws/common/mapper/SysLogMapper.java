package org.kaws.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.kaws.common.domain.model.SysLog;

/**
 * @author Bosco
 * @date 2022/4/9 3:38 下午
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
}
