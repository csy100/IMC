package com.csy.imc.common;

import lombok.Data;

@Data
public class Result<T> {
    //前后端交互编码
    private Integer code;
    private String message;
    private T data;

    /**
     * 错误响应编码
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(String message) {
        Result result = new Result();
        result.code = 0;
        result.message = message;
        return result;
    }

    /**
     * 成功响应编码
     * @param object
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.data = object;
        return result;
    }
}
