package com.zjft.usp.wms.flow.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库存类型枚举类
 *
 * @author jfzou
 * @date 2019/12/26 9:22
 * @Version 1.0
 **/
public enum StockTypeEnum {
    STOCK_DEPOT(10, "库房库存"),
    STOCK_PERSONAL(20, "个人库存");
    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    StockTypeEnum(int code, String name) {
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
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (StockTypeEnum typeEnum : StockTypeEnum.values()) {
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
    public static List<Integer> listNodeType() {
        return Arrays.stream(StockTypeEnum.values())
                .map(nodeTypeEnum -> nodeTypeEnum.getCode()).collect(Collectors.toList());
    }
}
