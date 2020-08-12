package com.zjft.usp.anyfix.work.finish.enums;

/**
 * 签名状态枚举类
 *
 * @author zrlin
 * @version 1.0
 * @date 2020/05/20 10:58
 **/
public enum FilesStatusEnum {

    PASS("1", "通过"),
    FAIL("2", "不通过");
    /**
     * 状态编码
     **/
    private String code;
    /**
     * 状态名称
     **/
    private String name;

    FilesStatusEnum(String code, String name) {
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
        for (FilesStatusEnum statusEnum : FilesStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getName();
            }
        }
        return null;
    }

}
