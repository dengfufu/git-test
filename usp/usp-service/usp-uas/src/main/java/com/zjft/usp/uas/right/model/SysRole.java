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
 * 角色表
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value = "SysRole对象", description = "角色表")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "role_id")
    private Long roleId;

    @ApiModelProperty(value = "企业编号")
    @TableField(value = "corp_id")
    private Long corpId;

    @ApiModelProperty(value = "角色名称")
    @TableField(value = "role_name")
    private String roleName;

    @ApiModelProperty(value = "系统类型", notes = "1=系统角色")
    @TableField(value = "sys_type")
    private Integer sysType;

    @ApiModelProperty(value = "是否可用")
    @TableField(value = "enabled")
    private String enabled;

    @ApiModelProperty(value = "描述")
    @TableField(value = "description")
    private String description;

    @ApiModelProperty(value = "操作人")
    @TableField(value = "operator")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @TableField(value = "operator_time")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operatorTime;


}
