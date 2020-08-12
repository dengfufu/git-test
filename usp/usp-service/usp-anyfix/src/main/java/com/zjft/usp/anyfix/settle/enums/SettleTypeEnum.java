package com.zjft.usp.anyfix.settle.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 委托商结算方式枚举类
 *
 * @author canlei
 * @version 1.0
 * @date 2020-07-06 09:45
 **/
public enum SettleTypeEnum {

    PER_WORK(1, "按单结算"),
    PER_MONTH(2, "按月结算"),
    PER_SEASON(3, "按季度结算");

    private Integer code;
    private String name;

    SettleTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * code和name的映射
     */
    public static final Map<Integer, String> map = new HashMap<>();

    static {
        for (SettleTypeEnum s : EnumSet.allOf(SettleTypeEnum.class)) {
            map.put(s.getCode(), s.getName());
        }
    }

    /**
     * 查询code对应的name
     *
     * @param code
     * @return
     */
    public static String lookup(Integer code) {
        return map.get(code);
    }

}
