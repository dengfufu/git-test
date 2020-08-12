package com.zjft.usp.common.enums;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限范围类型枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/3/11 16:11
 */
public enum RightScopeTypeEnum {

    SERVICE_BRANCH(10, "服务网点"),
    PROVINCE(20, "省份"),
    DEMANDER(30, "委托商");

    /**
     * 类型编码
     **/
    private Integer code;
    /**
     * 类型名称
     **/
    private String name;

    RightScopeTypeEnum(Integer code, String name) {
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
     **/
    public static String getNameByCode(Integer code) {
        for (RightScopeTypeEnum rightScopeTypeEnum : RightScopeTypeEnum.values()) {
            if (rightScopeTypeEnum.getCode().equals(code)) {
                return rightScopeTypeEnum.getName();
            }
        }
        return null;
    }

    /**
     * 获取所有范围类型
     *
     * @return
     */
    public static List<Integer> listScopeType() {
        return Arrays.stream(RightScopeTypeEnum.values())
                .map(rightScopeTypeEnum -> rightScopeTypeEnum.getCode()).collect(Collectors.toList());
    }
}
