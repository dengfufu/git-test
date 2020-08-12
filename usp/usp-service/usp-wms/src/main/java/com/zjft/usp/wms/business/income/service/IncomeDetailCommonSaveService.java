package com.zjft.usp.wms.business.income.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.wms.business.income.model.IncomeDetailCommonSave;

/**
 * <p>
 * 入库明细信息共用表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface IncomeDetailCommonSaveService extends IService<IncomeDetailCommonSave> {

    /**
     * 根据入库单号删除入库明细信息
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeId
     * @return void
     */
    void deleteByIncomeId(Long incomeId);

}
