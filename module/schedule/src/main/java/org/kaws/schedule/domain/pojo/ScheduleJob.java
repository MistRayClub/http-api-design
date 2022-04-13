package org.kaws.schedule.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bosco
 * @date 2022/4/13 7:15 下午
 */

@Getter
@Setter
@TableName("schedule_job")
public class ScheduleJob implements Serializable {

    private static final long serialVersionUID = 2375120129429390572L;

    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /**
     * 任务编号
     */

    @TableId
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
