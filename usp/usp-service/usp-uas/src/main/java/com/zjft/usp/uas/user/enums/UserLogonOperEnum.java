package com.zjft.usp.uas.user.enums;

/**
 * 用户登录操作类型枚举类
 *
 * @author zgpi
 * @date 2020/5/26 15:32
 */
public enum UserLogonOperEnum {

    LOGON(1, "登录"),
    AUTO_LOGON(2, "自动登录"),
    LOGOUT(3, "登出");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    UserLogonOperEnum(Integer code, String name) {
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
        for (UserLogonOperEnum operEnum : UserLogonOperEnum.values()) {
            if (operEnum.getCode().equals(code)) {
                return operEnum.getName();
            }
        }
        return null;
    }
}
