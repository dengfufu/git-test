package com.zjft.usp.pay.business.controller;

import com.alipay.api.AlipayApiException;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.pay.business.compo.PayCompoService;
import com.zjft.usp.pay.business.dto.PaymentApplyDemanderDto;
import com.zjft.usp.pay.business.filter.PaymentApplyFilter;
import com.zjft.usp.pay.business.model.PaymentApply;
import com.zjft.usp.pay.business.service.PaymentApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 支付申请表  前端控制器
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/payment-apply")
public class PaymentApplyController {

    @Autowired
    PaymentApplyService paymentApplyService;

    @Autowired
    PayCompoService payCompoService;


    @PostMapping(value = "/query")
    public Result<ListWrapper<PaymentApply>> query(@RequestBody PaymentApplyFilter paymentApplyFilter) {
        ListWrapper<PaymentApply> listWrapper = this.paymentApplyService.query(paymentApplyFilter);
        return Result.succeed(listWrapper);
    }

    /**
     * 委托商结算-创建在线支付申请
     *
     * @param paymentApplyDemanderDTO
     * @return
     */
    @PostMapping(value = "/demander-create")
    public Result<PaymentApply> demanderPaymentApplyCreate(@RequestBody PaymentApplyDemanderDto paymentApplyDemanderDTO, @LoginUser UserInfo userInfo) {
        PaymentApply paymentApply = paymentApplyService.demanderPaymentApplyAdd(paymentApplyDemanderDTO, userInfo.getUserId());
        return Result.succeed(paymentApply);
    }

    /**
     * 个人结算-创建在线支付申请
     *
     * @param userInfo
     * @return
     */
    @PostMapping(value = "/custom-create")
    public Result customPaymentApplyCreate(@LoginUser UserInfo userInfo) {
        return Result.succeed();
    }

    /**
     * 取消订单
     *
     * @return
     */
    @PostMapping(value = "/cancel")
    public Result paymentApplyCancel(@RequestBody Map<String, String> param) throws AlipayApiException {
        Long paymentApplyId = Long.valueOf(param.get("paymentApplyId"));
        paymentApplyService.paymentApplyCancel(paymentApplyId);
        return Result.succeed("付款申请取消成功");
    }

    /**
     * 查看订单
     *
     * @param param
     * @return
     * @throws AlipayApiException
     */
    @PostMapping(value = "/view")
    public Result<PaymentApply> viewDetail(@RequestBody Map<String, Long> param) {
        Long paymentApplyId = param.get("paymentApplyId");
        PaymentApply paymentApply = paymentApplyService.view(paymentApplyId);
        return Result.succeed(paymentApply);
    }

    /**
     * 直接请求第三方平台返回支付结果
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/direct-check-result")
    public Result<Boolean> directCheckResult(@RequestBody Map<String, String> param) throws AlipayApiException {
        Long paymentApplyId = Long.valueOf(param.get("paymentApplyId"));
        boolean result = payCompoService.directCheckPayResult(paymentApplyId);
        return Result.succeed(result, "第三方平台返回支付结果");
    }
}
