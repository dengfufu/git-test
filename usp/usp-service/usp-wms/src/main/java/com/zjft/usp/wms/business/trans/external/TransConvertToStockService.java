package com.zjft.usp.wms.business.trans.external;

import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;

import java.util.List;

/**
 * external用于调用外部业务，income业务里不要直接调用外部业务接口，如果需要定义外部接口，在external包下管理
 * 调用外部类，不直接调用外部业务类，通过本类做个包装
 * @Author: JFZOU
 * @Date: 2019-11-12 16:48
 */
public interface TransConvertToStockService {

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
     * @param transWareCommon
     * @return void
     */
    void adjustStock(TransWareCommon transWareCommon);
}
