package com.zjft.usp.wms.business.outcome.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 出库状态枚举类
 *
 * @author zphu
 * @version 1.0
 * @date 2019-11-21 20:04
 **/

public enum OutcomeStatusEnum {
    NO_OUTCOME(10, "待出库"),
    HAD_OUTCOME(20, "已出库");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    OutcomeStatusEnum(int code, String name) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据code获得name
     *
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (OutcomeStatusEnum typeEnum : OutcomeStatusEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }

    /**
     * 获取所有状态
     *
     * @return
     */
    public static List<Integer> listOutcomeStatus() {
        return Arrays.stream(OutcomeStatusEnum.values())
                .map(outcomeStatusEnum -> outcomeStatusEnum.getCode()).collect(Collectors.toList());
    }
}
