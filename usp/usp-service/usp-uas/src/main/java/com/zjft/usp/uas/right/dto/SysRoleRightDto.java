package com.zjft.usp.uas.right.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色权限
 * @author zgpi
 * @version 1.0
 * @date 2019/11/28 15:29
 */
@ApiModel("角色权限")
@Getter
@Setter
public class SysRoleRightDto {

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "角色编号")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "权限编号")
    private Long rightId;

    @ApiModelProperty(value = "权限名称")
    private String rightName;

    @ApiModelProperty(value = "权限码")
    private String rightCode;

    @ApiModelProperty(value = "权限类型")
    private Integer rightType;

    @ApiModelProperty(value = "应用编号")
    private Integer appId;

    @ApiModelProperty(value = "请求路径")
    private String uri;

    @ApiModelProperty(value = "请求方法")
    private String pathMethod;

    @ApiModelProperty(value = "是否有范围")
    private String hasScope;
}
