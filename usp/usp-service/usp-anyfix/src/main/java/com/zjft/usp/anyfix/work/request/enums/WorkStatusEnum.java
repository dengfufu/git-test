package com.zjft.usp.anyfix.work.request.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单状态枚举类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/24 8:55 上午
 **/
public enum WorkStatusEnum {

    TO_DISPATCH(10, "待提单"),
    TO_HANDLE(20, "待分配"),
    TO_ASSIGN(30, "待派单"),
    TO_CLAIM(40, "待接单"),
    TO_SIGN(50, "待签到"),
    TO_SERVICE(60, "待服务"),
    IN_SERVICE(70, "服务中"),
    TO_EVALUATE(80, "待评价"),
    CLOSED(90, "已完成"),
    RETURNED(100, "已退单"),
    CANCELED(110, "已撤单");

    /**
     * 状态编码
     **/
    private Integer code;
    /**
     * 状态名称
     **/
    private String name;

    WorkStatusEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
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
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     **/
    public static String getNameByCode(Integer code) {
        for (WorkStatusEnum statusEnum : WorkStatusEnum.values()) {
            if (statusEnum.getCode() == code) {
                return statusEnum.getName();
            }
        }
        return null;
    }

    /**
     * 运行中的状态列表，不包含待评价
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/10/15 10:20 上午
     **/
    public static List<Integer> getRunningStatus() {
        List<Integer> list = new ArrayList<>();
        list.add(WorkStatusEnum.TO_DISPATCH.getCode());
        list.add(WorkStatusEnum.TO_HANDLE.getCode());
        list.add(WorkStatusEnum.TO_ASSIGN.getCode());
        list.add(WorkStatusEnum.TO_CLAIM.getCode());
        list.add(WorkStatusEnum.TO_SIGN.getCode());
        list.add(WorkStatusEnum.TO_SERVICE.getCode());
        list.add(WorkStatusEnum.IN_SERVICE.getCode());
        return list;
    }

    /**
     * 可结算的工单状态，包含待评价、已完成
     *
     * @return
     */
    public static List<Integer> getSettleStatusList() {
        List<Integer> list = new ArrayList<>();
        list.add(WorkStatusEnum.TO_EVALUATE.getCode());
        list.add(WorkStatusEnum.CLOSED.getCode());
        return list;
    }

    /**
     * 可确认的工单状态，包含待评价、已完成、已退单
     *
     * @return
     */
    public static List<Integer> getDemanderConfirmStatusList() {
        List<Integer> list = new ArrayList<>();
        list.add(WorkStatusEnum.TO_EVALUATE.getCode());
        list.add(WorkStatusEnum.CLOSED.getCode());
        list.add(WorkStatusEnum.RETURNED.getCode());
        return list;
    }

    /**
     * 可在OA系统中进行报销的正常结束工单状态，包含已完成、待评价
     * @return
     */
    public static List<Integer> getCostNormalCloseList() {
        List<Integer> list = new ArrayList<>();
        list.add(WorkStatusEnum.CLOSED.getCode());
        list.add(WorkStatusEnum.TO_EVALUATE.getCode());
        return list;
    }

    /**
     * 可在ATM系统中进行绩效考核的正常结束工单状态，包含已完成、待评价
     * @return
     */
    public static List<Integer> getAppraiseNormalCloseList() {
        List<Integer> list = new ArrayList<>();
        list.add(WorkStatusEnum.CLOSED.getCode());
        list.add(WorkStatusEnum.TO_EVALUATE.getCode());
        return list;
    }

    /**
     * 状态列表
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/10/15 10:20 上午
     **/
    public static List<Integer> getStatusList() {
        List<Integer> list = new ArrayList<>();
        list.add(WorkStatusEnum.TO_DISPATCH.getCode());
        list.add(WorkStatusEnum.TO_HANDLE.getCode());
        list.add(WorkStatusEnum.TO_ASSIGN.getCode());
        list.add(WorkStatusEnum.TO_CLAIM.getCode());
        list.add(WorkStatusEnum.TO_SIGN.getCode());
        list.add(WorkStatusEnum.TO_SERVICE.getCode());
        list.add(WorkStatusEnum.IN_SERVICE.getCode());
        list.add(WorkStatusEnum.TO_EVALUATE.getCode());
        list.add(WorkStatusEnum.CLOSED.getCode());
        list.add(WorkStatusEnum.RETURNED.getCode());
        list.add(WorkStatusEnum.CANCELED.getCode());
        return list;
    }

    /**
     * 获得下一个状态
     *
     * @param workStatus
     * @return
     * @author zgpi
     * @date 2020/3/2 09:33
     */
    public static Integer getNextStatus(Integer workStatus) {
        List<Integer> statusList = getStatusList();
        int index = statusList.indexOf(workStatus);
        if (index != statusList.size() - 1) {
            return statusList.get(index + 1);
        } else {
            return 0;
        }
    }
}
