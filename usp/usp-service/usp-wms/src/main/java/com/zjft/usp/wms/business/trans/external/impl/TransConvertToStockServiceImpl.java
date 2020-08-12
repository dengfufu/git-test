package com.zjft.usp.wms.business.trans.external.impl;

import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.stock.service.StockCommonService;
import com.zjft.usp.wms.business.trans.external.TransConvertToStockService;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @Author: JFZOU
 * @Date: 2019-11-12 16:50
 */
@Transactional(rollbackFor = Exception.class)
@Component
public class TransConvertToStockServiceImpl implements TransConvertToStockService {

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
     * @param transWareCommon
     * @return void
     */
    @Override
    public void adjustStock(TransWareCommon transWareCommon) {
        if (transWareCommon != null) {

        }
    }

}
