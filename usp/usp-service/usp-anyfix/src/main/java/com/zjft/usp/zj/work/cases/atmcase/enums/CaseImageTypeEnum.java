package com.zjft.usp.zj.work.cases.atmcase.enums;

/**
 * CASE图片枚举类
 * @author jfzou
 */
public enum CaseImageTypeEnum {

    SIGN(10, "人脸签到图片");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    CaseImageTypeEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
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
     * @param code
     * @return
     */
    public static String getNameByCode(Integer code) {
        for (CaseImageTypeEnum imageTypeEnum : CaseImageTypeEnum.values()) {
            if (imageTypeEnum.getCode() == code) {
                return imageTypeEnum.getName();
            }
        }
        return null;
    }

}
