package com.zjft.usp.uas.right.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_right")
@ApiModel(value = "SysRight对象", description = "权限表")
public class SysRight implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限编号")
    @TableId(value = "right_id")
    private Long rightId;

    @ApiModelProperty(value = "权限名称")
    @TableField("right_name")
    private String rightName;

    @ApiModelProperty(value = "权限码")
    @TableField("right_code")
    private String rightCode;

    @ApiModelProperty(value = "父权限编号")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "应用编号")
    @TableField("app_id")
    private Integer appId;

    @ApiModelProperty(value = "系统权限 1=是")
    @TableField("sys_type")
    private Integer sysType;

    @ApiModelProperty(value = "是否范围权限")
    @TableField("has_scope")
    private String hasScope;

    @ApiModelProperty(value = "服务委托方 Y=是 N=否")
    @TableField("service_demander")
    private String serviceDemander;

    @ApiModelProperty(value = "服务委托方公共权限 Y=是 N=否")
    @TableField("service_demander_common")
    private String serviceDemanderCommon;

    @ApiModelProperty(value = "服务提供商 Y=是 N=否")
    @TableField("service_provider")
    private String serviceProvider;

    @ApiModelProperty(value = "服务提供商公共权限 Y=是 N=否")
    @TableField("service_provider_common")
    private String serviceProviderCommon;

    @ApiModelProperty(value = "设备使用商 Y=是 N=否")
    @TableField("device_user")
    private String deviceUser;

    @ApiModelProperty(value = "设备使用商公共权限 Y=是 N=否")
    @TableField("device_user_common")
    private String deviceUserCommon;

    @ApiModelProperty(value = "平台管理 Y=是 N=否")
    @TableField("cloud_manager")
    private String cloudManager;

    @ApiModelProperty(value = "平台管理公共权限 Y=是 N=否")
    @TableField("cloud_manager_common")
    private String cloudManagerCommon;

    @ApiModelProperty(value = "包含具体企业 Y=是 N=否")
    @TableField("has_extra_corp")
    private String hasExtraCorp;

}
