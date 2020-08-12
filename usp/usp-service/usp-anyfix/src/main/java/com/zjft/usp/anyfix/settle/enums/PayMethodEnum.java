package com.zjft.usp.anyfix.settle.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 付款方式枚举类
 *
 * @author canlei
 * @since 2020-04-28
 */
public enum PayMethodEnum {

    ONLINE(1, "在线支付"),
    OFFLINE(2, "线下支付");

    private Integer code;
    private String name;

    PayMethodEnum(Integer code, String name) {
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
        for (PayMethodEnum s : EnumSet.allOf(PayMethodEnum.class)) {
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
