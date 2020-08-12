package com.zjft.usp.anyfix.common.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/16 09:32
 */
@ApiModel("角色人员")
@Data
public class SysRoleUserDto {

    @ApiModelProperty(value = "人员编号")
    private Long userId;

    @ApiModelProperty(value = "人员姓名")
    private String userName;

    @ApiModelProperty(value = "角色编号")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
