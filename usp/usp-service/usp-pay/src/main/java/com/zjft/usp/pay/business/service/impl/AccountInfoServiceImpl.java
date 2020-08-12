package com.zjft.usp.pay.business.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.pay.business.dto.AccountInfoTraceDto;
import com.zjft.usp.pay.business.enums.AccountInfoEnum;
import com.zjft.usp.pay.business.enums.AccountTraceEnum;
import com.zjft.usp.pay.business.enums.CommonEnum;
import com.zjft.usp.pay.business.service.feign.UasFeignService;
import com.zjft.usp.pay.business.service.feign.dto.CorpDto;
import com.zjft.usp.pay.business.service.feign.dto.CorpRegistry;
import com.zjft.usp.pay.business.model.AccountInfo;
import com.zjft.usp.pay.business.mapper.AccountInfoMapper;
import com.zjft.usp.pay.business.model.AccountTrace;
import com.zjft.usp.pay.business.service.AccountInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.pay.business.service.AccountTraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 账户表 服务实现类
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoMapper, AccountInfo> implements AccountInfoService {

    @Autowired
    AccountTraceService accountTraceService;

    @Autowired
    UasFeignService uasFeignService;

    @Override
    public Long corpInitWallet(Long corpId) {
        Assert.notNull(corpId, "corpId 不能为 NULL");
        List<AccountInfo> accountInfoList = this.list(new QueryWrapper<AccountInfo>().eq("corp_id", corpId));
        if (accountInfoList.size() > 0) {
            throw new AppException("企业:" + corpId + ",已经开通钱包");
        }
        // 创建钱包信息
        log.info("企业号:{},创建钱包", corpId);
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setId(KeyUtil.getId());
        accountInfo.setCorpId(corpId);
        accountInfo.setAccountType(AccountInfoEnum.AccountType.enterprise.getCode());
        accountInfo.setStatus(AccountInfoEnum.Status.active.getCode());
        accountInfo.setCreateTime(DateUtil.date());
        this.save(accountInfo);
        return accountInfo.getId();
    }

    @Override
    public AccountInfo corpViewWallet(Long corpId) {
        Assert.notNull(corpId, "corpId 不能为 NULL");
        AccountInfo accountInfo = this.getOne(new QueryWrapper<AccountInfo>().eq("corp_id", corpId));
        if (accountInfo != null) {
            Result corpResult = this.uasFeignService.findCorpById(corpId);
            CorpDto corpDto = new CorpDto();
            if (corpResult != null && corpResult.getCode() == Result.SUCCESS) {
                corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
            }
            accountInfo.setCorpName(corpDto.getCorpName());
            accountInfo.setAccountTypeName(AccountInfoEnum.AccountType.getNameByCode(accountInfo.getAccountType()));
            accountInfo.setStatusName(AccountInfoEnum.Status.getNameByCode(accountInfo.getStatus()));
        }
        return accountInfo;
    }

    @Override
    public void userInitWallet(Long corpId) {

    }

    @Override
    public AccountInfo userViewWallet(Long corpId) {
        return null;
    }

    @Override
    public void walletTradeByThirdPay(AccountInfoTraceDto accountInfoTraceDTO) {
        // 订单相关信息
        int applySource = accountInfoTraceDTO.getApplySource();                 // 申请来源
        Long applyId = accountInfoTraceDTO.getApplyId();                        // 申请号
        String applyName = accountInfoTraceDTO.getApplyName();                  // 申请名称
        BigDecimal amount = accountInfoTraceDTO.getAmount();                    // 金额
        BigDecimal payFeeAmount = accountInfoTraceDTO.getPayFeeAmount();        // 订单支付平台手续费
        BigDecimal platformFeeAmount = accountInfoTraceDTO.getPlatFeeAmount();  // 平台服务费

        // 平台账户信息
        AccountInfo platformAccountInfo = this.getOne(new QueryWrapper<AccountInfo>().eq("account_type", "p"));
        Long platformFromAccountId = platformAccountInfo.getId(); // 平台自定义钱包号: TODO

        Date currentDate = DateUtil.date();
        if (applySource == AccountTraceEnum.ApplySource.pay.getCode()) {
            AccountInfo fromAccountInfo = this.getById(accountInfoTraceDTO.getPayerAccountId()); // 付款账户
            AccountInfo toAccountInfo = this.getById(accountInfoTraceDTO.getPayeeAccountId()); // 收款账户

            // 支付类流水
            log.info("支付申请单 申请ID号:{},申请(结算单)名称:{},支付订单来源:{},金额:{},支付平台手续费:{},平台服务费:{}",
                    applyId, applyName, applySource, amount, payFeeAmount, platformFeeAmount);
            // ======= 更新钱包以及钱包记录相关信息
            // 1. === 付款方, 1. 需更新钱包总支出，2. 不需要增加交易记录
            log.info("更新付款方:[{}]相关信息,支出金额:{}", fromAccountInfo.getId(), amount);
            BigDecimal fromTotalExpend = fromAccountInfo.getTotalExpend().add(amount); // 增加总支出
            fromAccountInfo.setTotalExpend(fromTotalExpend);
            this.saveOrUpdate(fromAccountInfo);

            // 2. === 收款方,1. 更新钱包余额和总收入，2. 增加交易记录
            log.info("更新收款方:[{}]相关信息,收入金额:{}", toAccountInfo.getId(), amount);
            BigDecimal toBalance = toAccountInfo.getBalance().add(amount); // 增加余额
            BigDecimal toTotalIncome = toAccountInfo.getTotalIncome().add(amount); // 增加总收入
            toAccountInfo.setBalance(toBalance);
            toAccountInfo.setTotalIncome(toTotalIncome);
            this.saveOrUpdate(toAccountInfo);
            AccountTrace toAccountTrace =
                    new AccountTrace()
                            .setId(KeyUtil.getId())
                            .setAccountId(toAccountInfo.getId())
                            .setAmount(amount)
                            .setBalance(toAccountInfo.getBalance())
                            .setApplyId(applyId)
                            .setTraceType(CommonEnum.TraceType.consume.getCode())
                            .setApplySource(applySource)
                            .setApplyName(applyName)
                            .setTime(currentDate)
                            .setDirection(AccountTraceEnum.Direction.in.getCode());
            accountTraceService.save(toAccountTrace);

            // 3. === 平台方，1. 更新钱包，2. 增加手续费记录
            log.info("更新平台方:[{}]相关信息,支付平台手续费:{}", platformFromAccountId, payFeeAmount);
            // 支付手续费
            BigDecimal currentPlatformBalance = platformAccountInfo.getBalance();
            BigDecimal currentPlatformTotalExpend = platformAccountInfo.getTotalExpend();

            if (accountInfoTraceDTO.getPayFeeAmount().compareTo(BigDecimal.ZERO) > 0) {
                currentPlatformBalance = currentPlatformBalance.subtract(payFeeAmount);
                currentPlatformTotalExpend = currentPlatformTotalExpend.add(payFeeAmount);

                AccountTrace payFeeTrace = new AccountTrace()
                        .setId(KeyUtil.getId())
                        .setAccountId(platformFromAccountId)
                        .setAmount(payFeeAmount)
                        .setBalance(currentPlatformBalance)
                        .setApplyId(applyId)
                        .setTraceType(CommonEnum.TraceType.pay_platform_fee.getCode())
                        .setApplySource(applySource)
                        .setApplyName(applyName)
                        .setTime(currentDate)
                        .setDirection(AccountTraceEnum.Direction.out.getCode());
                accountTraceService.save(payFeeTrace);
            }
            // 平台费用
            if (platformFeeAmount.compareTo(BigDecimal.ZERO) > 0) {
                currentPlatformBalance = currentPlatformBalance.subtract(platformFeeAmount);
                currentPlatformTotalExpend = currentPlatformTotalExpend.add(platformFeeAmount);

                AccountTrace platFeeTrace = new AccountTrace()
                        .setId(KeyUtil.getId())
                        .setAccountId(platformFromAccountId)
                        .setAmount(payFeeAmount)
                        .setBalance(currentPlatformBalance)
                        .setApplyId(applyId)
                        .setTraceType(CommonEnum.TraceType.platform_fee.getCode())
                        .setApplySource(applySource)
                        .setApplyName(applyName)
                        .setTime(currentDate)
                        .setDirection(AccountTraceEnum.Direction.in.getCode());
                accountTraceService.save(platFeeTrace);
            }
            platformAccountInfo.setBalance(currentPlatformBalance);
            platformAccountInfo.setTotalExpend(currentPlatformTotalExpend);
            this.saveOrUpdate(platformAccountInfo);

        } else if (applySource == AccountTraceEnum.ApplySource.withdraw.getCode()) {
            // ===== 更新提现账户
            AccountInfo withdrawAccountInfo = this.getById(accountInfoTraceDTO.getWithdrawAccountId()); // 提现账户
            if (withdrawAccountInfo.getBalance().compareTo(amount) < 0) {
                throw new AppException("余额不足无法提现");
            }
            BigDecimal newWithdrawBalance = withdrawAccountInfo.getBalance().subtract(amount);
            BigDecimal newWithdrawTotalExpend = withdrawAccountInfo.getTotalExpend().add(platformFeeAmount);
            withdrawAccountInfo.setBalance(newWithdrawBalance);
            withdrawAccountInfo.setTotalExpend(newWithdrawTotalExpend);
            this.saveOrUpdate(withdrawAccountInfo);

            // ===== 更新平台账户
            // 提现支付宝手续费
            BigDecimal currentPlatformBalance = platformAccountInfo.getBalance();
            BigDecimal currentPlatformTotalExpend = platformAccountInfo.getTotalExpend();

            if (accountInfoTraceDTO.getPayFeeAmount().compareTo(BigDecimal.ZERO) > 0) {
                currentPlatformBalance = currentPlatformBalance.subtract(payFeeAmount);
                currentPlatformTotalExpend = currentPlatformTotalExpend.add(payFeeAmount);

                AccountTrace payFeeTrace = new AccountTrace()
                        .setId(KeyUtil.getId())
                        .setAccountId(platformFromAccountId)
                        .setAmount(payFeeAmount)
                        .setBalance(platformAccountInfo.getBalance())
                        .setApplyId(applyId)
                        .setTraceType(CommonEnum.TraceType.withdraw.getCode())
                        .setApplySource(applySource)
                        .setApplyName(applyName)
                        .setTime(currentDate)
                        .setDirection(AccountTraceEnum.Direction.out.getCode());
                accountTraceService.save(payFeeTrace);
            }
            // 提现平台费用
            if (platformFeeAmount.compareTo(BigDecimal.ZERO) > 0) {
                currentPlatformBalance = currentPlatformBalance.subtract(platformFeeAmount);
                currentPlatformTotalExpend = currentPlatformTotalExpend.add(platformFeeAmount);

                AccountTrace platFeeTrace = new AccountTrace()
                        .setId(KeyUtil.getId())
                        .setAccountId(platformFromAccountId)
                        .setAmount(payFeeAmount)
                        .setBalance(platformAccountInfo.getBalance())
                        .setApplyId(applyId)
                        .setTraceType(CommonEnum.TraceType.platform_fee.getCode())
                        .setApplySource(applySource)
                        .setApplyName(applyName)
                        .setTime(currentDate)
                        .setDirection(AccountTraceEnum.Direction.in.getCode());
                accountTraceService.save(platFeeTrace);
            }
            platformAccountInfo.setBalance(currentPlatformBalance);
            platformAccountInfo.setTotalExpend(currentPlatformTotalExpend);
            this.saveOrUpdate(platformAccountInfo);

        } else if (applySource == AccountTraceEnum.ApplySource.refund.getCode()) {
            // ====== 更新退款账户


            // ====== 更新平台账户
        }
    }

    @Override
    public Map<Long, AccountInfo> accountIdAndAccountInfoMap() {
        List<AccountInfo> accountInfoList = this.list();
        List<Long> corpIdList = accountInfoList.stream().map(AccountInfo::getCorpId).filter(id -> id != null).collect(Collectors.toList());
        Map<Long, CorpRegistry> corpIdAndCorpRegistryMap = this.uasFeignService.corpIdAndCorpRegistryMap(corpIdList);
        accountInfoList.forEach((AccountInfo accountInfo) -> {
            if (accountInfo.getAccountType().equals(AccountInfoEnum.AccountType.person.getCode())) {
                // 个人钱包 TODO
            } else {
                CorpRegistry corpRegistry = corpIdAndCorpRegistryMap.get(accountInfo.getCorpId());
                accountInfo.setCorpName(corpRegistry.getCorpName());
            }
        });
        Map<Long, AccountInfo> accountIdAndAccountInfodMap = accountInfoList.stream().collect(Collectors.toMap(AccountInfo::getId, Function.identity()));
        return accountIdAndAccountInfodMap;
    }


}
