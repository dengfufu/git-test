package com.zjft.usp.anyfix.work.chat.enums;

/**
 * 应用枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/12 18:43
 */
public enum OperateTypeEnum {

    RECALL(1,"撤销"),
    BLOCK(2,"屏蔽"),
    DELETE(3, "删除");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    OperateTypeEnum(int code, String name) {
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
     **/
    public static String getNameByCode(Integer code) {
        for (OperateTypeEnum operatorTypeEnum : OperateTypeEnum.values()) {
            if (code == operatorTypeEnum.getCode()) {
                return operatorTypeEnum.getName();
            }
        }
        return null;
    }

}
