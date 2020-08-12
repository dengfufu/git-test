package com.zjft.usp.uas.right.dto;

import com.zjft.usp.uas.right.model.SysRightExtraCorp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/7 11:20
 */
@ApiModel("系统权限")
@Getter
@Setter
public class SysRightDto {

    @ApiModelProperty(value = "权限编号")
    private Long rightId;

    @ApiModelProperty(value = "权限名称")
    private String rightName;

    @ApiModelProperty(value = "权限码")
    private String rightCode;

    @ApiModelProperty(value = "父权限编号")
    private Long parentId;

    @ApiModelProperty(value = "应用编号")
    private Integer appId;

    @ApiModelProperty(value = "是否范围权限")
    private String hasScope;

    @ApiModelProperty(value = "范围类型列表")
    private List<Integer> scopeTypeList;

    @ApiModelProperty(value = "范围类型名称")
    private String scopeTypeNames;

    @ApiModelProperty(value = "服务委托方 Y=是 N=否")
    private String serviceDemander;

    @ApiModelProperty(value = "服务委托方公共权限 Y=是 N=否")
    private String serviceDemanderCommon;

    @ApiModelProperty(value = "服务提供商 Y=是 N=否")
    private String serviceProvider;

    @ApiModelProperty(value = "服务提供商公共权限 Y=是 N=否")
    private String serviceProviderCommon;

    @ApiModelProperty(value = "设备使用商 Y=是 N=否")
    private String deviceUser;

    @ApiModelProperty(value = "设备使用商公共权限 Y=是 N=否")
    private String deviceUserCommon;

    @ApiModelProperty(value = "平台管理 Y=是 N=否")
    private String cloudManager;

    @ApiModelProperty(value = "平台管理公共权限 Y=是 N=否")
    private String cloudManagerCommon;

    @ApiModelProperty(value = "包含具体企业 Y=是 N=否")
    private String hasExtraCorp;

    @ApiModelProperty(value = "具体企业ID列表")
    private List<SysRightExtraCorp> extraCorpList;

    @ApiModelProperty(value = "额外企业公共权限 Y=是 N=否")
    private String extraCorpCommon;
}
