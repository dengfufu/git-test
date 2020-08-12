package com.zjft.usp.uas.user.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zphu
 * @date 2019/8/15 15:15
 * @Version 1.0
 **/

public enum UserOperEnum {

    UO_REGISTER_USER((short) 1, "用户注册"),
    UO_UPDATE_NICKNAME((short) 2, "修改昵称"),
    UO_UPDATE_LOGINID((short) 3, "修改登录名"),
    UO_UPDATE_MOBILE((short) 4, "修改手机号"),
    UO_IDCARD_VERIFY((short) 5, "实名认证"),

    UO_SET_SIGNATURE((short) 6, "设置签名"),
    UO_SET_EMAIL((short) 7, "设置邮箱"),
    UO_SET_REGION((short) 8, "设置地区"),
    UO_INSERT_ADDRESS((short) 9, "添加地址"),
    UO_UPDATE_ADDRESS((short) 10, "修改地址"),
    UO_DELETE_ADDRESS((short) 11, "删除地址"),
    UO_UPDATE_SEX((short) 12, "修改性别"),
    UO_SET_PASSWORD((short) 13, "设置密码"),
    UO_UPDATE_PASSWORD((short) 14, "修改密码"),
    UO_FORGET_UPDATE_PASSWORD((short) 15, "忘记密码-修改密码"),
    ;

    private String name;
    private short index;
    private static final Map<Short, String> UO_USER_OPER_MAP = new HashMap<Short, String>();

    static {
        for (UserOperEnum uo : EnumSet.allOf(UserOperEnum.class)) {
            UO_USER_OPER_MAP.put(uo.getIndex(), uo.getName());
        }
    }

    private UserOperEnum(short index, String name) {
        this.name = name;
        this.index = index;
    }

    public static String getName(short index) {
        return (String) UO_USER_OPER_MAP.get(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getIndex() {
        return index;
    }

    public void setIndex(short index) {
        this.index = index;
    }
}
