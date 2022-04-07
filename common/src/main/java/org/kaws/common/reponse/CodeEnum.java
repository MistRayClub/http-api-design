package org.kaws.common.reponse;

import lombok.Getter;


/**
 * @author Bosco
 * @date 2022/4/1 7:48 下午
 */
public enum CodeEnum {

    SUCCESS(200, "OK"),

    FAILURE(500, "Internal Server Error"),

    TOKEN_INVALID(401, "Token Invalid"),

    TOKEN_EXPIRED(401, "Token Expired"),

    ;

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


    CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
