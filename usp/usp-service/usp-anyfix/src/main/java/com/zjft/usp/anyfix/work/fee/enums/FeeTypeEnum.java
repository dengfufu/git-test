package com.zjft.usp.anyfix.work.fee.enums;

/**
 * 费用类型枚举类,适用于费用明细
 *
 * @author canlei
 * @date 2020/4/21
 */
public enum FeeTypeEnum {

    ASSORT(1, "分类费用"),
    IMPLEMENT(2, "实施发生费用");

    /**
     * 费用类型代码
     */
    private Integer code;

    /**
     * 费用类型名称
     */
    private String name;

    FeeTypeEnum(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public Integer getCode() {
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
     * @date 2020/04/21 10:15 上午
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (FeeTypeEnum feeTypeEnum : FeeTypeEnum.values()) {
            if (code.equals(feeTypeEnum.getCode())) {
                return feeTypeEnum.getName();
            }
        }
        return null;
    }

}
