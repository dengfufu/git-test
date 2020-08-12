package com.zjft.usp.anyfix.work.ware.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 物品dto
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/14 1:35 下午
 **/
@ApiModel("物品dto")
@Data
public class WareDto {

    @ApiModelProperty("使用编号")
    private Long usedId;

    @ApiModelProperty("回收编号")
    private Long recycleId;

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "物品编号")
    private Long wareId;

    @ApiModelProperty(value = "物品型号名称")
    private String modelName;

    @ApiModelProperty(value = "物品品牌名称")
    private String brandName;

    @ApiModelProperty(value = "物品分类名称")
    private String catalogName;

    @ApiModelProperty(value = "物品序列号")
    private String wareSerial;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "物品单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

}
