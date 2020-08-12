package com.zjft.usp.pay.business.service;

import com.alipay.api.AlipayApiException;
import com.zjft.usp.pay.business.model.RefundApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 退款申请表  服务类
 * </p>
 *
 * @author CK
 * @since 2020-05-27
 */
public interface RefundApplyService extends IService<RefundApply> {

    /**
     * 创建退款申请
     *
     * @param refundApply
     * @param currentUserId
     * @return
     */
    void refundApplyAdd(RefundApply refundApply, Long currentUserId);

    /**
     * 退款申请审批
     *
     * @param refundApply
     * @param currentUserId
     */
    void refundApplyApprove(RefundApply refundApply, Long currentUserId) throws AlipayApiException;
}
