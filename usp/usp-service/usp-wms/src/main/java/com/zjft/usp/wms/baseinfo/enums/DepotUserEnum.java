package com.zjft.usp.wms.baseinfo.enums;

/**
 * 库房人员类型枚举类
 *
 * @author jfzou
 * @date 2019/11/06 9:22
 * @Version 1.0
 **/
public enum DepotUserEnum {
    MANAGER(10, "库房负责人"),
    CONSIGNEE(20, "收货人");
    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    DepotUserEnum(int code, String name) {
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
    public static String getNameByCode(Integer code) {
        for (DepotUserEnum typeEnum : DepotUserEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
