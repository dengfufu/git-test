package com.zjft.usp.anyfix.baseinfo.enums;


/**
 * 工单类型
 *
 * @author zphu
 * @date 2019/9/27 9:22
 * @Version 1.0
 **/
public enum WorkSysTypeEnum {
    MAINTAIN(1, "维护"),
    PATROL(2, "巡检"),
    INSTALL(3, "安装"),
    CONSULT(4, "咨询"),
    COMPLAINED(5, "投诉"),
    TRAIN(7, "培训"),
    DELIVER(8, "递送"),
    OTHER(99, "其他");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    WorkSysTypeEnum(int code, String name) {
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
        for (WorkSysTypeEnum typeEnum : WorkSysTypeEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }

}
