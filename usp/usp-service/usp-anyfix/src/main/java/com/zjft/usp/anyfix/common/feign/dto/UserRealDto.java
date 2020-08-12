package com.zjft.usp.anyfix.common.feign.dto;

import lombok.Data;

/**
 * @author zgpi
 * @date 2019/11/13 18:45
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
}
