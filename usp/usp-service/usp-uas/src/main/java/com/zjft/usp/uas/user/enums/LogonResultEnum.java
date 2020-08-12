package com.zjft.usp.uas.user.enums;

/**
 * 登录结果枚举类
 *
 * @author zgpi
 * @date 2020/5/25 15:25
 */
public enum LogonResultEnum {

    SUCCESS(1, "成功"),
    PWD_FAIL(2, "密码错误");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    LogonResultEnum(Integer code, String name) {
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
        for (LogonResultEnum resultEnum : LogonResultEnum.values()) {
            if (resultEnum.getCode().equals(code)) {
                return resultEnum.getName();
            }
        }
        return null;
    }

}
