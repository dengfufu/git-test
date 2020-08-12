package com.zjft.usp.device.device.enums;

/**
 * 保修状态枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/24 8:55 上午
 **/
public enum DeviceStatusEnum {

    RUN(1, "运行"),
    PAUSE(2, "暂停"),
    DEATH(3,"死亡");

    /**
     * 状态编码
     **/
    private int code;
    /**
     * 状态名称
     **/
    private String name;

    DeviceStatusEnum(int code, String name) {
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
        for (DeviceStatusEnum deviceStatusEnum : DeviceStatusEnum.values()) {
            if (code == deviceStatusEnum.getCode()) {
                return deviceStatusEnum.getName();
            }
        }
        return null;
    }
}
