package com.zjft.usp.anyfix.work.request.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 维保方式枚举类
 *
 * @author canlei
 * @version 1.0
 * @date 2020/2/11 8:55 上午
 **/
public enum WarrantyModeEnum {

    WHOLE_MACHINE_WARRANTY(10, "整机保"),
    SINGLE_WORK_WARRANTY(20, "单次保"),
    MAN_DAY(30, "人天保");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    WarrantyModeEnum(Integer code, String name) {
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
     * @author canlei
     * @date 2020/2/11 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (WarrantyModeEnum warrantyModeEnum : WarrantyModeEnum.values()) {
            if (warrantyModeEnum.getCode().equals(code)) {
                return warrantyModeEnum.getName();
            }
        }
        return null;
    }

    /**
     * 获取可结算维保方式列表
     *
     * @return
     */
    public static List<Integer> listSettleWarrantyStatus() {
        List<Integer> list = new ArrayList<>();
        list.add(WarrantyModeEnum.SINGLE_WORK_WARRANTY.getCode());
        list.add(WarrantyModeEnum.MAN_DAY.getCode());
        return list;
    }

}
