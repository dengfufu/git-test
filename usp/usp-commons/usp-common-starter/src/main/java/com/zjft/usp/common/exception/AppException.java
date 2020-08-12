package com.zjft.usp.common.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException{

    private Integer code;

    public AppException(String message) {
        super(message);
        this.code = 0;
    }

    public AppException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
