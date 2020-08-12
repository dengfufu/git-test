package com.zjft.usp.anyfix.work.support.enums;

/**
 * 严重度枚举类
 *
 * @author cxd
 * @version 1.0
 * @date 2020/4/23 10:55
 **/
public enum SeverityEnum {

    SLIGHT(1, "轻微"),
    COMMON(2, "一般"),
    SERIOUS(3, "严重"),
    VERY_SERIOUS(4, "非常严重");

    /**
     * 服务方式代码
     */
    private int code;

    /**
     * 服务方式名称
     */
    private String name;

    SeverityEnum(int code, String name) {
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
        for (SeverityEnum severityEnum : SeverityEnum.values()) {
            if (code == severityEnum.getCode()) {
                return severityEnum.getName();
            }
        }
        return null;
    }
}
