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
 * 权限映射表
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_right_url")
@ApiModel(value="SysRightUrl对象", description="权限映射表")
public class SysRightUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "权限类型 1=公共权限")
    @TableField(value = "right_type")
    private Integer rightType;

    @ApiModelProperty(value = "权限对应的请求URI")
    @TableField(value = "uri")
    private String uri;

    @ApiModelProperty(value = "请求方法")
    @TableField(value = "path_method")
    private String pathMethod;

    @ApiModelProperty(value = "权限ID")
    @TableField(value = "right_id")
    private Long rightId;

    @ApiModelProperty(value = "描述")
    @TableField(value = "description")
    private String description;


}
