package com.zjft.usp.anyfix.baseinfo.enums;


/**
 * @author zrlin
 * @date 2019/10/16 10:56
 *
 **/
public enum ServiceReasonEnum {
    /**
     * 客服退单
     */
    SERVICE_CANCEL_ORDER(1, "客服退单"),

    /**
     *客服撤回派单
     */
    SERVICE_REVOKE_DISTRIBUTION(2,"客服撤回派单"),

    /**
     *工程师拒绝派单
     */
    ENGINEER_REFUSE_DISTRIBUTION(3,"工程师拒绝派单"),

    /**
     *工程师退回派单
     */
    ENGINEER_REVOKE_DISTRIBUTION(4,"工程师退回派单");

    public final int code;
    public final String name;

    ServiceReasonEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }



    public String getName() {
        return name;
    }


}
