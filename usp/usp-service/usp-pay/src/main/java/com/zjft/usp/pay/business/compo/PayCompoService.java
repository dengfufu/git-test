package com.zjft.usp.pay.business.compo;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.pay.business.dto.AccountInfoTraceDto;
import com.zjft.usp.pay.business.enums.AccountTraceEnum;
import com.zjft.usp.pay.business.enums.CommonEnum;
import com.zjft.usp.pay.business.enums.PaymentApplyEnum;
import com.zjft.usp.pay.business.enums.TradeConfirmEnum;
import com.zjft.usp.pay.business.model.PaymentApply;
import com.zjft.usp.pay.business.model.TradeConfirm;
import com.zjft.usp.pay.business.service.AccountInfoService;
import com.zjft.usp.pay.business.service.PaymentApplyService;
import com.zjft.usp.pay.business.service.TradeConfirmService;
import com.zjft.usp.pay.business.service.feign.AnyfixFeignService;
import com.zjft.usp.pay.business.service.feign.dto.SettleDemanderOnlinePaymentDto;
import com.zjft.usp.pay.business.service.impl.PaymentApplyServiceImpl;
import com.zjft.usp.pay.business.service.AliPayService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;


/**
 * @author: CK
 * @create: 2020-05-26 14:48
 */
@Slf4j
@Service
public class PayCompoService {

    @Autowired
    AliPayService aliPayService;

    @Autowired
    PaymentApplyService paymentApplyService;

    @Autowired
    AccountInfoService accountInfoService;

    @Autowired
    TradeConfirmService tradeConfirmService;

    @Autowired
    AnyfixFeignService anyfixFeignService;

    /**
     * 付款申请: 支付宝电脑支付
     *
     * @param paymentApplyId
     * @param currentUserId
     * @return
     * @throws AlipayApiException
     */
    @Transactional
    public String payByAliPayPc(Long paymentApplyId, Long currentUserId) throws AlipayApiException {
        // 1.查询应用支付申请
        PaymentApply paymentApply = paymentApplyService.getById(paymentApplyId);
        if (ObjectUtils.isEmpty(paymentApply)) {
            log.error("应用支付申请号:{},不存在", paymentApplyId);
            throw new AppException("支付申请单不存在");
        }
        // 1.1 更新支付申请相关信息
        paymentApply.setPayFeeRate(0.006f);
        BigDecimal payFeeAmount = paymentApply.getOrderAmount()
                .multiply(new BigDecimal(0.006))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        paymentApply.setPayFeeAmount(payFeeAmount); // 支付手续费
        paymentApply.setStatus(PaymentApplyEnum.Status.in_pay.getCode()); // 更新状态
        paymentApplyService.updateById(paymentApply);

        // 2.支付宝: 电脑支付
        log.info("应用支付申请号:{},使用支付宝电脑支付,paymentApplyId");
        String form = aliPayService.aliPayPc(
                String.valueOf(paymentApply.getId()),
                paymentApply.getOrderName(),
                paymentApply.getOrderAmount().toString(),
                JSON.toJSONString(paymentApply),
                paymentApply.getExpireTime());

        // 3. 增加定时任务查询调用支付宝所需要的数据
        log.info("应用支付申请号:{},增加支付宝交易结果查询的定时任务");
        TradeConfirm tradeConfirmOld = tradeConfirmService.getOne(new QueryWrapper<TradeConfirm>().eq("trade_apply_id", paymentApply.getId()));
        if (StringUtils.isEmpty(tradeConfirmOld)) {
            log.info("创建交易号为:{}的交易确认记录", paymentApply.getId());
            TradeConfirm tradeConfirm = new TradeConfirm();
            tradeConfirm.setId(KeyUtil.getId());
            tradeConfirm.setTradeApplyId(paymentApply.getId());
            tradeConfirm.setTradeType(CommonEnum.PayWay.ali.getCode());
            tradeConfirm.setTradeStatus(PaymentApplyEnum.Status.in_pay.getCode());
            tradeConfirm.setFreq(TradeConfirmEnum.Freq.minute.getCode());
            tradeConfirmService.save(tradeConfirm);
        } else {
            log.error("已经存在交易号为:{}的交易确认记录", paymentApply.getId());
        }
        return form;
    }


    /**
     * 直接请求第三方支付结果
     *
     * @param paymentApplyId
     * @return
     * @throws AlipayApiException
     */
    public boolean directCheckPayResult(Long paymentApplyId) throws AlipayApiException {
        log.info("应用支付申请号:{},直接查询支付宝交易结果，不处理业务", paymentApplyId);
        boolean result = false;
        AlipayTradeQueryResponse response = aliPayService.aliPayQuery(String.valueOf(paymentApplyId), "");
        if (response.isSuccess()) {
            if (response.getTradeStatus().equals("TRADE_SUCCESS")) {
                log.info("支付宝: 交易支付成功");
                result = true;
            }
        }
        return result;
    }

    /**
     * 主动查询支付宝接口(定时任务)
     *
     * @param tradeConfirm
     */
    @Transactional
    public void queryJob(TradeConfirm tradeConfirm) {
        log.info("TradeConfirm:{}", JSON.toJSONString(tradeConfirm));
        // 支付宝支付
        if (tradeConfirm.getTradeType().equals(TradeConfirmEnum.TradeType.pay.getCode())) {
            int oldStatus = tradeConfirm.getTradeStatus(); // 1.获取当前数据库的状态
            int newStatus = 0; // 2.获取支付宝方的状态

            try {
                AlipayTradeQueryResponse response = aliPayService.aliPayQuery(String.valueOf(tradeConfirm.getTradeApplyId()), "");
                newStatus = this.convertTradeStatus(response);
            } catch (AlipayApiException e) {
                log.error(e.getErrMsg(), e);
                if (e.getErrCode().equals("-1")) {
                    // 交易不存在，且交易超时，清理confirm记录
                    this.checkAndCancelTrade(tradeConfirm);
                }
                return;
            }

            // 3.如果状态(转换后状态)变更，更新为最新状态，获取更新结果
            if (oldStatus != newStatus) {
                log.info("状态变化, old: {}, new: {}", oldStatus, newStatus);
                // 4.1.更新成功，confirm记录的更新(更新，删除)
                if (this.updateNewStatus(tradeConfirm, oldStatus, newStatus)) {
                    log.info("更新成功, 线程: {}", Thread.currentThread().getName());
                    if (this.tradeConfirmService.removeById(tradeConfirm.getId())) {
                        // 删除定时任务数据成功 && 新状态为支付成功
                        if (newStatus == PaymentApplyEnum.Status.pay_success.getCode()) {
                            this.updateBizInfo(tradeConfirm.getTradeApplyId(), newStatus);
                        }
                    }
                } else {
                    log.info("Thread执行结束, 已经由其他线程完成更新及后续处理");
                }
            } else {
                log.info("状态未变化: {}", newStatus);
            }
        } else {
            log.info("其他支付方式");
        }
    }

    /**
     * 处理应用中超时关闭的申请
     * <p>
     * 1. 发送支付宝关闭请求
     * 2. 更新应用中申请订单状态: 取消
     * 3. 清理定时任务数据
     *
     * @param tradeConfirm
     */
    @Transactional
    public void checkAndCancelTrade(TradeConfirm tradeConfirm) {
        PaymentApply paymentApply = paymentApplyService.getById(tradeConfirm.getTradeApplyId());
        // 1.检查是否本地超时, 需要创建时设置
        if (paymentApply.getExpireTime().compareTo(DateUtil.date()) <= 0) {
            try {
                // 1. 关闭交易
                aliPayService.aliPayClose(tradeConfirm.getTradeApplyId().toString(), "");
                // 2. 更新交易申请为cancel
                this.updateBizInfo(tradeConfirm.getTradeApplyId(), PaymentApplyEnum.Status.cancel.getCode());
                // 3. 清理confirm
                this.tradeConfirmService.removeById(tradeConfirm.getId());
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 支付成功后更新业务
     * <p>
     * 1. 判断: 如果新状态和当前状态一样则不更新业务
     * 2. 更新交易申请: 状态，完成时间，结束时间
     * 3. 更新钱包以及钱包流水
     * 4. 更新usp-anyfix中的结算单信息
     *
     * @param id
     * @param newStatus 最新状态，一般跟系统当前状态不同
     */
    @Transactional
    public boolean updateBizInfo(Long id, int newStatus) {
        log.info("updateBizInfo:newStatus:{}", newStatus);
        // 判断
        PaymentApply paymentApply = paymentApplyService.getById(id);
        if (paymentApply.getStatus() == newStatus) {
            return false;
        }

        // 1. 更新支付申请相关信息
        log.info("updateBizInfo:更新支付申请相关信息");
        UpdateWrapper<PaymentApply> wrapper = new UpdateWrapper<>();
        wrapper.set("status", newStatus).eq("id", id); // 更新交易申请状态

        if (newStatus == PaymentApplyEnum.Status.cancel.getCode()) {
            wrapper.set("cancel_reason", "超时自动关闭");
        } else if (newStatus == PaymentApplyEnum.Status.pay_success.getCode()) {
            // 1.1 对于交易成功的，更新完成时间，结束时间
            DateTime dtFinish = DateUtil.date();
            wrapper.set("finish_time", dtFinish);

            // 1.2 假设成功，M天后不允许退款
            DateTime dtEnd = DateUtil.offsetDay(dtFinish, PaymentApplyServiceImpl.REFUND_INTERVAL_DAYS);
            wrapper.set("end_time", dtEnd);

            // 2. 更新钱包，增加交易记录
            log.info("updateBizInfo:更新钱包，增加交易记录");
            AccountInfoTraceDto accountInfoTraceDTO = new AccountInfoTraceDto();
            accountInfoTraceDTO
                    .setPayerAccountId(paymentApply.getPayerAccountId())        // 付款方ID
                    .setPayeeAccountId(paymentApply.getPayeeAccountId())        // 收款方ID
                    .setAmount(paymentApply.getOrderAmount())                   // 发生金额
                    .setApplyId(paymentApply.getId())                           // 支付申请ID
                    .setApplyName(paymentApply.getOrderName())                  // 申请单名称：支付申请商品订单名称
                    .setApplySource(AccountTraceEnum.ApplySource.pay.getCode()) // 申请来源
                    .setPayFeeAmount(paymentApply.getPayFeeAmount())            // 第三方支付: 支付宝支付手续费
                    .setPlatFeeAmount(paymentApply.getPlatFeeAmount());         // 平台手续费
            accountInfoService.walletTradeByThirdPay(accountInfoTraceDTO);

            // 3. 更新结算单信息
            log.info("updateBizInfo:更新结算单信息");
            SettleDemanderOnlinePaymentDto settleDemanderOnlinePaymentDto = new SettleDemanderOnlinePaymentDto();
            settleDemanderOnlinePaymentDto.setSettleId(paymentApply.getOrderId());
            settleDemanderOnlinePaymentDto.setPayOperator(paymentApply.getApplyUser());
            settleDemanderOnlinePaymentDto.setPayOperateTime(DateUtil.date());
            Result result = anyfixFeignService.payOnline(settleDemanderOnlinePaymentDto);
            log.info("{}", result);
            if (result == null || result.getCode() == Result.FAIL) {
                log.error("更新结算单失败");
                throw new RuntimeException("更新结算单失败");
            }

        } else if (newStatus == PaymentApplyEnum.Status.in_pay.getCode()) {
            log.info("支付中...");
        }
        paymentApplyService.update(wrapper.eq("id", id));

        return true;
    }

    /**
     * 支付宝订单状态转换为应用状态
     *
     * @param response
     * @return 空字符串表示查询失败，其他返回实际查询结果
     */
    private int convertTradeStatus(AlipayTradeQueryResponse response) throws AlipayApiException {
        if (response.isSuccess()) {
            // 请求成功
            log.info("支付宝交易查询成功，支付宝交易号: {}", response.getTradeNo());
            if (response.getTradeStatus().equals("WAIT_BUYER_PAY")) {
                log.info("支付宝: 交易创建，等待买家付款");
                return PaymentApplyEnum.Status.in_pay.getCode();
            } else if (response.getTradeStatus().equals("TRADE_CLOSED")) {
                log.info("支付宝: 未付款交易超时关闭，或支付完成后全额退款");
                return PaymentApplyEnum.Status.cancel.getCode();
            } else if (response.getTradeStatus().equals("TRADE_FINISHED")) {
                log.info("支付宝: 交易结束，不可退款");
                return PaymentApplyEnum.Status.pay_success.getCode();
            } else if (response.getTradeStatus().equals("TRADE_SUCCESS")) {
                log.info("支付宝: 交易支付成功");
                return PaymentApplyEnum.Status.pay_success.getCode();
            }
        } else {
            if (response.getSubCode().equals("ACQ.TRADE_NOT_EXIST")) {
                log.info("支付宝: 交易不存在");
                throw new AlipayApiException("-1", "ACQ.TRADE_NOT_EXIST");
            }
        }
        throw new AlipayApiException("query failed");
    }

    /**
     * 更新旧状态为新状态，更新成功返回true
     *
     * @param tradeConfirm
     * @param oldStatus
     * @param newStatus
     * @return
     */
    @Transactional
    boolean updateNewStatus(TradeConfirm tradeConfirm, int oldStatus, int newStatus) {
        return this.tradeConfirmService.update(new UpdateWrapper<TradeConfirm>()
                .set("trade_status", newStatus)
                .eq("trade_status", oldStatus)
                .eq("id", tradeConfirm.getId())
        );
    }
}
