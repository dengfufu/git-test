package com.zjft.usp.uas.right.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/26 17:11
 */
@ApiModel(value = "系统权限")
@Getter
@Setter
public class SysRightFilter extends Page {

    @ApiModelProperty(value = "权限编号")
    private Long rightId;

    @ApiModelProperty(value = "权限名称")
    private String rightName;

    @ApiModelProperty(value = "权限码")
    private String rightCode;

    @ApiModelProperty(value = "应用编号")
    private Long appId;

    @ApiModelProperty(value = "父权限编号")
    private Long parentId;

    @ApiModelProperty(value = "系统权限 1=是")
    private Integer sysType;

    @ApiModelProperty(value = "是否是范围权限")
    private String hasScope;

    @ApiModelProperty(value = "范围类型列表")
    private List<Integer> scopeTypeList;

    @ApiModelProperty(value = "服务委托方 Y=是 N=否")
    private String serviceDemander;

    @ApiModelProperty(value = "服务提供商 Y=是 N=否")
    private String serviceProvider;

    @ApiModelProperty(value = "设备使用商 Y=是 N=否")
    private String deviceUser;

    @ApiModelProperty(value = "平台管理 Y=是 N=否")
    private String cloudManager;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "模糊查询")
    private String matchFilter;

    @ApiModelProperty(value = "用户id")
    private Long userId;
}
