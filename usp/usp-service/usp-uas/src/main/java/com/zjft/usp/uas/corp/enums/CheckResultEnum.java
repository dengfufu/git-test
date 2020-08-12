package com.zjft.usp.uas.corp.enums;

public enum CheckResultEnum {

    CHECK_PASS("Y", "审核通过"),
    CHECK_REFUSE("N", "审核不通过");

    private String code;
    private String name;

    CheckResultEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
