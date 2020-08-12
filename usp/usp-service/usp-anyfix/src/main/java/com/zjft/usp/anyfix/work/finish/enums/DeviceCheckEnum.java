package com.zjft.usp.anyfix.work.finish.enums;

/**
 * 设备档案检查结果枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/2/6 13:11
 **/
public enum DeviceCheckEnum {

    ADD(1, "新增设备档案"),
    MOD(2, "修改设备档案");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    DeviceCheckEnum(Integer code, String name) {
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
        for (DeviceCheckEnum statusEnum : DeviceCheckEnum.values()) {
            if (statusEnum.getCode() == code) {
                return statusEnum.getName();
            }
        }
        return null;
    }

}
