package com.zjft.usp.wms.business.stock.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 库存实时总账采购入库专属表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("stock_purchase")
@ApiModel(value = "StockPurchase对象", description = "库存实时总账采购入库专属表")
public class StockPurchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "库存明细ID")
    private Long stockId;

    @ApiModelProperty(value = "采购明细单号")
    private String purchaseDetailId;


}
