package com.zjft.usp.uas.user.dto;

import lombok.Data;

/**
 * @author zphu
 * @date 2019/8/29 8:45
 * @Version 1.0
 **/
@Data
public class UserRealDto {
    /** 手机号 **/
    private String mobile;
    /** 姓名 **/
    private String userName;
    /** 身份证号 **/
    private String idCard;
    /** 实名认证（1=已认证） **/
    private Integer verified;
    /** 支付宝认证 **/
    private Boolean alipay = false;
    /** 微信认证 **/
    private Boolean wx = false;
}
