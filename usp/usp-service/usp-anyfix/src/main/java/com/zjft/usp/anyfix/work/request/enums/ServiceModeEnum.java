package com.zjft.usp.anyfix.work.request.enums;

/**
 * 服务类型枚举类
 *
 * @author canlei
 * @version 1.0
 * @date 2019/9/24 4:55 下午
 **/
public enum ServiceModeEnum {

    LOCALE_SERVICE(1, "现场服务"),
    REMOTE_SERVICE(2, "远程服务");

    /**
     * 服务方式代码
     */
    private int code;

    /**
     * 服务方式名称
     */
    private String name;

    ServiceModeEnum(int code, String name) {
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
        for (ServiceModeEnum serviceModeEnum : ServiceModeEnum.values()) {
            if (code == serviceModeEnum.getCode()) {
                return serviceModeEnum.getName();
            }
        }
        return null;
    }
}
