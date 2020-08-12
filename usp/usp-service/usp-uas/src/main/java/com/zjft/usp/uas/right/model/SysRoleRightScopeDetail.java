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
 * 角色范围权限表
 * </p>
 *
 * @author zgpi
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_right_scope_d")
@ApiModel(value = "SysRoleRightScopeDetail对象", description = "角色范围权限表")
public class SysRoleRightScopeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "组织ID")
    @TableField("org_id")
    private String orgId;

    @ApiModelProperty(value = "是否包含下级， Y=是 N=否")
    @TableField("contain_lower")
    private String containLower;


}
