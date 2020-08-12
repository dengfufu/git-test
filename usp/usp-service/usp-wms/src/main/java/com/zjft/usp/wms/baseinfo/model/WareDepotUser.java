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
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ware_depot_user")
@ApiModel(value="WareDepotUser对象", description="库房人员表")
public class WareDepotUser extends Model<WareDepotUser> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "库房ID")
    @TableId("depot_id")
    private Long depotId;

    @ApiModelProperty(value = "用户ID")
    @TableId("user_id")
    private Long userId;

    @ApiModelProperty("用户名")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty(value = "人员类型(1=库房负责人2=库房收货人)")
    @TableId("type")
    private Integer type;

    @ApiModelProperty(value = "顺序号")
    private Integer sortNo;


}
