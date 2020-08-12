package com.zjft.usp.uas.right.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限范围类型表
 * </p>
 *
 * @author zgpi
 * @since 2020-3-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_right_scope_type")
@ApiModel(value="SysRightScopeType对象", description="权限范围类型表")
public class SysRightScopeType {

    @ApiModelProperty(value = "权限编号")
    @TableId("right_id")
    private Long rightId;

    @ApiModelProperty(value = "范围类型", notes = "1=服务网点")
    @TableField("scope_type")
    private Integer scopeType;
}
