package com.zjft.usp.wms.business.stock.dto;

import com.zjft.usp.wms.business.stock.model.StockCommon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2019-11-20 14:11
 */
@Data
public class StockCommonDto extends StockCommon {

    @ApiModelProperty(value = "分箱号")
    private Integer subBoxNum;

    @ApiModelProperty(value = "数量")
    private Integer quantity = 0;
}
