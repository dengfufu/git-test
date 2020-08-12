package com.zjft.usp.uas.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @author zphu
 * @Description 短信验证码相关信息
 * @date 2019/8/12 19:05
 * @Version 1.0
 **/
@Data
public class VerifyMessageDto {

    @NotEmpty(message = "手机号码（mobile）不能为空")
    /** 手机号码 **/
    private String mobile;

    @Min(value = 0,message = "验证码长度（verifyNum）必须为数字")
    /** 验证码长度 **/
    private String verifyNum;

    @Min(value = 0,message = "验证码（smsCode）必须为数字")
    /** 验证码 **/
    private String smsCode;

    /** 是否是注册验证码 **/
    private Boolean isRegister = false;

    @ApiModelProperty("企业编号")
    private Long corpId;
}
