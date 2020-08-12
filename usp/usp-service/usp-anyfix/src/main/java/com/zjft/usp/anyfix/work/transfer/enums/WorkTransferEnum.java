package com.zjft.usp.anyfix.work.transfer.enums;

/**工单流转方式
 * @author zphu
 * @date 2019/9/29 14:43
 * @Version 1.0
 **/
public enum WorkTransferEnum {

    AUTO_DISPATCH(10, "自动分配"),
    MANUAL_DISPATCH(20, "客户手动分配"),
    AUTO_HANDLE(30, "自动受理"),
    MANUAL_HANDLE(40, "客服手动受理"),
    MANUAL_TURN_HANDLE(50, "客服转处理"),
    MANUAL_RETURN(60, "客服退单"),
    CUSTOM_RECALL(70, "客户撤单"),
    CUSTOM_REMINDER(80, "客户催单"),
    ASSIGN_RECALL(90, "派单撤回"),
    CLAIM_WORK(100, "工单认领"),
    REFUSE_WORK(110, "拒绝派单"),
    RETURN_WORK(120,"退回派单"),
    RETURN_ASSIGN_SERVICE(130,"派单主管退单");

    /**
     * 服务方式代码
     */
    private int code;

    /**
     * 服务方式名称
     */
    private String name;

    WorkTransferEnum(int code, String name) {
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
}
