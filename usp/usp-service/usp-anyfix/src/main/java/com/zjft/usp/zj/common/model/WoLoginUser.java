package com.zjft.usp.zj.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-03-22 11:04
 * @Version 1.0
 */
@Data
public class WoLoginUser {

    @ApiModelProperty(value = "老平台SC用户对象")
    private ScSysUser scSysUser;
}
