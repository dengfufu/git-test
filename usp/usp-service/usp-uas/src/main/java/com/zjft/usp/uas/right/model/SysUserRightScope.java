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
@TableName("sys_user_right_scope")
@ApiModel(value = "SysUserRightScope对象", description = "人员范围权限表")
public class SysUserRightScope implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "人员编号")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "企业编号")
    @TableField("corp_id")
    private Long corpId;

    @ApiModelProperty(value = "权限编号")
    @TableField("right_id")
    private Long rightId;

    @ApiModelProperty(value = "范围类型，1=服务网点")
    @TableField("scope_type")
    private Integer scopeType;

    @ApiModelProperty(value = "是否有全部权限")
    @TableField("has_all_scope")
    private String hasAllScope;

    @ApiModelProperty(value = "是否有所在组织权限")
    @TableField("has_own_scope")
    private String hasOwnScope;

    @ApiModelProperty(value = "是否有所在组织的下级权限")
    @TableField("has_own_lower_scope")
    private String hasOwnLowerScope;


}
