package com.zjft.usp.anyfix.settle.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 结算单状态枚举类
 *
 * @author canlei
 * @since 2019-09-25
 */
public enum SettleStatusEnum {

    UNCHECKED(1, "待确认"),
    CHECK_PASS(2, "已通过"),
    CHECK_REFUSE(3, "不通过");

    private Integer code;
    private String name;

    SettleStatusEnum(Integer code, String name) {
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
        for (SettleStatusEnum s : EnumSet.allOf(SettleStatusEnum.class)) {
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
