package com.zjft.usp.wms.business.income.external;

import com.zjft.usp.wms.business.income.model.IncomeWareCommon;
import com.zjft.usp.wms.business.stock.model.StockCommon;

import java.util.List;

/**
 * external用于调用外部业务，income业务里不要直接调用外部业务接口，如果需要定义外部接口，在external包下管理
 * 调用外部类，不直接调用外部业务类，通过本类做个包装
 * @Author: JFZOU
 * @Date: 2019-11-12 16:48
 */
public interface StockExternalService {

    /**
     * 简化调用
     *
     * @param stockCommonList 库存共用对象列表
     */
    void save(List<StockCommon> stockCommonList);

    /***
     * 调整库存
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeWareCommon
     * @return void
     */
    void adjustStock(IncomeWareCommon incomeWareCommon);
}
