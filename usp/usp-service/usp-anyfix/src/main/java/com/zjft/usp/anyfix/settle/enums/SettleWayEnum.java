package com.zjft.usp.anyfix.settle.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 结算单结算方式枚举类
 *
 * @author canlei
 * @version 1.0
 * @date 2020-07-06 09:45
 **/
public enum SettleWayEnum {

    SETTLE_PERIOD(1, "按周期结算"),
    SETTLE_WORK(2, "按单结算");

    private Integer code;
    private String name;

    SettleWayEnum(Integer code, String name) {
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
        for (SettleWayEnum s : EnumSet.allOf(SettleWayEnum.class)) {
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
