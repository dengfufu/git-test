package com.zjft.usp.anyfix.settle.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 工单结算状态枚举类
 *
 * @author canlei
 * @since 2020-04-28
 */
public enum WorkSettleStatusEnum {

    TO_VERIFY(1, "待对账"),
    IN_VERIFY(2, "对账中"),
    TO_SETTLE(3, "待结算"),
    IN_SETTLE(4, "结算中"),
    SETTLED(5, "已结算");

    private Integer code;
    private String name;

    WorkSettleStatusEnum(Integer code, String name) {
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
        for (WorkSettleStatusEnum s : EnumSet.allOf(WorkSettleStatusEnum.class)) {
            map.put(s.getCode(), s.getName());
        }
    }

    /**
     * 查询code对应的name
     * @param code
     * @return
     */
    public static String lookup(Integer code) {
        return map.get(code);
    }

}

