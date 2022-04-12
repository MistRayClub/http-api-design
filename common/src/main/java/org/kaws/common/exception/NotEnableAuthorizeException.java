package org.kaws.common.exception;

/**
 * @author Bosco
 * @date 2022/4/12 7:10 下午
 */
public class NotEnableAuthorizeException extends RuntimeException {

    private static final long serialVersionUID = 5511910566699598245L;

    private Integer code;

    public NotEnableAuthorizeException() {
    }

    public NotEnableAuthorizeException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
