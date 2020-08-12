package com.zjft.usp.wms.business.trans.enums;

/**
 * 调拨状态枚举类
 *
 * @author zrlin
 * @date 2019/11/23 9:22
 * @Version 1.0
 **/
public enum CountEnum {

    END(100, "完成"),
    ALL_COUNT(200,"全部数量");
    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    CountEnum(int code, String name) {
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
     * @date 2019/12/23 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (CountEnum typeEnum : CountEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
