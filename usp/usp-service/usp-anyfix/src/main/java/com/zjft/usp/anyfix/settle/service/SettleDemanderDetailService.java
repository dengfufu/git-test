package com.zjft.usp.anyfix.settle.service;

import com.zjft.usp.anyfix.settle.dto.SettleDemanderDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleDemanderDetailFilter;
import com.zjft.usp.anyfix.settle.model.SettleDemanderDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import com.zjft.usp.common.model.ListWrapper;

import java.util.List;

/**
 * <p>
 * 供应商结算单明细 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
public interface SettleDemanderDetailService extends IService<SettleDemanderDetail> {

    /**
     * 查询结算对账单
     *
     * @author canlei
     * @param settleDemanderDetailFilter
     * @return
     */
    List<WorkFeeVerifyDto> listWorkFeeVerifyByFilter(SettleDemanderDetailFilter settleDemanderDetailFilter);

    /**
     * 分页查询结算对账单
     *
     * @author canlei
     * @param settleDemanderDetailFilter
     * @return
     */
    ListWrapper<WorkFeeVerifyDto> queryWorkFeeVerifyByFilter(SettleDemanderDetailFilter settleDemanderDetailFilter);

    /**
     * 查询结算工单
     *
     * @param settleDemanderDetailFilter
     * @return
     */
    List<WorkFeeDto> listWorkByFilter(SettleDemanderDetailFilter settleDemanderDetailFilter);

    /**
     * 分页查询结算工单
     *
     * @param settleDemanderDetailFilter
     * @return
     */
    ListWrapper<WorkFeeDto> queryWorkByFilter(SettleDemanderDetailFilter settleDemanderDetailFilter);

    /**
     * 根据结算单编号删除
     *
     * @author canlei
     * @param settleId
     */
    void deleteBySettleId(Long settleId);

    /**
     * 根据结算单号获取明细
     *
     * @param settleId
     * @return
     */
    List<SettleDemanderDetail> listBySettleId(Long settleId);

    /**
     * 根据结算单编号查询工单处理列表
     *
     * @param settleId
     * @return
     */
//    List<WorkDeal> listWorkDealBySettleId(Long settleId);

}
