package org.kaws.common.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bosco
 * @date 2022/4/9 3:18 下午
 */

@Data
@TableName("sys_log")
public class SysLog {

    /**
     * 主键
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求链接
     */
    private String operateUrl;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 响应数据
     */
    private String responseBody;

    /**
     * 接口执行状态
     */
    private boolean success;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;

    /**
     * 扩展信息
     */
    @TableField(exist = false)
    private Map<String, String> map = new HashMap<>();

}
