package com.zjft.usp.uas.user.dto;

import lombok.Data;

/**
 * @description: 用户微信Dto
 * @author chenxiaod
 * @date 2019/8/13 9:56
 */
@Data
public class UserWxDto extends UserInfoDto{

    /** 微信openId **/
    private String openId;

    /** 微信unionId **/
    private String unionId;

    private String userName;

    private String code;

    private String userId;
}
