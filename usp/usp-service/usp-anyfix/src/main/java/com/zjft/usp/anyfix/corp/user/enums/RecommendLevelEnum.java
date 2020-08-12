package com.zjft.usp.anyfix.corp.user.enums;

/**
 * 推荐级别枚举类
 *
 * @author zgpi
 * @date 2019/10/12 2:22 下午
 **/
public enum RecommendLevelEnum {

    STRONG_RECOMMEND(1, "强烈推荐"),
    RECOMMEND(2, "推荐"),
    CAN_ASSIGN(3, "可派单"),
    DEPRECATED_ASSIGN(4, "不建议派单"),
    CAN_NOT_ASSIGN(5, "无法派单");

    /**
     * 代码
     */
    private int code;

    /**
     * 名称
     */
    private String name;

    RecommendLevelEnum(int code, String name) {
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
     * @date 2019/10/11 10:15 上午
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (RecommendLevelEnum recommendLevelEnum : RecommendLevelEnum.values()) {
            if (code == recommendLevelEnum.getCode()) {
                return recommendLevelEnum.getName();
            }
        }
        return null;
    }
}
