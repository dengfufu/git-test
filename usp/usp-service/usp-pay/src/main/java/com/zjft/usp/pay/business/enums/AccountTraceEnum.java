package com.zjft.usp.pay.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 钱包交易记录
 *
 * @author: CK
 * @create: 2020-05-29 13:47
 */
public final class AccountTraceEnum {

    /**
     * 资金方向
     */
    @AllArgsConstructor
    @Getter
    public enum Direction {
        in(10, "收入"),
        out(20, "支出");

        private int code;
        private String name;

        public static String getNameByCode(int code) {
            for (Direction typeEnum : Direction.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }

    @AllArgsConstructor
    @Getter
    public enum ApplySource {

        pay(100, "支付"),
        withdraw(200, "提现"),
        refund(100, "退款");

        private int code;
        private String name;

        public static String getNameByCode(int code) {
            for (ApplySource typeEnum : ApplySource.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }
}



