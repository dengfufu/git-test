package com.zjft.usp.anyfix.work.fee.enums;

/**
 * 快递方式枚举类
 *
 * @author canlei
 * @date 2020/1/12
 */
public enum PostWayEnum {

    EXPRESS(1, "快递"),
    LOGISTICS(2, "物流"),
    CONSIGN(3, "托运");

    /**
     * 邮寄方式代码
     */
    private int code;

    /**
     * 邮寄方式名称
     */
    private String name;

    PostWayEnum(int code, String name) {
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
     * @author canlei
     * @date 2019/01/07 10:15 上午
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (PostWayEnum postWayEnum : PostWayEnum.values()) {
            if (code == postWayEnum.getCode()) {
                return postWayEnum.getName();
            }
        }
        return null;
    }
}
