package com.zjft.usp.anyfix.work.request.enums;

/**
 * 工单来源枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/24 8:55 上午
 **/
public enum WorkSourceEnum {

    APP(1, "APP"),
    WECHAT(2, "微信"),
    WEB(3, "网页"),
    PHONE(4, "电话"),
    EMAIL(5,"邮件");

    /**
     * 来源编码
     **/
    private int code;
    /**
     * 来源名称
     **/
    private String name;

    WorkSourceEnum(int code, String name) {
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
        for (WorkSourceEnum workSourceEnum : WorkSourceEnum.values()) {
            if (code == workSourceEnum.getCode()) {
                return workSourceEnum.getName();
            }
        }
        return null;
    }
    
}
