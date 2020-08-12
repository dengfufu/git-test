package com.zjft.usp.pay.business.controller;


import com.alipay.api.AlipayApiException;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.pay.business.model.RefundApply;
import com.zjft.usp.pay.business.service.RefundApplyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 退款申请表  前端控制器
 * </p>
 *
 * @author CK
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/refund-apply")
public class RefundApplyController {

    @Autowired
    RefundApplyService refundApplyService;

    @ApiOperation("退款申请创建")
    @PostMapping(value = "/add")
    public Result refundApplyAdd(@RequestBody Map<String, Object> param, @LoginUser UserInfo userInfo) {
        Long payId = (Long) param.get("payId"); // 原支付订单
        BigDecimal refundAmount = (BigDecimal) param.get("refundAmount"); // 退款金额
        String applyReason = (String) param.get("applyReason"); // 退款原因
        RefundApply refundApply = new RefundApply();
        refundApply.setPayId(payId);
        refundApply.setRefundAmount(refundAmount);
        refundApply.setApplyReason(applyReason);
        refundApplyService.refundApplyAdd(refundApply, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("退款申请审批")
    @PostMapping(value = "/approve")
    public Result refundApplyApprove(@RequestBody Map<String, Object> param, @LoginUser UserInfo userInfo) throws AlipayApiException {
        Long id = (Long) param.get("id"); // 退款申请id
        Integer approveResult = (Integer) param.get("approveResult"); // 退款原因
        String approveNote = (String) param.get("approveNote"); // 退款原因
        RefundApply refundApply = new RefundApply();
        refundApply.setId(id);
        refundApply.setApproveResult(approveResult);
        refundApply.setApproveNote(approveNote);
        refundApplyService.refundApplyApprove(refundApply, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("退款申请查看详情")
    @PostMapping(value = "/view")
    public Result refundApplyView(@RequestBody Map<String, Object> param) {
        return Result.succeed();
    }

}
