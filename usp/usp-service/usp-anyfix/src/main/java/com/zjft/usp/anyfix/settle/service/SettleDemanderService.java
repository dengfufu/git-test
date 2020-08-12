package com.zjft.usp.anyfix.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.settle.dto.BankAccountDto;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderDto;
import com.zjft.usp.anyfix.settle.filter.SettleDemanderFilter;
import com.zjft.usp.anyfix.settle.model.SettleDemander;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.model.ListWrapper;

import java.util.List;

/**
 * <p>
 * 供应商结算单 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
public interface SettleDemanderService extends IService<SettleDemander> {

    /**
     * 分页查询
     *
     * @author canlei
     * @param settleDemanderFilter
     * @return
     */
    ListWrapper<SettleDemanderDto> query(SettleDemanderFilter settleDemanderFilter, Long userId, Long corpId);

    /**
     * 添加
     *
     * @author canlei
     * @param settleDemanderDto
     * @param curUserId
     */
    void add(SettleDemanderDto settleDemanderDto, Long curUserId);

    /**
     * 根据id查询
     *
     * @author canlei
     * @param settleId
     * @return
     */
    SettleDemanderDto findById(Long settleId);

    /**
     * 核对结算单
     *
     * @author canlei
     * @param settleDemanderDto
     * @param curUserId
     */
    void check(SettleDemanderDto settleDemanderDto, Long curUserId);

    /**
     * 删除结算单
     *
     * @author canlei
     * @param settleId
     */
    void delete(Long settleId);

    /**
     * 根据结算单编号List获取结算单编号和结算工单总费用的映射
     *
     * @author canlei
     * @param settleIdList
     * @return
     */
//    Map<Long, BigDecimal> mapSettleIdAndFee(List<Long> settleIdList);

    /**
     * 查询可结算工单
     *
     * @param workFilter
     * @return
     */
//    SettleDemanderDto listCanSettleWork(WorkFilter workFilter);

    /**
     * 根据工单生成结算单
     *
     * @param settleDemanderDto
     * @param userId
     * @param corpId
     * @return
     */
//    Long addByWork(SettleDemanderDto settleDemanderDto, Long userId, Long corpId);

    /**
     * 根据结算单号获取应结算总金额
     *
     * @param settleId
     * @return
     */
//    BigDecimal getSettleFeeBySettleId(Long settleId);

    /**
     * 查询收款账户信息
     *
     * @param settleDemanderFilter
     * @return
     */
    ListWrapper<BankAccountDto> listBankAccount(SettleDemanderFilter settleDemanderFilter);

    /**
     * 修改结算单
     *
     * @param settleDemanderDto
     * @param userId
     * @param serviceCorp
     */
    void update(SettleDemanderDto settleDemanderDto, Long userId, Long serviceCorp);

    /**
     * 生成结算单号
     *
     * @param settleDemanderDto
     * @return
     */
    String generateSettleCode(SettleDemanderDto settleDemanderDto);

    /**
     * 委托商确认费用根据对账单列表批量生成结算单
     *
     * @param workDealList
     * @param userId
     */
    void batchAddByWorkListAndDemander(List<WorkDeal> workDealList, Long userId, Long demanderCorp);

    /**
     * 自动确认费用根据对账单列表批量生成结算单
     *
     * @param workDealList
     * @param userId
     * @param serviceCorp
     */
    void batchAddByWorkListAndService(List<WorkDeal> workDealList, Long userId, Long serviceCorp);

    /**
     * 查询可结算工单
     *
     * @param workFilter
     * @return
     */
    List<WorkFeeDto> listCanSettleWork(WorkFilter workFilter);

}
