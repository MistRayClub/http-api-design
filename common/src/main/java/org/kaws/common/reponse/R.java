package org.kaws.common.reponse;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @author Bosco
 * @date 2022/4/1 6:48 下午
 */
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {

    /**
     * 是 否 成 功
     */
    private boolean success;

    /**
     * 错 误 状 态
     */
    private int code;

    /**
     * 错 误 消 息
     */
    private String msg;

    /**
     * 返 回 数 据
     */
    private T data;

    /**
     * 成 功 操 作
     */
    public static <T> R<T> success() {
        return success(null);
    }

    /**
     * 成 功 操 作 , 携 带 数 据
     */
    public static <T> R<T> success(T data) {
        return success(ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成 功 操 作, 携 带 消 息
     */
    public static <T> R<T> success(String message) {
        return success(message, null);
    }

    /**
     * 成 功 操 作, 携 带 消 息 和 携 带 数 据
     */
    public static <T> R<T> success(String message, T data) {
        return success(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 成 功 操 作, 携 带 自 定 义 状 态 码 和 消 息
     */
    public static <T> R<T> success(int code, String message) {
        return success(code, message, null);
    }

    /**
     * 成 功 操 作, 携 带 自 定义 状 态 码, 消 息 和 数 据
     */
    public static <T> R<T> success(int code, String message, T data) {
        R<T> result = new R<T>();
        result.setCode(code);
        result.setMsg(message);
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    /**
     * 失 败 操 作, 默 认 数 据
     */
    public static <T> R<T> failure() {
        return failure(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 失 败 操 作, 携 带 自 定 义 消 息
     */
    public static <T> R<T> failure(String message) {
        return failure(message, null);
    }

    /**
     * 失 败 操 作, 携 带 自 定 义 消 息 和 数 据
     */
    public static <T> R<T> failure(String message, T data) {
        return failure(ResultCode.FAILURE.getCode(), message, data);
    }

    /**
     * 失 败 操 作, 携 带 自 定 义 状 态 码 和 自 定 义 消 息
     */
    public static <T> R<T> failure(int code, String message) {
        return failure(ResultCode.FAILURE.getCode(), message, null);
    }

    /**
     * 失 败 操 作, 携 带 自 定 义 状 态 码 , 消 息 和 数 据
     */
    public static <T> R<T> failure(int code, String message, T data) {
        R<T> result = new R<T>();
        result.setCode(code);
        result.setMsg(message);
        result.setSuccess(false);
        result.setData(data);
        return result;
    }

    /**
     * Boolean 返 回 操 作, 携 带 默 认 返 回 值
     */
    public static <T> R<T> decide(boolean b) {
        return decide(b, ResultCode.SUCCESS.getMessage(), ResultCode.FAILURE.getMessage());
    }

    /**
     * Boolean 返 回 操 作, 携 带 自 定 义 消 息
     */
    public static <T> R<T> decide(boolean b, String success, String failure) {
        if (b) {
            return success(success);
        } else {
            return failure(failure);
        }
    }

}
