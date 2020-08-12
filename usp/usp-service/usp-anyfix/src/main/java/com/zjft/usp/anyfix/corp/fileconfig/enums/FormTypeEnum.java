package com.zjft.usp.anyfix.corp.fileconfig.enums;

/**
 * 是否可用枚举类
 *
 * @author zgpi
 * @date 2019/10/16 3:10 下午
 **/
public enum FormTypeEnum {

    SERVICE_FINISH(1, "服务完成");

    /**
     * 代码
     */
    private int code;

    /**
     * 名称
     */
    private String name;

    FormTypeEnum(int code, String name) {
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
     * @param code
     * @return
     */
    public static String getNameByCode(Integer code) {
        for (FormTypeEnum typeEnum : FormTypeEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
