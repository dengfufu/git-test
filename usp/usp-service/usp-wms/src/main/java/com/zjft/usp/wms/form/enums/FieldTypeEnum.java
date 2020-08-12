package com.zjft.usp.wms.form.enums;

/**
 * 字段类型枚举类
 * 用于表单模板定义字段类型
 * @author jfzou
 * @date 2019/11/06 9:22
 * @Version 1.0
 **/
public enum FieldTypeEnum {
    ONE_TEXT(10, "单行文本"),
    MORE_TEXT(20, "多行文本"),
    UPLOAD_IMAGE(30, "上传图片"),
    UPLOAD_FILE(40, "上传文件"),
    SYS_LIST(50, "系统列表"),
    CUSTOM_LIST(60, "自定义列表"),
    DATE(70, "日期"),
    NUMBER(80, "数字");
    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    FieldTypeEnum(int code, String name) {
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
     * @param code
     * @return
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (FieldTypeEnum typeEnum : FieldTypeEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
