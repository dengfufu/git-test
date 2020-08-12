package com.zjft.usp.uas.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户登录日志表
 * </p>
 *
 * @author zgpi
 * @since 2020-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_user_logon_log")
@ApiModel(value="UserLogonLog对象", description="用户登录日志表")
public class UserLogonLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志ID")
    @TableId("log_id")
    private Long logId;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "手机号")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "操作类型，1=登录 2=自动登录 3=登出")
    @TableField("operate_type")
    private Integer operateType;

    @ApiModelProperty(value = "登录类型， 1=手机号+密码 2=手机号+短信验证码 3=微信")
    @TableField("logon_type")
    private Integer logonType;

    @ApiModelProperty(value = "登录结果，1=登录成功 2=密码错误")
    @TableField("logon_result")
    private Integer logonResult;

    @ApiModelProperty(value = "操作详情")
    @TableField("details")
    private String details;

    @ApiModelProperty(value = "操作时间")
    @TableField("operate_time")
    private Date operateTime;

    @ApiModelProperty(value = "经度")
    @TableField("lon")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    @TableField("lat")
    private BigDecimal lat;

    @ApiModelProperty(value = "应用ID")
    @TableField("app_id")
    private Integer appId;

    @ApiModelProperty(value = "应用标识")
    @TableField("client_id")
    private String clientId;

    @ApiModelProperty(value = "设备串号")
    @TableField("device_id")
    private String deviceId;

    @ApiModelProperty(value = "设备类型")
    @TableField("device_type")
    private Integer deviceType;

    @ApiModelProperty(value = "操作系统版本")
    @TableField("os_version")
    private String osVersion;


}
