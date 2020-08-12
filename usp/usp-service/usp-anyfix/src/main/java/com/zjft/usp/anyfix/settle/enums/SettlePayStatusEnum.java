package com.zjft.usp.anyfix.settle.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 结算单付款状态枚举类
 *
 * @author canlei
 * @since 2020-04-28
 */
public enum SettlePayStatusEnum {

    TO_PAY(1, "待付款"),
    TO_RECEIPT(2, "已付款"),
    RECEIPTED(3, "已收款");

    private Integer code;
    private String name;

    SettlePayStatusEnum(Integer code, String name) {
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
        for (SettlePayStatusEnum s : EnumSet.allOf(SettlePayStatusEnum.class)) {
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

    /**
     * 获得收款状态名称
     *
     * @param code
     * @return
     * @author zgpi
     * @date 2020/5/6 10:27
     **/
    public static String getReceiptName(Integer code) {
        if (RECEIPTED.getCode().equals(code)) {
            return "已收款";
        } else {
            return "未收款";
        }
    }

}
