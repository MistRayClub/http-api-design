package org.kaws.common.reponse;

import lombok.Getter;


/**
 * @author Bosco
 * @date 2022/4/1 7:48 下午
 */
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    FAILURE(500, "操作失败");


    /**
     * 状 态 码
     */
    @Getter
    private final int code;

    /**
     * 携 带 消 息
     */
    @Getter
    private final String message;

    /**
     * 构 造 方 法
     */
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
