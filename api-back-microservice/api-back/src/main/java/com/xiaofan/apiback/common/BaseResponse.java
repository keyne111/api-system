package com.xiaofan.apiback.common;

import java.io.Serializable;

import lombok.Data;

/**
 * 通用返回类
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(com.xiaofan.apiback.common.ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
