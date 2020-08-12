package com.zjft.usp.wms.business.income.enums;

import com.zjft.usp.common.utils.JsonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 入库状态枚举类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-14 20:04
 **/

public enum IncomeStatusEnum {
    NO_INCOME(10, "待入库"),
    HAD_INCOME(20, "已入库");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    IncomeStatusEnum(int code, String name) {
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
        for (IncomeStatusEnum typeEnum : IncomeStatusEnum.values()) {
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
    public static List<Integer> listIncomeStatus() {
        return Arrays.stream(IncomeStatusEnum.values())
                .map(incomeStatusEnum -> incomeStatusEnum.getCode()).collect(Collectors.toList());
    }
}
