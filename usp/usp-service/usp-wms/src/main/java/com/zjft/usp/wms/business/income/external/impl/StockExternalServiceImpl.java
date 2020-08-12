package com.zjft.usp.wms.business.income.external.impl;

import com.zjft.usp.wms.business.income.external.StockExternalService;
import com.zjft.usp.wms.business.income.external.conversion.IncomeToStock;
import com.zjft.usp.wms.business.income.model.IncomeWareCommon;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.stock.service.StockCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Author: JFZOU
 * @Date: 2019-11-12 16:50
 */
@Transactional(rollbackFor = Exception.class)
@Component
public class StockExternalServiceImpl implements StockExternalService {

    @Autowired
    private StockCommonService stockCommonService;


    @Override
    public void save(List<StockCommon> stockCommonList) {
        this.stockCommonService.saveBatch(stockCommonList);
    }

    /***
     * 调整库存
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeWareCommon
     * @return void
     */
    @Override
    public void adjustStock(IncomeWareCommon incomeWareCommon) {
        if (incomeWareCommon != null) {
            List<StockCommon> stockCommonList = new ArrayList<StockCommon>();
            StockCommon stockCommon = new IncomeToStock().getStockCommon(incomeWareCommon);
            stockCommonList.add(stockCommon);

        }
    }

}
