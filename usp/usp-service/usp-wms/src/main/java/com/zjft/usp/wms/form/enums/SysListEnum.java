package com.zjft.usp.wms.form.enums;

/**
 * 系统列表枚举类
 *
 * @author jfzou
 * @date 2019/11/06 9:22
 * @Version 1.0
 **/
public enum SysListEnum {
    BRAND(10, "品牌列表"),
    MODEL(20, "型号列表"),
    CATALOG(30, "分类列表"),
    DEPOT(40, "库房列表"),
    STATUS(50, "状态列表"),
    RIGHT(60, "产权列表"),
    AREA(70, "区域列表"),
    USER(80, "用户列表"),
    SUPPLIER(90, "供应商列表");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    SysListEnum(int code, String name) {
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
     * @author jfzou
     * @date 2019/11/07 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (SysListEnum typeEnum : SysListEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
