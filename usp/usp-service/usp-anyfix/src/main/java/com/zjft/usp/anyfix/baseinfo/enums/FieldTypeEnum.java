package com.zjft.usp.anyfix.baseinfo.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段类型枚举类
 *
 * @author cxd
 * @date 2019/1/3 9:22
 * @Version 1.0
 **/
public enum FieldTypeEnum {
    TEXT(10, "单行文本"),
    MULTILINE_TEXT(20, "多行文本"),
    DROP_RADIO_LIST(30, "下拉单选列表"),
    DROP_MULTIPLE_SELECTION_LIST(40, "下拉多选列表"),
    RADIO(50, "单选框"),
    CHECKBOX(60, "多选框"),
    DATE(70, "日期"),
    TIME(80, "时间"),
    NUMBER(90, "纯数字 "),
    DECIMAL(100, "小数"),
    EMAIL(110, "邮箱");
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

    /**
     * 获取所有状态
     *
     * @return
     */
    public static List<Integer> listFieldType() {
        return Arrays.stream(FieldTypeEnum.values())
                .map(nodeTypeEnum -> nodeTypeEnum.getCode()).collect(Collectors.toList());
    }
}
