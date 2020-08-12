package com.zjft.usp.wms.business.consign.enums;

/**
 * 快递类型
 *
 * @author zphu
 * @date 2019/12/16 9:22
 * @Version 1.0
 **/
public enum ExpressTypeEnum {
    LOGISTICS(10, "物流"),
    FAST(20, "快件"),
    REGULAR(30, "慢件");
    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
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

    /**
     * 根据code获得name
     *
     * @param code
     * @return
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     **/
    public static String getNameByCode(int code) {
        for (ExpressTypeEnum typeEnum : ExpressTypeEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
