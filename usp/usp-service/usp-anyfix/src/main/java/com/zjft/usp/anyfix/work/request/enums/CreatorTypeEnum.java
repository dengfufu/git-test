package com.zjft.usp.anyfix.work.request.enums;

/**
 * 建单人类型枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/2/20 15:55
 **/
public enum CreatorTypeEnum {

    DEMANDER(1,"委托商"),
    SERVICE(2,"服务商"),
    USER(3,"普通用户");

    /**
     * 建单人类型代码
     */
    private int code;

    /**
     * 建单人类型名称
     */
    private String name;

    CreatorTypeEnum(int code, String name) {
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
     **/
    public static String getNameByCode(Integer code) {
        for (CreatorTypeEnum creatorTypeEnum : CreatorTypeEnum.values()) {
            if (code == creatorTypeEnum.getCode()) {
                return creatorTypeEnum.getName();
            }
        }
        return null;
    }
}
