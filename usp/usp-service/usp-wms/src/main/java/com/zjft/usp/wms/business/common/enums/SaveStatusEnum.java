package com.zjft.usp.wms.business.common.enums;

/**
 * 保存状态枚举类
 *
 * @author jfzou
 * @date 2019/11/06 9:22
 * @Version 1.0
 **/
public enum SaveStatusEnum {
    SAVE(10, "待提交"),
    SUBMIT(20, "已提交");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    SaveStatusEnum(int code, String name) {
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
        for (SaveStatusEnum typeEnum : SaveStatusEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
