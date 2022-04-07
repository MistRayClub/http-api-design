package org.kaws.common.exception;

/**
 * @author Bosco
 * @date 2022/4/7 3:20 下午
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -3178487481087055590L;

    private Integer code;

    public BusinessException() {
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
    }

    public Integer getCode() {
        return code;
    }
}
