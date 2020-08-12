package com.zjft.usp.anyfix.goods.enums;

import com.zjft.usp.common.utils.IntUtil;

/**
 * 运输方式枚举类
 *
 * @author zgpi
 * @date 2020/4/20 10:05
 **/
public enum TransportTypeEnum {

    TAKE_THEIR(1, "自提"),
    CONSIGN(2, "托运"),
    EXPRESS(3, "快递");

    /**
     * 状态编码
     **/
    private int code;
    /**
     * 状态名称
     **/
    private String name;

    TransportTypeEnum(int code, String name) {
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
        for (TransportTypeEnum transportTypeEnum : TransportTypeEnum.values()) {
            if (code == transportTypeEnum.getCode()) {
                return transportTypeEnum.getName();
            }
        }
        return null;
    }
}
