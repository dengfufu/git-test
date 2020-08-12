package com.zjft.usp.uas.user.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 登录日志filter
 *
 * @author cxd
 * @version 1.0
 * @date 2020/5/26 3:02 下午
 **/

@ApiModel(value = "登录日志filter")
@Data
public class UserLogonLogFilter extends Page {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String customCorpName;

    @ApiModelProperty(value = "当前企业编号")
    private Long corpId;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "操作类型，1=登录 2=自动登录 3=登出")
    private Long operateType;

    @ApiModelProperty(value = "登录类型，1=手机号+密码 2=手机号+短信验证码 3=微信")
    private Long logonType;

    @ApiModelProperty(value = "登录结果，1=登录成功 2=密码错误 3=账号锁定")
    private Long logonResult;

    @ApiModelProperty(value = "应用标识")
    private String clientId;

    @ApiModelProperty(value = "设备类型")
    private Long deviceType;

    @ApiModelProperty(value = "操作时间开始")
    private Date operateTimeStart;

    @ApiModelProperty(value = "操作时间结束")
    private Date operateTimeEnd;
}
