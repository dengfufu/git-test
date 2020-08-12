package com.zjft.usp.wms.flow.enums;

/**
 * 流程是否已结束枚举类
 *
 * @author jfzou
 * @date 2019/10/16 3:10 下午
 **/
public enum FlowEndEnum {

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

    FlowEndEnum(String code, String name) {
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
        for (FlowEndEnum enabledEnum : FlowEndEnum.values()) {
            if (enabledEnum.getCode().equals(code)) {
                return enabledEnum.getName();
            }
        }
        return null;
    }
}
