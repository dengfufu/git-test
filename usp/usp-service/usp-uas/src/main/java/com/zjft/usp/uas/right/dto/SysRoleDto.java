package com.zjft.usp.uas.right.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 角色Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-12 09:51
 **/
@Getter
@Setter
public class SysRoleDto {

    @ApiModelProperty(value = "角色编号")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "系统类型", notes = "1=系统角色")
    private Integer sysType;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "应用编号", notes = "多个用逗号隔开")
    private String appIds;

    @ApiModelProperty(value = "应用名称", notes = "多个用逗号隔开")
    private String appNames;

    @ApiModelProperty(value = "权限编号列表")
    List<Long> rightIdList;

    @ApiModelProperty(value = "角色权限列表")
    List<SysRoleRightDto> rightList;

    @ApiModelProperty(value = "角色范围权限列表")
    List<SysRoleRightScopeDto> roleRightScopeList;

    @ApiModelProperty(value = "人员编号")
    private Long userId;

    @ApiModelProperty(value = "判断参数（是否紫金）")
    private String key;
}
