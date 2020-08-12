package com.zjft.usp.pay.business.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;

import java.util.Date;
import java.util.Map;

/**
 * @author: CK
 * @create: 2020-05-19 15:36
 */
public interface AliPayService {

    /**
     * 阿里支付退款申请查询
     *
     * @param outTradeNo:   应用交易申请号
     * @param tradeNo:      支付宝交易号
     * @param outRequestNo: 退款申请号
     * @return
     */
    String aliPayRefundQuery(String outTradeNo, String tradeNo, String outRequestNo) throws AlipayApiException;

    /**
     * 阿里支付退款
     *
     * @param outTradeNo:   应用交易申请号
     * @param tradeNo:      支付宝交易号
     * @param outRequestNo: 退款申请号
     * @param refundAmount: 退款金额
     * @param refundReason: 退款理由
     * @return
     * @throws AlipayApiException
     */
    String aliPayRefund(String outTradeNo, String tradeNo, String outRequestNo, String refundAmount, String refundReason) throws AlipayApiException;


    /**
     * 交易查询
     *
     * @param outTradeNo: 应用交易申请号
     * @param tradeNo:    支付宝交易号
     * @return
     */
    AlipayTradeQueryResponse aliPayQuery(String outTradeNo, String tradeNo) throws AlipayApiException;


    /**
     * 关闭交易
     *
     * @param outTradeNo: 应用交易申请号
     * @param tradeNo:    支付宝交易号
     * @return
     * @throws AlipayApiException
     */
    AlipayTradeCloseResponse aliPayClose(String outTradeNo, String tradeNo) throws AlipayApiException;


    /**
     * 网站支付
     *
     * @param outTradeNo:  应用交易申请号
     * @param subject:     订单标题
     * @param totalAmount: 订单总金额
     * @param body:        订单描述
     * @param timeExpire:  过期时间
     * @return
     * @throws AlipayApiException
     */
    String aliPayPc(String outTradeNo, String subject, String totalAmount, String body, Date timeExpire) throws AlipayApiException;

    /**
     * 校验签名
     *
     * @param params
     * @return
     */
    boolean rsaCheckV1(Map<String, String> params);
}
