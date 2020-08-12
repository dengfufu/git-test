package com.zjft.usp.pay.business.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.zjft.usp.pay.config.AliPayConfig;
import com.zjft.usp.pay.business.service.AliPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author: CK
 * @create: 2020-05-19 15:36
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AliPayServiceImpl implements AliPayService {

    @Autowired
    AlipayClient alipayClient;

    @Autowired
    private AliPayConfig aliPayConfig;

    @Value("${usp.pay.alipay.returnUrl}")
    private String returnUrl;

    @Value("${usp.pay.alipay.notifyUrl}")
    private String notifyUrl;

    public static final boolean NEED_ENCRIPT = true;
    @Override
    public String aliPayRefundQuery(String outTradeNo, String tradeNo, String outRequestNo) throws AlipayApiException {
        log.info("应用订单号：" + outTradeNo + "支付宝退款查询");
        AlipayTradeFastpayRefundQueryRequest aliPayRequest = new AlipayTradeFastpayRefundQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("trade_no", tradeNo);
        bizContent.put("out_request_no", outRequestNo);
        aliPayRequest.setBizContent(bizContent.toString());
        aliPayRequest.setNeedEncrypt(NEED_ENCRIPT);
        String result = alipayClient.certificateExecute(aliPayRequest).getBody();
        return result;
    }

    @Override
    public String aliPayRefund(String outTradeNo,
                               String tradeNo,
                               String outRequestNo,
                               String refundAmount,
                               String refundReason) throws AlipayApiException {
        log.info("应用订单号：" + outTradeNo + "支付宝退款");
        // 创建退款请求builder，设置请求参数
        AlipayTradeRefundRequest refundRequest = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo); // 紫金内部支付申请号
        bizContent.put("trade_no", tradeNo); // 支付宝交易号,out_trade_no 和 trade_no 2选1必填
        bizContent.put("out_request_no", outRequestNo); // 支付宝交易号,out_trade_no 和 trade_no 2选1必填
        bizContent.put("refund_amount", refundAmount); //需要退款的金额,必填
        bizContent.put("refund_reason", refundReason); //退款的原因说明，可选
        refundRequest.setBizContent(bizContent.toString());
        refundRequest.setNeedEncrypt(NEED_ENCRIPT);
        String result = alipayClient.certificateExecute(refundRequest).getBody();
        return result;
    }

    @Override
    public AlipayTradeQueryResponse aliPayQuery(String outTradeNo, String tradeNo) throws AlipayApiException {
        log.info("应用订单号：" + outTradeNo + "支付宝交易查询");
        AlipayTradeQueryRequest aliPayRequest = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("trade_no", tradeNo);
        aliPayRequest.setBizContent(bizContent.toString());
        aliPayRequest.setNeedEncrypt(NEED_ENCRIPT);
        AlipayTradeQueryResponse response = alipayClient.certificateExecute(aliPayRequest);
        log.info("查询结果: {}", response);
        return response;
    }

    @Override
    public AlipayTradeCloseResponse aliPayClose(String outTradeNo, String tradeNo) throws AlipayApiException {
        log.info("应用订单号：" + outTradeNo + "支付宝交易关闭");
        AlipayTradeCloseRequest aliPayCloseRequest = new AlipayTradeCloseRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("trade_no", tradeNo);
        aliPayCloseRequest.setBizContent(bizContent.toString());
        aliPayCloseRequest.setNeedEncrypt(NEED_ENCRIPT);
        AlipayTradeCloseResponse response = alipayClient.certificateExecute(aliPayCloseRequest);
        return response;
    }

    @Override
    public String aliPayPc(String outTradeNo,
                           String subject,
                           String totalAmount,
                           String body,
                           Date timeExpire) throws AlipayApiException {
        log.info("支付宝PC支付下单");
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);//前台通知
        alipayRequest.setNotifyUrl(notifyUrl);//后台回调
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);// 商户订单号，商户网站订单系统中唯一订单号，必填
        bizContent.put("subject", subject);// 订单名称，必填
        bizContent.put("total_amount", totalAmount);//订单金额:元,必填
        bizContent.put("body", body);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");//电脑网站支付
        bizContent.put("time_expire", DateUtil.format(timeExpire, "yyyy-MM-dd HH:mm:ss"));//电脑网站支付
        /**
         * 这里有三种模式可供选择
         * 如果在系统内支付，并且是弹出层支付，建议选择模式二、其他模式会跳出当前iframe(亲测有效)
         */
        bizContent.put("qr_pay_mode", "2");
        String biz = bizContent.toString().replaceAll("\"", "'");
        alipayRequest.setBizContent(biz);
        alipayRequest.setNeedEncrypt(NEED_ENCRIPT);
        log.info("业务参数:" + alipayRequest.getBizContent());
        String form = alipayClient.pageExecute(alipayRequest).getBody();
        return form;
    }

    @Override
    public boolean rsaCheckV1(Map<String, String> params) {
        try {
            log.info("AliPayCertPath: {},charset: {},signType: {},",
                    aliPayConfig.getAlipayCertPath(),
                    aliPayConfig.getCharset(),
                    aliPayConfig.getSignType());
            boolean verifyResult = AlipaySignature.certVerifyV1(
                    params,
                    aliPayConfig.getAlipayCertPath(),
                    aliPayConfig.getCharset(),
                    aliPayConfig.getSignType());
            return verifyResult;
        } catch (AlipayApiException e) {
            log.debug("verify sigin error, exception is:{}", e);
            return false;
        }
    }
}
