package com.zjft.usp.uas.wallet.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @description:  钱包余额功能
 * @author chenxiaod
 * @date 2019/8/6 15:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_wal_balance")
public class WalBalance {

    /** 用户ID **/
    private long userId;

    /** 币种 **/
    private String ccy;

    /** 余额 **/
    private BigDecimal balance;
}
