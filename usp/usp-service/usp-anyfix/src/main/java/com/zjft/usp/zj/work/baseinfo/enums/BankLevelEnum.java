package com.zjft.usp.zj.work.baseinfo.enums;

/**
 * 银行级别枚举类
 *
 * @author zgpi
 * @date 2020/3/24 10:48
 **/
public enum BankLevelEnum {

    FIRST(1, "总行"),
    SECOND(2, "分行");

    /**
     * 代码
     */
    private Integer code;

    /**
     * 名称
     */
    private String name;

    BankLevelEnum(Integer code, String name) {
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
     * @date 2020/3/24 10:48
     **/
    public static String getNameByCode(Integer code) {
        for (BankLevelEnum bankLevelEnum : BankLevelEnum.values()) {
            if (bankLevelEnum.getCode().equals(code)) {
                return bankLevelEnum.getName();
            }
        }
        return null;
    }
}
