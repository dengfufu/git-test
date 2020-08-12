package com.zjft.usp.anyfix.work.finish.enums;

/**
 * 签名状态枚举类
 *
 * @author zrlin
 * @version 1.0
 * @date 2020/05/20 10:58
 **/
public enum SignatureStatusEnum {

    PASS("1", "通过"),
    REFUSE("2", "拒绝签名"),
    FAIL("3", "补签");
    /**
     * 状态编码
     **/
    private String code;
    /**
     * 状态名称
     **/
    private String name;

    SignatureStatusEnum(String code, String name) {
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

    public static String getNameByCode(String code) {
        for (SignatureStatusEnum statusEnum : SignatureStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getName();
            }
        }
        return null;
    }

}
