package com.zjft.usp.pay.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.feign.service.UserFeignService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.pay.business.dto.PaymentApplyDemanderDto;
import com.zjft.usp.pay.business.filter.PaymentApplyFilter;
import com.zjft.usp.pay.business.enums.CommonEnum;
import com.zjft.usp.pay.business.enums.PaymentApplyEnum;
import com.zjft.usp.pay.business.model.AccountInfo;
import com.zjft.usp.pay.business.model.PaymentApply;
import com.zjft.usp.pay.business.mapper.PaymentApplyMapper;
import com.zjft.usp.pay.business.service.AccountInfoService;
import com.zjft.usp.pay.business.service.PaymentApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.pay.business.service.AliPayService;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 支付申请表  服务实现类
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PaymentApplyServiceImpl extends ServiceImpl<PaymentApplyMapper, PaymentApply> implements PaymentApplyService {

    public static final int REFUND_INTERVAL_DAYS = 3;

    public static final int TRADE_TIMEOUT_MIN = 15;

    @Autowired
    AliPayService aliPayService;

    @Autowired
    AccountInfoService accountInfoService;

    @Autowired
    UserFeignService userFeignService;

    @Override
    public PaymentApply demanderPaymentApplyAdd(PaymentApplyDemanderDto paymentApplyDemanderDTO, Long currentUserId) {
        // 1. 判断是否开通在线支付功能
        AccountInfo payerAccountInfo = this.accountInfoService.corpViewWallet(paymentApplyDemanderDTO.getPayerCorpId());
        AccountInfo payeeAccountInfo = this.accountInfoService.corpViewWallet(paymentApplyDemanderDTO.getPayeeCorpId());
        if (payerAccountInfo == null) {
            log.error("付款方未开通在线支付功能");
            throw new AppException("付款方未开通在线支付功能");
        }
        if (payeeAccountInfo == null) {
            log.error("收款方未开通在线支付功能");
            throw new AppException("收款方未开通在线支付功能");
        }
        // 2. 判断结算订单是否已经支付过
        PaymentApply paymentApplyOld = this.getOne(new QueryWrapper<PaymentApply>()
                .eq("order_id", paymentApplyDemanderDTO.getOrderId())
                .eq("status", PaymentApplyEnum.Status.pay_success.getCode()));
        if (paymentApplyOld != null) {
            log.error("该结算订单已经完成支付");
            throw new AppException("该结算订单已经完成支付");
        }

        // 3. 判断是否已经存在【未支付订单】
        QueryWrapper<PaymentApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", paymentApplyDemanderDTO.getOrderId());
        queryWrapper.in("status", Arrays.asList(
                PaymentApplyEnum.Status.create.getCode(), // 订单创建
                PaymentApplyEnum.Status.in_pay.getCode(), // 付款中
                PaymentApplyEnum.Status.pay_fail.getCode()) // 支付失败
        );
        Date currentDate = DateUtil.date();
        queryWrapper.gt("expire_time", currentDate);
        PaymentApply paymentApply = this.getOne(queryWrapper);
        if (ObjectUtils.isEmpty(paymentApply)) {
            log.info("结算订单号:{},不存在支付申请", paymentApplyDemanderDTO.getOrderId());
            paymentApply = new PaymentApply();
            paymentApply.setId(KeyUtil.getId());

            paymentApply.setPayeeAccountId(payeeAccountInfo.getId());
            paymentApply.setPayerAccountId(payerAccountInfo.getId());
            paymentApply.setOrderId(paymentApplyDemanderDTO.getOrderId());
            paymentApply.setOrderName(paymentApplyDemanderDTO.getOrderName());
            paymentApply.setOrderAmount(paymentApplyDemanderDTO.getOrderAmount());

            paymentApply.setOrderType(CommonEnum.OrderType.consume.getCode()); // 付款
            paymentApply.setOrderSource(CommonEnum.OrderSource.demander.getCode()); // 委托商结算单
            paymentApply.setPayWay(CommonEnum.PayWay.ali.getCode()); // 支付方式:支付宝
            paymentApply.setChannelType(PaymentApplyEnum.ChannelType.ali_pc.getCode()); // 电脑支付
            paymentApply.setStatus(PaymentApplyEnum.Status.create.getCode());
            paymentApply.setApplyUser(currentUserId);
            DateTime applyTime = DateUtil.date();
            paymentApply.setApplyTime(applyTime);
            Date expireTime = DateUtil.offsetMinute(applyTime, TRADE_TIMEOUT_MIN);
            paymentApply.setExpireTime(expireTime);
            log.info("创建应用支付申请,申请id:{}", paymentApply.getId());
            this.save(paymentApply);

        } else {
            log.info("结算订单号:{},已经存在未完成的支付申请", paymentApplyDemanderDTO.getOrderId());
        }
        paymentApply.setPayeeCorpName(payeeAccountInfo.getCorpName());
        paymentApply.setPayerCorpName(payerAccountInfo.getCorpName());
        return paymentApply;
    }

    @Override
    public ListWrapper<PaymentApply> query(PaymentApplyFilter paymentApplyFilter) {
        if (paymentApplyFilter.getCorpId() == null) {
            throw new AppException("缺少参数corpId");
        }
        AccountInfo accountInfo = this.accountInfoService.corpViewWallet(paymentApplyFilter.getCorpId());
        if (accountInfo == null) {
            throw new AppException("该企业未开通企业钱包");
        }
        ListWrapper<PaymentApply> listWrapper = new ListWrapper<>();
        QueryWrapper<PaymentApply> queryWrapper = new QueryWrapper<>();

        queryWrapper.nested(i -> i.eq("payer_account_id", accountInfo.getId()) // 付款人
                .or(m -> m.eq("payee_account_id", accountInfo.getId())
                        .eq("status", PaymentApplyEnum.Status.pay_success.getCode())));  // 收款人并且支付成功

        if (!StringUtils.isEmpty(paymentApplyFilter.getOrderId())) {
            queryWrapper.eq("order_id", paymentApplyFilter.getOrderId());
        }
        if (!StringUtils.isEmpty(paymentApplyFilter.getOrderName())) {
            queryWrapper.like("order_name", paymentApplyFilter.getOrderName());
        }
        if (!StringUtils.isEmpty(paymentApplyFilter.getStatus())) {
            queryWrapper.eq("status", paymentApplyFilter.getStatus());
        }
        if (!StringUtils.isEmpty(paymentApplyFilter.getStartApplyTime())) {
            queryWrapper.ge("apply_time", paymentApplyFilter.getStartApplyTime());
        }
        if (!StringUtils.isEmpty(paymentApplyFilter.getEndApplyTime())) {
            queryWrapper.le("apply_time", paymentApplyFilter.getEndApplyTime());
        }
        queryWrapper.orderByDesc("apply_time");
        Page page = new Page(paymentApplyFilter.getPageNum(), paymentApplyFilter.getPageSize());
        IPage<PaymentApply> paymentApplyIPage = this.page(page, queryWrapper);
        List<PaymentApply> paymentApplyList = paymentApplyIPage.getRecords();

        if (CollectionUtil.isNotEmpty(paymentApplyList)) {
            Map<Long, AccountInfo> accountIdAndAccountInfoMap = accountInfoService.accountIdAndAccountInfoMap();
            paymentApplyList.forEach((PaymentApply paymentApply) -> {
                paymentApply.setPayWayName(CommonEnum.PayWay.getNameByCode(paymentApply.getPayWay()));
                paymentApply.setStatusName(PaymentApplyEnum.Status.getNameByCode(paymentApply.getStatus()));
                paymentApply.setOrderTypeName(CommonEnum.OrderType.getNameByCode(paymentApply.getOrderType()));
                paymentApply.setOrderSourceName(CommonEnum.OrderSource.getNameByCode(paymentApply.getOrderSource()));
                AccountInfo payerAccountInfo = accountIdAndAccountInfoMap.get(paymentApply.getPayerAccountId());
                AccountInfo payeeAccountInfo = accountIdAndAccountInfoMap.get(paymentApply.getPayeeAccountId());
                paymentApply.setPayerCorpId(payerAccountInfo.getCorpId());
                paymentApply.setPayeeCorpId(payeeAccountInfo.getCorpId());
                paymentApply.setPayerCorpName(payerAccountInfo.getCorpName());
                paymentApply.setPayeeCorpName(payeeAccountInfo.getCorpName());
                // 防止定时任务未处理
                Date currentDate = DateUtil.date();
                if (paymentApply.getExpireTime().compareTo(currentDate) < 0 &&
                        (paymentApply.getStatus() == PaymentApplyEnum.Status.create.getCode() ||
                                paymentApply.getStatus() == PaymentApplyEnum.Status.in_pay.getCode())) {
                    paymentApply.setStatus(PaymentApplyEnum.Status.cancel.getCode());
                    paymentApply.setStatusName(PaymentApplyEnum.Status.getNameByCode(paymentApply.getStatus()));
                    paymentApply.setCancelReason("超时自动关闭");
                }
            });
            listWrapper.setList(paymentApplyList);
        }
        listWrapper.setTotal(paymentApplyIPage.getTotal());
        return listWrapper;

    }

    @Override
    public PaymentApply view(Long paymentApplyId) {
        PaymentApply paymentApply = this.getById(paymentApplyId);
        if (!StringUtils.isEmpty(paymentApply)) {
            Map<Long, AccountInfo> accountIdAndAccountInfoMap = accountInfoService.accountIdAndAccountInfoMap();
            paymentApply.setPayWayName(CommonEnum.PayWay.getNameByCode(paymentApply.getPayWay()));
            paymentApply.setStatusName(PaymentApplyEnum.Status.getNameByCode(paymentApply.getStatus()));
            paymentApply.setOrderTypeName(CommonEnum.OrderType.getNameByCode(paymentApply.getOrderType()));
            paymentApply.setOrderSourceName(CommonEnum.OrderSource.getNameByCode(paymentApply.getOrderSource()));
            AccountInfo payerAccountInfo = accountIdAndAccountInfoMap.get(paymentApply.getPayerAccountId());
            AccountInfo payeeAccountInfo = accountIdAndAccountInfoMap.get(paymentApply.getPayeeAccountId());
            paymentApply.setPayerCorpId(payerAccountInfo.getCorpId());
            paymentApply.setPayeeCorpId(payeeAccountInfo.getCorpId());
            paymentApply.setPayerCorpName(payerAccountInfo.getCorpName());
            paymentApply.setPayeeCorpName(payeeAccountInfo.getCorpName());
            // 防止定时任务未处理
            Date currentDate = DateUtil.date();
            if (paymentApply.getExpireTime().compareTo(currentDate) < 0 &&
                    (paymentApply.getStatus() == PaymentApplyEnum.Status.create.getCode() ||
                            paymentApply.getStatus() == PaymentApplyEnum.Status.in_pay.getCode())) {
                paymentApply.setStatus(PaymentApplyEnum.Status.cancel.getCode());
                paymentApply.setStatusName(PaymentApplyEnum.Status.getNameByCode(paymentApply.getStatus()));
                paymentApply.setCancelReason("超时自动关闭");
            }
        }
        return paymentApply;
    }


    @Override
    public void paymentApplyCancel(Long paymentApplyId) throws AlipayApiException {
        PaymentApply paymentApplyOld = this.getById(paymentApplyId);
        paymentApplyOld.setStatus(PaymentApplyEnum.Status.cancel.getCode());
        paymentApplyOld.setCancelReason("手工取消");
        this.updateById(paymentApplyOld);
        // 支付方式: 支付宝
        if (paymentApplyOld.getPayWay().equals(CommonEnum.PayWay.ali.getCode())) {
            AlipayTradeCloseResponse response = aliPayService.aliPayClose(String.valueOf(paymentApplyOld.getId()), "");
            if (response.isSuccess()) {
                log.info("应用支付申请号:{},支付宝交易关闭接口调用成功", paymentApplyOld.getId());
                String outTradeNo = response.getOutTradeNo();
                String tradeNo = response.getTradeNo();
                log.info("应用支付申请号:{},支付宝交易号:{}", outTradeNo, tradeNo);
            } else {
                log.error("应用支付申请号:{},支付宝交易关闭接口调用失败", paymentApplyOld.getId());
                if (response.getSubCode().equals("ACQ.SYSTEM_ERROR")) {
                    log.error("系统异常,需要重新发起请求");
                }
                if (response.getSubCode().equals("ACQ.TRADE_NOT_EXIST")) {
                    log.error("交易不存在,检查传入的交易号和外部订单号是否正确，修改后再重新发起");
                }
                if (response.getSubCode().equals("交易状态不合法")) {
                    log.error("交易状态不合法,检查当前交易的状态是不是等待买家付款，只有等待买家付款状态下才能发起交易关闭");
                }
                if (response.getSubCode().equals("ACQ.INVALID_PARAMETER")) {
                    log.error("参数无效,检查请求参数，修改后重新发起请求");
                }
            }
        }
    }
}
