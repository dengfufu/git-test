package com.zjft.usp.anyfix.work.request.enums;

/**
 * 城郊枚举类
 *
 * @author cxd
 * @version 1.0
 * @date 2020/3/11 22:55 下午
 **/
public enum ZoneEnum {

    CITY(1, "市区"),
    SUBURBAN(2, "郊县");

    /**
     * 服务方式代码
     */
    private int code;

    /**
     * 服务方式名称
     */
    private String name;

    ZoneEnum(int code, String name) {
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
        for (ZoneEnum zoneEnum : ZoneEnum.values()) {
            if (code == zoneEnum.getCode()) {
                return zoneEnum.getName();
            }
        }
        return null;
    }
}
