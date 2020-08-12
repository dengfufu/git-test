package com.zjft.usp.pay.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: CK
 * @create: 2020-05-27 11:04
 */
public final class CommonEnum {

    /**
     * 订单来源
     */
    @AllArgsConstructor
    @Getter
    public enum OrderSource {
        demander(100, "客户结算单"),
        device_user(101, "客户结算单"),
        engineer(102, "工程师结算单"),
        recharge(200, "充值订单"),
        withdraw(300, "提现订单");

        private int code;
        private String name;

        public static String getNameByCode(long code) {
            for (OrderSource typeEnum : OrderSource.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }

    /**
     * 交易类型
     */
    @AllArgsConstructor
    @Getter
    public enum OrderType {
        consume(100000, "支付消费"),
        wallet_consume(100001, "余额消费");

        private int code;
        private String name;

        public static String getNameByCode(long code) {
            for (OrderType typeEnum : OrderType.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }

    /**
     * 交易类型
     */
    @AllArgsConstructor
    @Getter
    public enum TraceType {
        consume(100000, "支付消费"),
        wallet_consume(100001, "余额消费"),
        withdraw(200000, "提现"),
        refund(300000, "退款"),
        refund_pay_platform_fee(900000, "退支付手续费"),
        pay_platform_fee(900001, "支付平台手续费"),
        platform_fee(900010, "平台费用"),
        refund_platform_fee(900011, "退平台费用"),
        platform_rent(900020, "入驻费用"),
        refund_platform_rent(900021, "退入驻费用"),
        platform_marketing(900030, "营销活动");

        private int code;
        private String name;

        public static String getNameByCode(int code) {
            for (TraceType typeEnum : TraceType.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }


    /**
     * 支付方式
     */
    @AllArgsConstructor
    @Getter
    public enum PayWay {
        ali(10, "支付宝"),
        weixin(20, "微信"),
        wallet(30, "钱包余额");

        private int code;
        private String name;

        public static String getNameByCode(long code) {
            for (PayWay typeEnum : PayWay.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }


    /**
     * 对账状态
     */
    @AllArgsConstructor
    @Getter
    public enum CheckStatus {
        defaultStatus(10, "未对账"),
        success(20, "对账成功"),
        fail(21, "对账失败");

        private int code;
        private String name;

        public static String getNameByCode(long code) {
            for (CheckStatus typeEnum : CheckStatus.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }
}
