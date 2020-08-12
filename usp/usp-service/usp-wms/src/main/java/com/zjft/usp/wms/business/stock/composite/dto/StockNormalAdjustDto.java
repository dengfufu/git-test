package com.zjft.usp.wms.business.stock.composite.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 库存业务正常调整dto类（适用于调拨、出库）
 *
 * @Author: JFZOU
 * @Date: 2019-11-25 9:04
 */
@Data
public class StockNormalAdjustDto {

    @ApiModelProperty(value = "源库存明细ID")
    private Long sourceStockId;

    @ApiModelProperty(value = "库存明细的原库存状态")
    private int oldSituation;

    @ApiModelProperty(value = "库存明细的新库存状态")
    private int newSituation;

    @ApiModelProperty(value = "库存明细的原库房ID")
    private Long oldDepotId;

    @ApiModelProperty(value = "库存明细的新库房ID")
    private Long newDepotId;

    @ApiModelProperty(value = "需要调整的数量")
    private int adjustQty;

    @ApiModelProperty(value = "处理人ID")
    private Long doBy;

    @ApiModelProperty(value = "入库单明细ID，ID可以是常规入库单明细ID，调拨入库单明细ID，工单明细ID，还可以是调库入库单明细ID等。")
    private Long formDetailId;

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;
}
