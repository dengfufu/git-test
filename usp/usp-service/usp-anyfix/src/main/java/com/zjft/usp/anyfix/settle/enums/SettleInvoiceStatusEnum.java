package com.zjft.usp.anyfix.settle.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 结算单状态枚举类
 *
 * @author canlei
 * @since 2020-04-28
 */
public enum SettleInvoiceStatusEnum {

    TO_INVOICE(1, "未开票"),
    INVOICED(2, "已开票");

    private Integer code;
    private String name;

    SettleInvoiceStatusEnum(Integer code, String name) {
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
        for (SettleInvoiceStatusEnum s : EnumSet.allOf(SettleInvoiceStatusEnum.class)) {
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
