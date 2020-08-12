package com.zjft.usp.anyfix.settle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 支付方式
 *
 * @author CK
 * @since 2020-06-11
 */
@AllArgsConstructor
@Getter
public enum SettlePayMethodEnum {

    PAY_ONLINE(1, "在线支付"),
    PAY_OFFLINE(2, "线下支付");

    private int code;
    private String name;

    public static String getNameByCode(int code) {
        for (SettlePayMethodEnum typeEnum : SettlePayMethodEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
