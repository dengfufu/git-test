package com.zjft.usp.anyfix.common.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author canlei
 * @date 2019-08-04
 */
@ApiModel("企业用户表")
@Getter
@Setter
public class CorpUserDto implements Serializable {

    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("添加时间")
    private Date addTime;

    @ApiModelProperty("角色名称")
    private String roleNames;

    @ApiModelProperty("第三方系统用户id")
    private String account;

}