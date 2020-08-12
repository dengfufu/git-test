package com.zjft.usp.uas.wallet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.wallet.model.WalBalance;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;

/**
 * @description: 钱包余额Mapper 接口
 * @author chenxiaod
 * @date 2019/8/6 16:01
 */
public interface WalBalanceMapper extends BaseMapper<WalBalance> {

    /**
     * description: 查询钱包余额
     *
     * @param userId 用户id
     * @return java.math.BigDecimal
     */
    BigDecimal walBalanceQuery(@Param("userId")long userId);
}