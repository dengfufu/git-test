package com.zjft.usp.sms.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author : dcyu
 * @Date : 2019年8月12日
 * @Desc : 短信实体类
 */
@Data
public class Sms {
    /**手机号码*/
    private String phoneNumbers;
    /**应用类型*/
    private int appId;
    /**短信类型*/
    private int smsType;
    /**短信变量对应参数*/
    private String param;
    /**发送时间 格式：yyyyMMddHHmm*/
    private Date sendDate;
    /**回调地址*/
    private String callBack;
}
