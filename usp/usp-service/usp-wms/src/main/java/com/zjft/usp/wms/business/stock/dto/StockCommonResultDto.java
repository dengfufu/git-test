package com.zjft.usp.wms.business.stock.dto;

import com.zjft.usp.wms.business.stock.model.StockCommon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2019-11-25 15:11
 */
@Data
public class StockCommonResultDto extends StockCommon {

    @ApiModelProperty(value = "型号名称")
    private String modelName;

    @ApiModelProperty(value = "分类名称")
    private String catalogName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "物料状态名称")
    private String statusName;

    @ApiModelProperty(value = "存储状态名称")
    private String situationName;

    @ApiModelProperty(value = "产权名称")
    private String propertyRightName;

    @ApiModelProperty(value = "库房名称")
    private String depotName;

    @ApiModelProperty(value = "数量")
    private Integer quantity;
}
