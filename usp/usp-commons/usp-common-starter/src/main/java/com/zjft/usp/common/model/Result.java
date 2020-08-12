package com.zjft.usp.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 格式化返回信息
 *
 * @author CK
 * @date 2019-08-02 14:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    public static int SUCCESS = 0;
    public static int FAIL = 1;

    private T data;
    private Integer code;
    private String msg;

    public static <T> Result<T> succeed() {
        return succeedWith(null, SUCCESS, "");
    }

    public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, SUCCESS, msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeedWith(model, SUCCESS, msg);
    }

    public static <T> Result<T> succeedStr(T model) {
        return succeedWith(model, SUCCESS, "");
    }

    public static <T> Result<T> succeed(T model) {
        return succeedWith(model, SUCCESS, "");
    }

    public static <T> Result<T> succeedWith(T datas, Integer code, String msg) {
        return new Result<>(datas, code, msg);
    }

    public static <T> Result<T> failed(String msg) {
        return failedWith(null, FAIL, msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return failedWith(model, FAIL, msg);
    }

    public static <T> Result<T> failedWith(T datas, Integer code, String msg) {
        return new Result<>(datas, code, msg);
    }

    public static boolean isSucceed(Result result) {
        return result != null && result.getCode() == Result.SUCCESS;
    }

}
