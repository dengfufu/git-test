package com.zjft.usp.uas.user.enums;

/**
 * 设备类型枚举类
 * @author zgpi
 * @version 1.0
 * @date 2019/11/28 14:10
 */
public enum DeviceTypeEnum {

    IPHONE(1,"IPhone"),
    IPAD(2,"IPad"),
    ANDROID(3,"Android"),
    MOBILE_WEB(4,"Mobile Web"),
    PC(5,"PC");

    private String name;
    private Integer code;

    DeviceTypeEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public Integer getCode() {
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


    /**
     * 根据code获得name
     *
     * @param code
     * @return
     * @author zgpi
     * @date 2020/5/25 15:21
     **/
    public static String getNameByCode(Integer code) {
        for (DeviceTypeEnum deviceTypeEnum : DeviceTypeEnum.values()) {
            if (deviceTypeEnum.getCode().equals(code)) {
                return deviceTypeEnum.getName();
            }
        }
        return null;
    }
}
