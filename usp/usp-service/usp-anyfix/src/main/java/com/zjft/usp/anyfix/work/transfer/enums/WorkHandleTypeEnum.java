package com.zjft.usp.anyfix.work.transfer.enums;

/**
 * 受理方式枚举类
 * @author zgpi
 * @version 1.0
 * @date 2019/11/14 18:30
 */
public enum WorkHandleTypeEnum {

    AUTO(1, "自动受理"),
    MANUAL(2, "手动受理");

    /**
     * 编码
     **/
    private int code;
    /**
     * 名称
     **/
    private String name;

    WorkHandleTypeEnum(int code, String name) {
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
     * @author zgpi
     * @date 2019/11/14 18:30
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (WorkHandleTypeEnum workHandleTypeEnum : WorkHandleTypeEnum.values()) {
            if (code == workHandleTypeEnum.getCode()) {
                return workHandleTypeEnum.getName();
            }
        }
        return null;
    }
}
