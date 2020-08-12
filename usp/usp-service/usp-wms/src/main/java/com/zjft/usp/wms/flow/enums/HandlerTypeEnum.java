package com.zjft.usp.wms.flow.enums;

/**
 * 处理人类型枚举类
 *
 * @author jfzou
 * @date 2019/11/18 9:22
 * @Version 1.0
 **/
public enum HandlerTypeEnum {
    ASSIGN_TO_CREATE_BY(10, "发起人"),
    ASSIGN_TO_ROLE(20, "指定角色"),
    ASSIGN_TO_PEOPLE(30, "指定用户");
    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    HandlerTypeEnum(int code, String name) {
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
        for (HandlerTypeEnum typeEnum : HandlerTypeEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
