package com.zjft.usp.anyfix.corp.branch.enums;

public enum ServiceBranchTypeEnum {

    OWN_BRANCH(1, "自有网点"),
    THIRD_PARTY_BRANCH(2, "第三方网点");

    private int code;

    private String name;

    ServiceBranchTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
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

}
