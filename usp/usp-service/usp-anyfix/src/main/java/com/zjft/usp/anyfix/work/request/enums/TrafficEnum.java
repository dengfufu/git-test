package com.zjft.usp.anyfix.work.request.enums;

/**
 * @Author: JFZOU
 * @Date: 2020-03-02 15:06
 * @Version 1.0
 */
public enum TrafficEnum {
    NULL(0,""),
    BUS(1,"公交"),
    MOTOR(2, "摩的"),
    WALK(3, "步行"),
    LONG_BUS(4, "长途汽车"),
    SCENE(5, "现场"),
    TAXI(6, "出租车"),
    TRAIN(7, "火车"),
    PLANE(8, "飞机"),
    CORP_CAR(9, "公司车"),
    BANK_CAR(10, "银行车"),
    ZONE_BUS(11, "郊县汽车"),
    SHIP(12, "轮渡");

    /**
     * 交通工具编码
     **/
    private Integer code;
    /**
     * 交通工具名称
     **/
    private String name;

    TrafficEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getNameByCode(Integer code) {
        for (TrafficEnum trafficEnum : TrafficEnum.values()) {
            if (trafficEnum.getCode() == code) {
                return trafficEnum.getName();
            }
        }
        return null;
    }
}
