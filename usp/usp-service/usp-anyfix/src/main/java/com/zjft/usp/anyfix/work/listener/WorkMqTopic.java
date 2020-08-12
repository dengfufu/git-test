package com.zjft.usp.anyfix.work.listener;

/**
 * 工单消息监听类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/10 2:13 下午
 **/
public class WorkMqTopic {

    /** 扫一扫建单主题 **/
    public static final String SCAN_CREATE_WORK = "scan-create-work";
    /** 建单主题 **/
    public static final String CREATE_WORK = "create-work";
    /** 修改工单主题 **/
    public static final String MOD_WORK = "mod-work";
    /** 客户手工分配工单主题 **/
    public static final String CUSTOM_DISPATCH_WORK = "custom-dispatch-work";
    /** 系统自动分配工单主题 **/
    public static final String AUTO_DISPATCH_WORK = "auto-dispatch-work";
    /** 系统自动分配服务网点主题 **/
    public static final String AUTO_DISPATCH_SERVICE_BRANCH = "auto-dispatch-service-branch";
    /** 客服受理主题 **/
    public static final String SERVICE_HANDLE_WORK = "service-handle-work";
    /** 客服转处理主题 **/
    public static final String SERVICE_TURN_HANDLE_WORK = "service-turn-handle-work";
    /** 派单主题 **/
    public static final String ASSIGN_WORK = "assign-work";
    /** 退单主题 **/
    public static final String RETURN_WORK = "return-work";
    /** 沟通记录通知主题 **/
    public static final String WORK_CHAT = "work-chat";
    /** 工单预警处理主题 **/
    public static final String WORK_REMIND_DEAL = "work-remind-deal";
}
