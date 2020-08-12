package com.zjft.usp.anyfix.work.fee.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 工单支出费用类型枚举类
 * </p>
 *
 * @author canlei
 * @since 2020-05-29
 */
public enum ImplementTypeEnum {

    SUBURB_TRAFFIC_EXPENSE(1, "郊区交通费"),
    LONG_TRAFFIC_EXPENSE(2, "长途交通费"),
    HOTEL_EXPENSE(3, "住宿费"),
    TRAVEL_EXPENSE(4, "出差补助"),
    POST_EXPENSE(5, "邮寄费");

    private Integer code;
    private String name;

    ImplementTypeEnum(Integer code, String name) {
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
        for (ImplementTypeEnum s : EnumSet.allOf(ImplementTypeEnum.class)) {
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
