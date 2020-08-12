package com.zjft.usp.wms.business.income.service;

import com.zjft.usp.wms.business.income.model.IncomeWareReversed;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 入库明细通用销账表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-19
 */
public interface IncomeWareReversedService extends IService<IncomeWareReversed> {

    /**
     * 根据入库明细号获取销账单号
     *
     * @author canlei
     * @param detailId
     * @return
     */
    List<Long> listBookIdByDetailId(Long detailId);

    /**
     * 根据入库单明细编号删除
     *
     * @author canlei
     * @param detailId
     */
    void deleteByDetailId(Long detailId);

}
