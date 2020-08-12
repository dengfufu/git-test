package com.zjft.usp.anyfix.goods.enums;

import com.zjft.usp.common.utils.IntUtil;

/**
 * 付费方式枚举类
 *
 * @author zgpi
 * @date 2020/5/13 20:17
 **/
public enum PayWayEnum {

    CONSIGN_PAY(1, "寄付"),
    RECEIVE_PAY(2, "到付");

    /**
     * 状态编码
     **/
    private int code;
    /**
     * 状态名称
     **/
    private String name;

    PayWayEnum(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByCode(Integer code) {
        if (IntUtil.isZero(code)) {
            return null;
        }
        for (PayWayEnum consignTypeEnum : PayWayEnum.values()) {
            if (code == consignTypeEnum.getCode()) {
                return consignTypeEnum.getName();
            }
        }
        return null;
    }
}
