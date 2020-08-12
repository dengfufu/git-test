package com.zjft.usp.anyfix.goods.enums;

import com.zjft.usp.common.utils.IntUtil;

/**
 * 托运方式枚举类
 *
 * @author zgpi
 * @date 2020/4/20 10:05
 **/
public enum ConsignTypeEnum {

    BUS(10, "汽车"),
    TRAIN(20, "火车"),
    SHIP(30, "轮船"),
    PLANE(40, "飞机"),
    OTHER(50, "其他");

    /**
     * 状态编码
     **/
    private int code;
    /**
     * 状态名称
     **/
    private String name;

    ConsignTypeEnum(int code, String name) {
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
        for (ConsignTypeEnum consignTypeEnum : ConsignTypeEnum.values()) {
            if (code == consignTypeEnum.getCode()) {
                return consignTypeEnum.getName();
            }
        }
        return null;
    }
}
