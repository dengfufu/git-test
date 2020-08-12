package com.zjft.usp.uas.right.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色范围权限表
 * </p>
 *
 * @author zgpi
 * @since 2020-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_right_scope")
@ApiModel(value="SysRoleRightScope对象", description="角色范围权限表")
public class SysRoleRightScope implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "角色编号")
    @TableField(value = "role_id")
    private Long roleId;

    @ApiModelProperty(value = "权限编号")
    @TableField(value = "right_id")
    private Long rightId;

    @ApiModelProperty(value = "范围类型，1=服务网点")
    @TableField(value = "scope_type")
    private Integer scopeType;

    @ApiModelProperty(value = "是否有全部权限")
    @TableField(value = "has_all_scope")
    private String hasAllScope;

    @ApiModelProperty(value = "是否有所在组织权限")
    @TableField(value = "has_own_scope")
    private String hasOwnScope;

    @ApiModelProperty(value = "是否有所在组织的下级权限")
    @TableField(value = "has_own_lower_scope")
    private String hasOwnLowerScope;


}
