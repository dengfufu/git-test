package com.zjft.usp.anyfix.work.check.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 费用确认状态枚举类
 *
 * @author zgpi
 * @date 2020/5/29 17:25
 */
public enum FeeConfirmStatusEnum {

    UN_CONFIRM(1, "待确认"),
    CONFIRM_PASS(2, "确认通过"),
    CONFIRM_REFUSE(3, "确认不通过");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    FeeConfirmStatusEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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
     * @author zgpi
     * @date 2020/5/11 17:11
     **/
    public static String getNameByCode(Integer code) {
        for (FeeConfirmStatusEnum statusEnum : FeeConfirmStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getName();
            }
        }
        return null;
    }

    public static List<Integer> listFeeConfirmStatus() {
        List<Integer> list = new ArrayList<>();
        for (FeeConfirmStatusEnum statusEnum : FeeConfirmStatusEnum.values()) {
            list.add(statusEnum.getCode());
        }
        return list;
    }
}
