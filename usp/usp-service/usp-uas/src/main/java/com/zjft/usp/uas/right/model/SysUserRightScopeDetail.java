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
 * 人员范围权限表
 * </p>
 *
 * @author zgpi
 * @since 2020-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_right_scope_d")
@ApiModel(value="SysUserRightScopeDetail对象", description="人员范围权限表")
public class SysUserRightScopeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "组织ID")
    @TableField("org_id")
    private String orgId;

    @ApiModelProperty(value = "是否包含下级， Y=是 N=否")
    @TableField("contain_lower")
    private String containLower;


}
