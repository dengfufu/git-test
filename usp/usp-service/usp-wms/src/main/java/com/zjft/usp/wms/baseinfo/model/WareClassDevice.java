package com.zjft.usp.wms.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物品类型适用设备表
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@ApiModel(value="WareClassDevice对象", description="物品类型适用设备表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ware_class_device")
public class WareClassDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物品类型ID")
    private Long wareClassId;

    @ApiModelProperty(value = "设备小类ID")
    private Long smallClassId;


}
