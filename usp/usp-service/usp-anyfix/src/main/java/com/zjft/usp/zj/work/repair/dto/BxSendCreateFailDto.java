package com.zjft.usp.zj.work.repair.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 工行补录失败Dto
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-19 14:44
 **/
@Data
public class BxSendCreateFailDto implements Serializable {
    private static final long serialVersionUID = 1L;
    // 交易序号
    private String tranID;
    // 是否重发
    private String retryFlag;
    // 服务商标识
    private String serviceID;
    // 服务商工单号
    private String facAppNo;
    // 工单类型
    private String appType;
    // 工单状态
    private String appStatus;
    // 终端编号
    private String devID;
    // 设备小类
    private String smallClass;
    // 厂商序列号 不提供。
    private String devSN;
    // 设备品牌 不提供。
    private String brandClass;
    // 设备型号 不提供。
    private String modelClass;
    // 摆放地点 不提供。
    private String location;
    // 网点名称 不提供。
    private String brName;
    // 工单描述
    private String appDesc;
    // 现场联系人
    private String connector;
    // 现场联系人电话
    private String connectorPhone;
    // 创建时间
    private String createTime;
    // 创建人
    private String createUser;
    // 创建人手机号
    private String createUserPhone;
    // 下一处理人姓名
    private String nextUser;
    // 下一处理人电话
    private String nextUserPhone;
    // 是否行外补单 1-是，0-否
    private String facAdded;
    // 服务开始时间
    private String startWorkDateTime;
    // 服务结束时间
    private String endWorkDateTime;
    // 服务类型
    private String serType;
    // 新终端编号 当服务单类型为旧机移机时，此字段为新的终端编号。
    private String newDevid;
    // 巡检处理方式
    private String dealWay;
    // 行方陪同人员 不提供。
    private String escort;
    // 处理结果 0-成功，非0表示失败
    private String return_code;
    // 结果消息 非0时必须返回
    private String return_msg;
    // 返回OME工单号
    private String ret_appNo;
    // 返回工单状态
    private String ret_appStatus;
    // 记录时间
    private Timestamp addTime;
    // 缺陷列表，附加信息
    //private List<BxSendBugRemote> listBxSendBug;
    private String retryFlagName;
    private String appTypeName;
    private String serTypeName;
    private String appTypeAndStatus;
    private String statusName;
    private String appTypeAndReturnStatus;
    private String returnStatusName;
    private String startWorkTimeFmt;
    private String endWorkTimeFmt;
    private String createTimeFmt;
}
