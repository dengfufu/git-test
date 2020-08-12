package com.zjft.usp.pay.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付申请
 *
 * @author: CK
 * @create: 2020-05-26 18:37
 */
public final class PaymentApplyEnum {

    /**
     * 订单状态
     */
    @AllArgsConstructor
    @Getter
    public enum Status {
        create(100, "订单创建"), // 用户主动
        cancel(101, "订单取消"), // 用户主动
        in_pay(200, "付款中"), // 系统请求支付宝轮询
        pay_success(300, "支付成功"), // 系统请求支付宝轮询
        pay_fail(301, "支付失败");// 系统请求支付宝轮询
        private int code;
        private String name;

        public static String getNameByCode(long code) {
            for (Status typeEnum : Status.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }

    /**
     * 支付渠道
     */
    @AllArgsConstructor
    @Getter
    public enum ChannelType {
        ali_pc(10, "支付宝电脑支付"),
        weixin_pc(20, "微信电脑支付"),
        wallet(30, "钱包余额支付");
        private int code;
        private String name;

        public static String getNameByCode(long code) {
            for (ChannelType typeEnum : ChannelType.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }
}
