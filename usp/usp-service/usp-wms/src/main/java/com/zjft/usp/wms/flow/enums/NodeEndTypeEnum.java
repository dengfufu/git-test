package com.zjft.usp.wms.flow.enums;

/**
 * 节点结束类型枚举类
 *
 * @author jfzou
 * @date 2019/11/06 9:22
 * @Version 1.0
 **/
public enum NodeEndTypeEnum {
    PASS(10, "通过"),
    RETURN_PREVIOUS_NODE(20, "不通过，退回上一步"),
    RETURN_START(30, "不通过，退回申请"),
    NOT_PASS(40, "不通过，流程结束");
    /**不通过，退回指定步骤，后续扩展使用*/
    //RETURN_ASSIGN_NODE(50, "不通过，退回指定步骤");

    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    NodeEndTypeEnum(int code, String name) {
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
     * @author jfzou
     * @date 2019/11/07 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (NodeEndTypeEnum typeEnum : NodeEndTypeEnum.values()) {
            if (code == typeEnum.getCode()) {
                return typeEnum.getName();
            }
        }
        return null;
    }
}
