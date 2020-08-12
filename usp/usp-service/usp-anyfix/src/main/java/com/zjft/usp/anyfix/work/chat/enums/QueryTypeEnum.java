package com.zjft.usp.anyfix.work.chat.enums;

/**
 * 应用枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/12 18:43
 */
public enum QueryTypeEnum {

    QUERY_MORE(1,"顺序号往前查询"),
    QUERY_LATEST(2, "顺序号往后查询");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    QueryTypeEnum(int code, String name) {
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
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (QueryTypeEnum querTypeEnum : QueryTypeEnum.values()) {
            if (code == querTypeEnum.getCode()) {
                return querTypeEnum.getName();
            }
        }
        return null;
    }

}
