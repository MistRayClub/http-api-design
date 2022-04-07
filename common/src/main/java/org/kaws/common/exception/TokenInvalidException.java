package org.kaws.common.exception;

/**
 * @author Bosco
 * @date 2022/4/7 4:37 下午
 */
public class TokenInvalidException extends RuntimeException {

    private static final long serialVersionUID = -2705918487129351480L;

    private Integer code;

    public TokenInvalidException() {

    }

    public TokenInvalidException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public TokenInvalidException(String message) {
        super(message);
    }

    public Integer getCode() {
        return code;
    }
}
