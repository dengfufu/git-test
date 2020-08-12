package com.zjft.usp.uas.right.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.uas.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 租户设置表
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant")
@ApiModel(value="SysTenant对象", description="租户设置表")
public class SysTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业ID")
    @TableId(value = "corp_id")
    private Long corpId;

    @ApiModelProperty(value = "服务委托方 Y=是 N=否")
    @TableField(value = "service_demander")
    private String serviceDemander;

    @ApiModelProperty(value = "服务提供商 Y=是 N=否")
    @TableField(value = "service_provider")
    private String serviceProvider;

    @ApiModelProperty(value = "设备使用商 Y=是 N=否")
    @TableField(value = "device_user")
    private String deviceUser;

    @ApiModelProperty(value = "平台管理 Y=是 N=否")
    @TableField(value = "cloud_manager")
    private String cloudManager;

    @ApiModelProperty(value = "是否需要绑定企业私有账号")
    @TableField(value = "need_account")
    private boolean needAccount;

    @ApiModelProperty(value = "用户校验API")
    @TableField(value = "apply_check_api")
    private String applyCheckApi;

    @ApiModelProperty(value = "操作人")
    @TableField(value = "operator")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @TableField(value = "operate_time")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @ApiModelProperty(value = "委托商级别")
    @TableField(value = "demander_level")
    private Integer demanderLevel;

    @ApiModelProperty(value = "服务商级别")
    @TableField(value = "service_level")
    private Integer serviceLevel;

}
