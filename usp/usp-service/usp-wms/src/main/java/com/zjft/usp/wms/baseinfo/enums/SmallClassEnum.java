package com.zjft.usp.wms.baseinfo.enums;

/**
 * 业务小类枚举类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-12 13:54
 **/
public enum SmallClassEnum {
    INCOME_CORP_PURCHASE(10, "公司采购入库"),
    INCOME_VENDOR_WARE(20, "厂商物料入库"),
    INCOME_EXIST_WARE(30, "现有物料入库"),
    INCOME_VENDOR_RETURN(40, "厂商返还入库"),
    INCOME_SALE_LOAN_RETURN(50, "销售借用归还入库"),
    INCOME_BORROW_RETURN(60, "领用退料入库"),

    OUTCOME_SALE_BORROW(70, "销售借用出库"),
    OUTCOME_CORP_SALE(80, "公司销售出库"),
    OUTCOME_RETURN_VENDOR(90, "归还厂商出库"),
    OUTCOME_BORROW_WARE(100, "物料领用出库"),

    TRANS_WARE_TRANSFER(110, "物料库存调拨"),
    TRANS_WARE_SHIFT(120, "物料快速转库"),
    TRANS_WARE_BAD_RETURN(130, "待修物料返还");

    /**
     * 类型编码
     */
    private int code;
    /**
     * 类型名称
     */
    private String name;

    SmallClassEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code获得name
     *
     * @param code
     * @return String
     **/
    public static String getNameByCode(Integer code) {
        for (SmallClassEnum typeEnum : SmallClassEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
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
}
