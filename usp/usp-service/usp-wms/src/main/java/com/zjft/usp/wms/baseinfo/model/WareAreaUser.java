package com.zjft.usp.wms.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库房人员表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ware_area_user")
@ApiModel(value="WareAreaUser对象", description="库房人员表")
public class WareAreaUser extends Model<WareAreaUser> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "区域ID")
    @TableId("area_id")
    private Long areaId;

    @ApiModelProperty(value = "用户ID")
    @TableId("user_id")
    private Long userId;

    @ApiModelProperty("用户名")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty(value = "人员类型(10=区域负责人)")
    @TableId("type")
    private Integer type;

    @ApiModelProperty(value = "顺序号")
    private Integer sortNo;


}
