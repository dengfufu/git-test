package com.zjft.usp.wms.form.enums;

/**
 * 字段分类枚举类
 * 用于表单模板
 * @author jfzou
 * @date 2019/11/06 9:22
 * @Version 1.0
 **/
public enum FieldCatalogEnum {
    SYS_CORE(10, "系统必备字段"),
    SYS_OPTIONAL(20, "系统可选字段"),
    USER_DEFINE(30, "用户自定义字段");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    FieldCatalogEnum(int code, String name) {
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
     *
     * @param code 编码值
     * @return name 编码名称
     * @author jfzou
     * @date 2019/11/07 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (FieldCatalogEnum typeEnum : FieldCatalogEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
