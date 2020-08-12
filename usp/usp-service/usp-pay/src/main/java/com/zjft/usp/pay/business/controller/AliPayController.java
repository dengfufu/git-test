package com.zjft.usp.pay.business.controller;

import com.alipay.api.AlipayApiException;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.pay.business.compo.PayCompoService;
import com.zjft.usp.pay.business.enums.PaymentApplyEnum;
import com.zjft.usp.pay.business.service.AliPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author: CK
 * @create: 2020-05-19 15:37
 */
@Slf4j
@Api(tags = "支付宝支付")
@RestController
@RequestMapping("/alipay")
public class AliPayController {

    @Autowired
    AliPayService aliPayService;

    @Autowired
    PayCompoService payCompoService;

    @ApiOperation(value = "电脑支付")
    @RequestMapping(value = "/pc-pay", method = RequestMethod.POST)
    public Result<String> pcPay(@RequestBody Map<String, String> param, @LoginUser UserInfo userInfo) throws AlipayApiException {
        Long paymentApplyId = Long.valueOf(param.get("paymentApplyId")); // 支付申请单id
        String form = payCompoService.payByAliPayPc(paymentApplyId, userInfo.getUserId());
        return Result.succeed(form, "支付宝电脑支付");
    }

    @ApiOperation(value = "查询支付异步结果")
    @RequestMapping(value = "/result-test", method = RequestMethod.POST)
    public Result<String> aliPayResultQuery(@RequestBody Map<String, String> param, @LoginUser UserInfo userInfo) throws AlipayApiException {
        Long paymentApplyId = Long.valueOf(param.get("paymentApplyId")); // 支付申请单id
//        payCompoService.payByAliPayQuery(Long.valueOf(paymentApplyId));
        return Result.succeed();
    }

    /**
     * 支付异步通知
     * 接收到异步通知并验签通过后，一定要检查通知内容，
     * 包括通知中的app_id、out_trade_no、total_amount是否与请求中的一致，并根据trade_status进行后续业务处理。
     *
     * @param request
     * @throws Exception
     */
    @ApiOperation(value = "支付宝支付回调(二维码、H5、网站)")
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public String aliPayNotify(HttpServletRequest request) throws Exception {
        log.info("/****aliPay notify****/");
        // 取出所有参数是为了验证签名
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
//            valueStr = new String(valueStr.getBytes("ß-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        log.info("param:{}", params);
        boolean signVerified = aliPayService.rsaCheckV1(params); // 验证签名
        if (signVerified) {
            log.info("支付宝验证签名成功！");
            String outTradeNo = params.get("out_trade_no"); //应用订单号
            String tradeNo = params.get("trade_no"); //支付宝交易号
            String tradeStatus = params.get("trade_status");//交易状态
            if (tradeStatus.equals("WAIT_BUYER_PAY")) { // 如果状态是正在等待用户付款
                log.info("应用订单号:" + outTradeNo + ":,交易创建,正在等待买家付款");
            } else if (tradeStatus.equals("TRADE_CLOSED")) {
                log.info("应用订单号:" + outTradeNo + "订单的状态已经关闭,未付款交易超时关闭，或支付完成后全额退款");
            } else if (tradeStatus.equals("TRADE_SUCCESS")) {
                log.info("应用订单号:" + outTradeNo + ",交易支付成功");
                // 根据结果，增加企业钱包交易记录
                boolean result = payCompoService.updateBizInfo(Long.valueOf(outTradeNo), PaymentApplyEnum.Status.pay_success.getCode());
                if (result) {
                    // 做其他后续处理
                } else {
                    log.debug("支付申请已处理，可能由主动query线程处理");
                }
            } else if (tradeStatus.equals("TRADE_FINISHED")) {
                log.info(outTradeNo + "交易结束，不可退款");
            }
            return "success";
        } else {
            log.info("支付宝验证签名失败！");
            return "invalid signature";
        }
    }

}
