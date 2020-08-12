package com.zjft.usp.zj.work.repair.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 工行对接报修接收信息
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-18 15:19
 **/
@Data
public class BxReceiveCreateDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "交易序号 编码规则：yyyyMMddHHmmssSSS（17位）+3位服务商编号（不足3为前补0）+6位数字的随机数")
    private String tranID;
    @ApiModelProperty(value = "版本号")
    private String version;
    @ApiModelProperty(value = "是否重发")
    private String retryFlag;
    @ApiModelProperty(value = "服务商标识")
    private String serviceID;
    @ApiModelProperty(value = "OME工单号")
    private String appNo;
    @ApiModelProperty(value = "工单类型")
    private String appType;
    @ApiModelProperty(value = "工单状态")
    private String appStatus;
    @ApiModelProperty(value = "处理动作")
    private String activeType;
    // 工单描述
    private String appDesc;
    // 现场联系人 姓名<电话>
    private String connector;
    // 创建人
    private String createUser;
    // 工单创建时间 格式：yyyymmddhhMMss
    private String createTime;
    // 工单审核时间 格式：yyyymmddhhMMss
    private String accessorTime;
    // 预约时间 格式：yyyymmddhhMMss
    private String preBookTime;
    // 预计时长（小时）
    private String preDurat;
    // 终端编号
    private String devID;
    // 设备小类
    private String smallClass;
    // 厂商序列号
    private String devSN;
    // 设备品牌
    private String brandClass;
    // 设备型号
    private String modelClass;
    // 摆放地点
    private String location;
    // 设备所在行政区划
    private String adminValue;
    // 是否自动工单 1-是；0-否(默认)；2-自动转手工
    private String autoCreate;
    // 要求服务商处理时间
    private String requierTime;
    // 工单流转时间
    private String actionTime;
    // 服务类型
    private String serType;
    // 新终端编号 当服务单类型为旧机移机时，此字段为新的终端编号。
    private String newDevid;
    // 故障发生时间
    private String bugTime;
    // 现场联系人2信息：姓名<电话>;仅故障单有该信息
    private String connectorsec;
    // 人脸识别开关（0-关；1-开）
    private String faceIdentFlag;
    // 设备台数 不使用。用于某些网点类设备。
    private String devCnt;
    // 网点固定电话 不使用。用于某些网点类设备。
    private String siteTel;
    // 处理结果 0-成功，非0表示失败
    private String return_code;
    // 结果消息 非0时必须返回
    private String return_msg;
    // 服务商工单号
    private String facAppNo;
    // 下一处理人姓名
    private String nextUser;
    // 下一处理人电话
    private String nextUserPhone;
    // 消息ID
    private String msgId;
    // 工单创建时间
    private String displayCreateTime;
    // 工单审核时间
    private String displayAccessorTime;
    // 预约时间
    private String displayPreBookTime;
    // 工单流转时间
    private String displayActionTime;
    // 是否重发名称
    private String retryFlagName;
    // 处理动作名称
    private String activeTypeName;
    // 人脸识别开关对应名称
    private String faceIdentFlagName;
    // 故障模块列表，附加信息
    private List<BxReceiveBugDto> bxReceiveBugDtoList;
}
