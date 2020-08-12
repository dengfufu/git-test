package com.zjft.usp.msg.model;

import com.zjft.usp.common.model.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author : dcyu
 * @Date : 2019/8/19 15:43
 * @Desc : 短信发送结果
 * @Version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SmsResult {

    /** 返回状态码*/
    private String code = "";

    /** 状态码描述*/
    private String message = "";

    /** 回执ID*/
    private String bizId = "";

    /** 请求ID*/
    private String requestId = "";

    private SmsResult() {}

    private SmsResult(String code, String message){
        this.code = code;
        this.message =message;
    }

    private SmsResult(String code, String message, String bizId, String requestId){
        this.code = code;
        this.message =message;
        this.bizId = bizId;
        this.requestId = requestId;
    }

    public static SmsResult info(String message){
        return new SmsResult(String.valueOf(Result.SUCCESS), message);
    }

    public static SmsResult fail(String message){
        return new SmsResult(String.valueOf(Result.FAIL), message, "", "");
    }

    public static SmsResult fail(String message, String bizId, String requestId){
        return new SmsResult(String.valueOf(Result.FAIL), message, bizId, requestId);
    }

    public static SmsResult success(String bizId, String requestId){
        return new SmsResult(String.valueOf(Result.SUCCESS), "", bizId, requestId);
    }

}
