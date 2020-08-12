package com.zjft.usp.anyfix.goods.enums;

import com.zjft.usp.common.utils.IntUtil;

/**
 * 签收状态枚举类
 *
 * @author zgpi
 * @date 2020/4/21 10:05
 **/
public enum SignStatusEnum {

    NOT_SIGN(1, "未签收"),
    PART_SIGN(2, "部分签收"),
    ALL_SIGN(3, "全部签收");

    /**
     * 状态编码
     **/
    private int code;
    /**
     * 状态名称
     **/
    private String name;

    SignStatusEnum(int code, String name) {
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

    public static String getNameByCode(Integer code) {
        if (IntUtil.isZero(code)) {
            return null;
        }
        for (SignStatusEnum signStatusEnum : SignStatusEnum.values()) {
            if (code == signStatusEnum.getCode()) {
                return signStatusEnum.getName();
            }
        }
        return null;
    }
}
