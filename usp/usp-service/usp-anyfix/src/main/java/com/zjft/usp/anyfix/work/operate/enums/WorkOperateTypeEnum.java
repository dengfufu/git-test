package com.zjft.usp.anyfix.work.operate.enums;

/**
 * 工单处理类型枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/24 8:55 上午
 **/
public enum WorkOperateTypeEnum {

    CREATE(10, "创建工单"),
    MOD(15, "修改工单"),
    RESUBMIT(17,"重新提交工单"),
    AUTO_DISPATCH(20, "自动提单"),
    MANUAL_DISPATCH(30, "客户手动提单"),
    AUTO_DISPATCH_BRANCH(40,"自动分配服务网点"),
    AUTO_HANDLE(50, "自动分配"),
    MANUAL_HANDLE(60, "客服手动分配"),
    TURN_HANDLE(70,"客服转处理"),
    AUTO_ASSIGN(80,"自动派单"),
    RETURN_ASSIGN_SERVICE(81,"服务网点退回工单"),
    ASSIGN(90, "派单"),
    RECALL_ASSIGN(100,"撤回派单"),
    CLAIM(110, "工程师接单"),
    REFUSE_ASSIGN(120,"工程师拒绝派单"),
    RETURN_ASSIGN(130,"工程师退回派单"),
    CHANGE_BOOKING_TIME(140,"修改预约时间"),
    SIGN(150, "到达现场"),
    LOCATE_SERVICE(160,"处理完成"),
    REMOTE_SERVICE(170, "处理完成"),
    EVALUATE(180, "客户评价"),
    CUSTOM_RECALL(190, "客户撤单"),
    CUSTOM_REMINDER(200, "客户催单"),
    SERVICE_RETURN(210,"客服退单"),
    SERVICE_CHECK(220, "服务商审核"),
    SERVICE_CHECK_FINISH(222, "服务商审核服务内容"),
    SERVICE_BATCH_CHECK_FINISH(223, "服务商批量审核服务"),
    SERVICE_CHECK_FEE(224, "服务商审核费用"),
    SERVICE_BATCH_CHECK_FEE(225, "服务商批量审核费用"),
    DEMANDER_CHECK(230, "委托商确认"),
    DEMANDER_CONFIRM_FINISH(232, "委托商确认服务"),
    DEMANDER_BATCH_CONFIRM_FINISH(233, "委托商批量确认服务"),
    DEMANDER_CONFIRM_FEE(234, "委托商确认费用"),
    DEMANDER_BATCH_CONFIRM_FEE(235, "委托商批量确认费用"),
    AUTO_CONFIRM_FINISH(236, "自动确认服务"),
    AUTO_CONFIRM_FEE(237, "自动确认费用"),
    MOD_LOCALE_FINISH(240, "修改服务完成信息"),
    CHANGE_PLAN_FINISH_TIME(250,"修改预计完成时间"),
    MOD_WORK_FEE(260,"修改工单费用"),
    MOD_FILES(270,"补传附件");


    /**
     * 类型编码
     **/
    private int code;
    /**
     * 类型名称
     **/
    private String name;

    WorkOperateTypeEnum(int code, String name) {
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
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     * @param code
     * @return
     **/
    public static String getNameByCode(Integer code) {
        for (WorkOperateTypeEnum workOperateTypeEnum : WorkOperateTypeEnum.values()) {
            if (code == workOperateTypeEnum.getCode()) {
                return workOperateTypeEnum.getName();
            }
        }
        return null;
    }
}
