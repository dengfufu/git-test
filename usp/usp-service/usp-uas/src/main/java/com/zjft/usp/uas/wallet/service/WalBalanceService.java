package com.zjft.usp.uas.wallet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.wallet.model.WalBalance;
import java.math.BigDecimal;

/**
 * @description: 服务类
 * @author chenxiaod
 * @date 2019/8/6 16:30
 */
public interface WalBalanceService extends IService<WalBalance> {

    /**
     * description: 查询钱包余额
     *
     * @param userId 用户id
     * @return java.math.BigDecimal
     */
    BigDecimal walBalanceQuery(long userId);
}
