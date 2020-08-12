package com.zjft.usp.uas.wallet.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @description: 钱包明细功能
 * @author chenxiaod
 * @date 2019/8/6 16:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_wal_detail")
public class WalDetail {

    /** 用户ID **/
    private long userId;

    /** 交易号 **/
    private int txId;

    /** 交易类型 **/
    private int txType;

    /** 币种 **/
    private String ccy;

    /** 交易金额 **/
    private BigDecimal amt;

    /** 钱包余额 **/
    private BigDecimal balance;

    /** 交易时间 **/
    private Timestamp addTime;
}
