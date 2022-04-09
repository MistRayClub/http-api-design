package org.kaws.common.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bosco
 * @date 2022/4/9 3:41 下午
 */
@Data
public class SysLogDTO {
    /**
     * 编号
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
     * 请求的方法
     */
    private String method;

    /**
     * 请求的连接
     */
    private String operateUrl;

    /**
     * 请 求 参 数
     */
    private String requestParam;

    /**
     * 获 取 请 求 体
     */
    private String requestBody;

    /**
     * 接 口 响 应 数 据
     */
    private String responseBody;

    /**
     * 接 口 执 行 状 态
     */
    private boolean success;

    /**
     * 异 常 信 息
     */
    private String errorMsg;

    /**
     * 操 作 时 间
     */
    private LocalDateTime createTime;

    /**
     * 扩 展 信 息
     */
    private Map<String, String> map = new HashMap<>();


}
