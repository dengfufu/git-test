package com.zjft.usp.wms.form.enums;

/**
 * 是否系统内置枚举类
 * 用于表单模板是否为系统内置
 * @author zgpi
 * @date 2019/10/16 3:10 下午
 **/
public enum SysBuildInEnum {

    YES("Y", "是"),
    NO("N", "否");

    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    SysBuildInEnum(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
     * @date 2019/10/16 10:15 上午
     **/
    public static String getNameByCode(String code) {
        for (SysBuildInEnum enabledEnum : SysBuildInEnum.values()) {
            if (enabledEnum.getCode().equals(code)) {
                return enabledEnum.getName();
            }
        }
        return null;
    }
}
