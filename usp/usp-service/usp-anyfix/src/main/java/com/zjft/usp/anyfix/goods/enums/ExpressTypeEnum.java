package com.zjft.usp.anyfix.goods.enums;

import com.zjft.usp.common.utils.IntUtil;

/**
 * 快递类型枚举类
 *
 * @author zgpi
 * @date 2020/4/20 10:30
 **/
public enum ExpressTypeEnum {

    LOGISTICS(1, "物流"),
    FAST(2, "快件"),
    SLOW(3, "慢件");

    /**
     * 状态编码
     **/
    private int code;
    /**
     * 状态名称
     **/
    private String name;

    ExpressTypeEnum(int code, String name) {
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
        for (ExpressTypeEnum expressTypeEnum : ExpressTypeEnum.values()) {
            if (code == expressTypeEnum.getCode()) {
                return expressTypeEnum.getName();
            }
        }
        return null;
    }
}
