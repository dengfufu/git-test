package com.zjft.usp.uas.user.dto;

import lombok.Data;

/**
 * @description: 用户支付宝Dto
 * @author chenxiaod
 * @date 2019/8/13 9:50
 */
@Data
public class UserAlipayDto {

    /** 支付宝id **/
    private String alipayUserId;

    /** 支付宝登陆id **/
    private String alipayLogonId;

    /** 用户id **/
    private Long userId;
}
