package com.zjft.usp.anyfix.work.request.enums;

/**
 * 优先级枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/24 4:55 下午
 **/
public enum WorkPriorityEnum {

    GENERAL(1, "一般"),
    URGENT(2, "紧急"),
    VERY_URGENT(3, "非常紧急");

    /**
     * 优先级编码
     **/
    private int code;
    /**
     * 优先级名称
     **/
    private String name;

    WorkPriorityEnum(int code, String name) {
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
        for (WorkPriorityEnum workPriorityEnum : WorkPriorityEnum.values()) {
            if (code == workPriorityEnum.getCode()) {
                return workPriorityEnum.getName();
            }
        }
        return null;
    }
}
