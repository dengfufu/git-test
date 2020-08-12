package com.zjft.usp.anyfix.work.fee.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 对账单结算状态
 *
 * @author canlei
 * @date 2020/5/12
 */
public enum WorkFeeVerifySettleStatusEnum {

    TO_SETTLE(1, "待结算"),
    IN_SETTLE(2, "结算中"),
    SETTLED(3, "已结算");

    private Integer code;
    private String name;

    WorkFeeVerifySettleStatusEnum(Integer code, String name) {
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
        for (WorkFeeVerifySettleStatusEnum s : EnumSet.allOf(WorkFeeVerifySettleStatusEnum.class)) {
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
