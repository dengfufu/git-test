package com.zjft.usp.wms.business.stock.composite;

import com.zjft.usp.wms.business.stock.composite.dto.StockNormalAdjustDto;
import com.zjft.usp.wms.business.stock.composite.dto.StockNormalUnAdjustDto;


/**
 * @Author: JFZOU
 * @Date: 2019-11-22 17:07
 */
public interface StockCompositeService {

    /**
     * 调整数量
     *
     * @param stockAdjustDto
     */
    void adjustSituationQty(StockNormalAdjustDto stockAdjustDto);

    /**
     * 取消调整数量（预留接口，用于撤消调整数量）
     *
     * @param stockNormalUnAdjustDto
     */
    void unAdjustSituationQty(StockNormalUnAdjustDto stockNormalUnAdjustDto);
}
