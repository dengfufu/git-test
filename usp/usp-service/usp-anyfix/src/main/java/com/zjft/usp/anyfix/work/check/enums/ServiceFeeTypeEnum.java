package com.zjft.usp.anyfix.work.check.enums;

/**
 * 服务费用类型枚举类
 *
 * @author zgpi
 * @date 2020/5/16 11:33
 **/
public enum ServiceFeeTypeEnum {

    SERVICE(1, "服务"),
    FEE(2, "费用"),
    SERVICE_FEE(3, "服务和费用");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    ServiceFeeTypeEnum(Integer code, String name) {
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
     **/
    public static String getNameByCode(Integer code) {
        for (ServiceFeeTypeEnum typeEnum : ServiceFeeTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getName();
            }
        }
        return null;
    }

}
