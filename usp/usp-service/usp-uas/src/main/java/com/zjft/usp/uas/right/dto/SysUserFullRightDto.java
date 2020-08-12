package com.zjft.usp.uas.right.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户当前企业的所有权限，包含范围权限
 *
 * @author: CK
 * @create: 2020-04-07 13:08
 */
@Setter
@Getter
public class SysUserFullRightDto {

    @ApiModelProperty(value = "权限编号")
    private Long rightId;

    @ApiModelProperty(value = "权限名称")
    private String rightName;

    @ApiModelProperty(value = "权限码")
    private String rightCode;

    @ApiModelProperty(value = "应用编号")
    private Integer appId;

    @ApiModelProperty(value = "是否范围权限")
    private String hasScope;

    @ApiModelProperty(value = "范围权限集合")
    List<SysUserRightScopeDto> scopeTypeList = new ArrayList<>();

}
