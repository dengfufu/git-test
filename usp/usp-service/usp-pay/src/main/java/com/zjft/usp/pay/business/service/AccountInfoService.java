package com.zjft.usp.pay.business.service;

import com.zjft.usp.pay.business.dto.AccountInfoTraceDto;
import com.zjft.usp.pay.business.model.AccountInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 账户表 服务类
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
public interface AccountInfoService extends IService<AccountInfo> {

    /**
     * 企业开通钱包
     *
     * @param corpId
     */
    Long corpInitWallet(Long corpId);

    /**
     * 企业查询账户钱包信息
     *
     * @param corpId
     * @return
     */
    AccountInfo corpViewWallet(Long corpId);

    /**
     * 个人开通钱包
     *
     * @param corpId
     */
    void userInitWallet(Long corpId);

    /**
     * 个人查询账户钱包信息
     *
     * @param corpId
     * @return
     */
    AccountInfo userViewWallet(Long corpId);


    /**
     * 第三方支付交易后: 更新钱包，增加记录
     *
     * @param accountInfoTraceDTO
     */
    void walletTradeByThirdPay(AccountInfoTraceDto accountInfoTraceDTO);

    Map<Long,AccountInfo> accountIdAndAccountInfoMap();

}
