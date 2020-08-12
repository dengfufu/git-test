package com.zjft.usp.common.enums;

/**
 * 微信消息模板
 *
 * @author ljzhu
 */
public enum WxTemplateEnum {

    ADD(1, "uNMPGIP7aA8cjM1dP3gNDjaJHmJIsctH7KinUqQ5FzI");

    /**
     * 模板编码
     **/
    private int code;
    /**
     * 模板value
     **/
    private String value;

    WxTemplateEnum(int code, String value) {
        this.value = value;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getvalue() {
        return value;
    }

    public void setvalue(String value) {
        this.value = value;
    }

    /**
     * 根据code获得value
     *
     * @param code
     * @return
     **/
    public static String getvalueByCode(Integer code) {
        for (WxTemplateEnum wxTemplateEnum : WxTemplateEnum.values()) {
            if (code == wxTemplateEnum.getCode()) {
                return wxTemplateEnum.getvalue();
            }
        }
        return null;
    }
}
