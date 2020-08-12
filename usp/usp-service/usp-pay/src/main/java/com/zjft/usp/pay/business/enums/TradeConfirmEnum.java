package com.zjft.usp.pay.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: CK
 * @create: 2020-06-02 09:26
 */
public final class TradeConfirmEnum {

    /**
     * 交易申请类型
     */
    @AllArgsConstructor
    @Getter
    public enum TradeType {
        //
        pay(10, "支付"),
        refund(20, "退款"),
        transfer(20, "转账");

        private int code;
        private String name;

        public static String getNameByCode(long code) {
            for (TradeType typeEnum : TradeType.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }

    /**
     * 钱包账户类型
     */
    @AllArgsConstructor
    @Getter
    public enum Freq {
        minute("m", "分钟"),
        hour("h", "小时"),
        day("d", "天");

        private String code;
        private String name;

        public static String getNameByCode(String code) {
            for (Freq typeEnum : Freq.values()) {
                if (code.equals(typeEnum.getCode())) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }
}
