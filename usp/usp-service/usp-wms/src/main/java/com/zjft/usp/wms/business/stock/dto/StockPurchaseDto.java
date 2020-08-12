package com.zjft.usp.wms.business.stock.dto;

import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.stock.model.StockPurchase;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2019-11-07 16:25
 */
@Data
public class StockPurchaseDto {

    /**
     * 库存通用对象
     */
    private List<StockCommon> stockCommonList;

    /**
     * 库存采购专属对象
     */
    private List<StockPurchase> stockPurchaseList;
}
