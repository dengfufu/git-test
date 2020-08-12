package com.zjft.usp.wms.baseinfo.enums;

/**
 * 业务大类枚举类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-12 13:54
 **/
public enum LargeClassEnum {
    INCOME(10, "入库"),
    OUTCOME(20, "出库"),
    TRANS(30, "转库"),
    INVENTORY(40, "盘点"),
    REPAIR(50, "维修"),
    ASSEMBLE(60, "组装"),
    DISCARD(70, "报废"),
    SPLIT(80, "拆分");


    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    LargeClassEnum(int code, String name) {
        this.code = code;
        this.name = name;
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
     * @return String
     **/
    public static String getNameByCode(Integer code) {
        for (LargeClassEnum typeEnum : LargeClassEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
