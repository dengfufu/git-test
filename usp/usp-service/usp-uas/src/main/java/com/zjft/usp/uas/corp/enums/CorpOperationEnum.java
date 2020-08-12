package com.zjft.usp.uas.corp.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业操作类别
 * @author canlei
 * @date 2019-08-14
 * @version 1.0
 */
public enum CorpOperationEnum {

    /**
     * 企业注册
     */
    CO_REGISTRY((short)1, "企业注册"),
    /**
     * 企业认证申请
     */
    CO_VERIFY_APPLY((short)2, "企业认证申请"),
    /**
     * 企业认证审核
     */
    CO_VERIFY_CHECK((short)3, "企业认证审核"),
    /**
     * 加入企业申请
     */
    CO_USER_APPLY((short)4, "加入企业申请"),
    /**
     * 加入企业审核
     */
    CO_USER_CHECK((short)5, "加入企业审核"),
    /**
     * 设置企业管理员
     */
    CO_ADMIN((short)6, "设置企业管理员"),
    /**
     * 修改企业管理密码
     */
    CO_PASSWORD((short)7, "修改企业管理密码"),
    /**
     * 修改企业注册信息
     */
    CO_UPDATE_REGISTRY((short)7, "修改企业注册信息"),
    /**
     * 绑定企业支付宝
     */
    CO_ALIPAY((short)8, "绑定企业支付宝");

    private final short type;
    private final String name;

    CorpOperationEnum(short type, String name){
        this.type = type;
        this.name = name;
    }

    public short getType(){
        return type;
    }

    public String getName(){
        return name;
    }

    /**
     * type和name的映射
     */
    public static final Map<Short, String> map = new HashMap<>();

    static {
        for (CorpOperationEnum s : EnumSet.allOf(CorpOperationEnum.class)) {
            map.put(s.getType(), s.getName());
        }
    }

    /**
     * 查询type对应的name
     * @param type
     * @return
     */
    public static String lookup(short type) {
        return map.get(type);
    }

}
