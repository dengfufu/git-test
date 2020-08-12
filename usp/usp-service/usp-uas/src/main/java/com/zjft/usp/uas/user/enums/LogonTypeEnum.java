package com.zjft.usp.uas.user.enums;

/**
 * 用户登录类型枚举类
 *
 * @author zgpi
 * @date 2020/5/25 15:20
 */
public enum LogonTypeEnum {

    MOBILE_PWD(1, "手机号+密码"),
    MOBILE_SMSCODE(2, "手机号+短信验证码"),
    WECHAT(3, "微信");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    LogonTypeEnum(Integer code, String name) {
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
        for (LogonTypeEnum typeEnum : LogonTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
