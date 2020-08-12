package com.zjft.usp.pay.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: CK
 * @create: 2020-05-28 13:47
 */
public final class AccountInfoEnum {

    /**
     * 钱包账户类型
     */
    @AllArgsConstructor
    @Getter
    public enum AccountType {
        enterprise("e", "企业钱包"),
        person("c", "个人钱包"),
        platform("p", "平台钱包");

        private String code;
        private String name;

        public static String getNameByCode(String code) {
            for (AccountType typeEnum : AccountType.values()) {
                if (code.equals(typeEnum.getCode())) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }

    /**
     * 钱包账户状态
     */
    @AllArgsConstructor
    @Getter
    public enum Status {

        active(10, "激活"),
        freeze(20, "冻结");

        private int code;
        private String name;

        public static String getNameByCode(int code) {
            for (Status typeEnum : Status.values()) {
                if (code == typeEnum.getCode()) {
                    return typeEnum.getName();
                }
            }
            return null;
        }
    }


}



