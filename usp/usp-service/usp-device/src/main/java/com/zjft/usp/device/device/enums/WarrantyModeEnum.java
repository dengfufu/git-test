package com.zjft.usp.device.device.enums;

/**
 * 维保方式枚举类
 *
 * @author zrlin
 * @version 1.0
 * @date 2020/3/31 13:55 下午
 **/
public enum WarrantyModeEnum {

    IN(10, "整机保"),
    OUT(20, "单次保");

    /**
     * 状态编码
     **/
    private int code;
    /**
     * 状态名称
     **/
    private String name;

    WarrantyModeEnum(int code, String name) {
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
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (WarrantyModeEnum warrantyEnum : WarrantyModeEnum.values()) {
            if (code == warrantyEnum.getCode()) {
                return warrantyEnum.getName();
            }
        }
        return null;
    }
}
