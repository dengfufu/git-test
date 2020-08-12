package com.zjft.usp.pay.business.service;

import com.alipay.api.AlipayApiException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.pay.business.dto.PaymentApplyDemanderDto;
import com.zjft.usp.pay.business.filter.PaymentApplyFilter;
import com.zjft.usp.pay.business.model.PaymentApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 支付申请表  服务类
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
public interface PaymentApplyService extends IService<PaymentApply> {

    /**
     * 委托商结算-创建支付订单申请
     *
     * @param paymentApplyDemanderDTO
     * @param currentUserId
     * @return
     */
    PaymentApply demanderPaymentApplyAdd(PaymentApplyDemanderDto paymentApplyDemanderDTO, Long currentUserId);

    /**
     * 查询支付申请单
     *
     * @param paymentApplyFilter
     * @return
     */
    ListWrapper<PaymentApply> query(PaymentApplyFilter paymentApplyFilter);


    /**
     * 查看支付订单详情
     *
     * @param paymentApplyId
     * @return
     */
    PaymentApply view(Long paymentApplyId);

    /**
     * 取消支付订单
     *
     * @param paymentApplyId
     */
    void paymentApplyCancel(Long paymentApplyId) throws AlipayApiException;

}
