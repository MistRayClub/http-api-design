package org.kaws.schedule.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Bosco
 * @date 2022/4/13 7:15 下午
 */

@Getter
@Setter
public class ScheduleJobDTO {

    /**
     * 任务编号
     */
    private String jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 运行类
     */
    private String beanName;

    /**
     * 携带参数
     */
    private String params;

    /**
     * cron 表达式
     */
    private String cronExpression;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 备 注
     */
    private String remark;

}
