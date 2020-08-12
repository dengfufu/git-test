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
 * @author: CK
 * @create: 2020-04-10 15:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_right_extra_corp")
@ApiModel(value = "SysRightExtraCorp对象", description = "权限额外企业表")
public class SysRightExtraCorp {

    @ApiModelProperty(value = "权限编号")
    @TableId("right_id")
    private Long rightId;

    @ApiModelProperty(value = "企业ID")
    @TableField(value = "corp_id")
    private Long corpId;

    @ApiModelProperty(value = "企业公共权限")
    @TableField(value = "common")
    private String common;

}
