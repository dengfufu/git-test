package com.zjft.usp.zj.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-03-22 21:00
 * @Version 1.0
 */

@Data
public class ScSysUser {

    @ApiModelProperty(value = "老平台用户名")
    private String userId;
    @ApiModelProperty(value = "模块（菜单）权限")
    private String rightM;
    @ApiModelProperty(value = "对象权限")
    private String rightO;
    @ApiModelProperty(value = " 动作权限")
    private String rightA;
    private String rightE;
    private String inWho;
    private String inDate;
}
