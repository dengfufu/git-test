package com.zjft.usp.wms.business.income.external.conversion;

import com.zjft.usp.wms.business.income.model.IncomeWareCommon;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import lombok.Data;
import org.springframework.beans.BeanUtils;


/**
 * 对象转换
 * 将入库相关对象转换成库存对象，各业务自己负责转换，不要在stock业务中转换。
 *
 * @Author: JFZOU
 * @Date: 2019-11-12 13:45
 */
@Data
public class IncomeToStock {

    /**
     * 把入库对象转换成库存通用对象
     *
     * @param incomeCommon
     * @return
     */
    public StockCommon getStockCommon(IncomeWareCommon incomeCommon) {
        StockCommon stockCommon = new StockCommon();
        BeanUtils.copyProperties(incomeCommon, stockCommon);
        /**纠正属性赋值*/
        stockCommon.setInFormDetailId(incomeCommon.getId());
        stockCommon.setIncomeQty(incomeCommon.getQuantity());
        return stockCommon;
    }
}

