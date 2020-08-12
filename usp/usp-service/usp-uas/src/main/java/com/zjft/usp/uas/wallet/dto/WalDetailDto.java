package com.zjft.usp.uas.wallet.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @description: 钱包明细类
 * @author chenxiaod
 * @date 2019/8/21 17:29
 */
@Data
public class WalDetailDto {

    /** 交易类型 **/
    private int txType;

    /** 交易金额 **/
    private BigDecimal amt;

    /** 钱包余额 **/
    private BigDecimal balance;

    /** 交易时间 **/
    private Timestamp addTime;
}
