package com.zjft.usp.wms.flow.enums;

import com.zjft.usp.wms.business.outcome.enums.OutcomeStatusEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 节点类型枚举类
 *
 * @author jfzou
 * @date 2019/11/08 9:22
 * @Version 1.0
 **/
public enum NodeTypeEnum {
    FILL_IN(10, "填写节点"),
    COMMON_APPROVAL(20, "普通审批节点"),
    COUNTERSIGN_APPROVAL(30, "会签审批节点"),
    DELIVERY(40, "发货节点"),
    RECEIVE(50, "收货节点"),
    CONFIRM(60, "确认节点");
    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    NodeTypeEnum(int code, String name) {
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
        for (NodeTypeEnum typeEnum : NodeTypeEnum.values()) {
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
        return Arrays.stream(NodeTypeEnum.values())
                .map(nodeTypeEnum -> nodeTypeEnum.getCode()).collect(Collectors.toList());
    }
}
