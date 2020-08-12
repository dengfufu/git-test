package com.zjft.usp.wms.business.consign.enums;

import com.zjft.usp.wms.baseinfo.enums.DepotUserEnum;

/**
 * 运送方式
 *
 * @author zphu
 * @date 2019/12/16 9:22
 * @Version 1.0
 **/
public enum TransportTypeEnum {
    BUS(10, "自提"),
    TRAIN(20, "托运"),
    OTHER(30, "快递");
    /**
     * 类型编码
     **/
    private long code;
    /**
     * 类型名称
     **/
    private String name;

    TransportTypeEnum(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public long getCode() {
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
    public static String getNameByCode(long code) {
        for (TransportTypeEnum typeEnum : TransportTypeEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
