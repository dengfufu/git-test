package com.zjft.usp.anyfix.work.fee.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 工单对账单状态
 *
 * @author canlei
 * @date 2020/5/12
 */
public enum WorkFeeVerifyStatusEnum {

    TO_SUBMIT(1, "待提交"),
    TO_VERIFY(2, "待对账"),
    TO_CONFIRM(3, "待确认"),
    REFUSE(4, "不通过"),
    PASS(5, "已通过");

    private Integer code;
    private String name;

    WorkFeeVerifyStatusEnum(Integer code, String name) {
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
        for (WorkFeeVerifyStatusEnum s : EnumSet.allOf(WorkFeeVerifyStatusEnum.class)) {
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
