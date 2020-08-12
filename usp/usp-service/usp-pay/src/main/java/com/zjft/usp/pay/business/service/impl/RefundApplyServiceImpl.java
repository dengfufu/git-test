package com.zjft.usp.pay.business.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alipay.api.AlipayApiException;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.pay.business.enums.CommonEnum;
import com.zjft.usp.pay.business.enums.RefundApplyEnum;
import com.zjft.usp.pay.business.model.PaymentApply;
import com.zjft.usp.pay.business.model.RefundApply;
import com.zjft.usp.pay.business.mapper.RefundApplyMapper;
import com.zjft.usp.pay.business.service.PaymentApplyService;
import com.zjft.usp.pay.business.service.RefundApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.pay.business.service.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 退款申请表  服务实现类
 * </p>
 *
 * @author CK
 * @since 2020-05-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RefundApplyServiceImpl extends ServiceImpl<RefundApplyMapper, RefundApply> implements RefundApplyService {

    @Autowired
    PaymentApplyService paymentApplyService;

    @Autowired
    AliPayService aliPayService;

    @Override
    public void refundApplyAdd(RefundApply refundApply, Long currentUserId) {
        RefundApply refundApplyNew = new RefundApply();
        refundApplyNew.setId(KeyUtil.getId());
        refundApplyNew.setPayId(refundApply.getPayId());
        refundApplyNew.setApplyReason(refundApply.getApplyReason());
        refundApplyNew.setApplyUser(currentUserId);
        refundApplyNew.setApplyTime(DateUtil.date());
        refundApply.setStatus(RefundApplyEnum.Status.create.getCode()); // 退款申请状态
        // 查询原订单的支付方式
        PaymentApply paymentApply = paymentApplyService.getById(refundApply.getPayId());
        paymentApply.setPayWay(paymentApply.getPayWay());
        this.save(refundApplyNew);
    }

    @Override
    public void refundApplyApprove(RefundApply refundApply, Long currentUserId) throws AlipayApiException {
        RefundApply refundApplyOld = this.getById(refundApply.getId());
        // 判断是否是新建状态 TODO

        // 审批信息
        refundApplyOld.setApproveResult(refundApply.getApproveResult());
        refundApplyOld.setApproveNote(refundApply.getApproveNote());
        refundApplyOld.setApproveUser(currentUserId);
        refundApplyOld.setApproveTime(DateUtil.date());

        // 审批结果同意
        if (refundApply.getApproveResult().equals(RefundApplyEnum.ApproveResult.agree.getCode())) {
            refundApplyOld.setStatus(RefundApplyEnum.Status.approve_success.getCode()); // 审核通过
            // 支付宝退款
            if (refundApplyOld.getPayId().equals(CommonEnum.PayWay.ali.getCode())) {
                aliPayService.aliPayRefund(
                        String.valueOf(refundApplyOld.getPayId()),
                        String.valueOf(refundApplyOld.getRefundAmount()),
                        String.valueOf(refundApplyOld.getId()),
                        refundApplyOld.getRefundAmount().toString(),
                        refundApply.getApplyReason());
            }
        } else {
            refundApplyOld.setStatus(RefundApplyEnum.Status.approve_fail.getCode()); // 审核不通过
        }
        this.updateById(refundApplyOld);
    }
}
