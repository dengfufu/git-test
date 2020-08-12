package com.zjft.usp.anyfix.atm.appraise.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-03-09 8:38
 * @Version 1.0
 */
@ApiModel(value = "返回绩效考核工单列表信息")
@Data
public class EngineerAppraiseDto {

    @ApiModelProperty(value = "工单ID，需要配合手机号表示惟一")
    private String workId;

    @ApiModelProperty(value = "工单号，不同委托商之间存在重号的情况")
    private String workCode;

    @ApiModelProperty(value = "委托商ID")
    private Long demandedCorpId;

    @ApiModelProperty(value = "委托商名称，由于工单编号不同企业会重新编号，因此需要与工单编号一起使用以示区分")
    private String demandedCorpName;

    @ApiModelProperty(value = "工单类型ID")
    private Integer workTypeId;

    @ApiModelProperty(value = "工单类型名称，对应报销系统的CASE类型")
    private String workTypeName;

    @ApiModelProperty(value = "工单状态ID")
    private Integer workStatusId;

    @ApiModelProperty(value = "工单状态名称")
    private String workStatusName;

    @ApiModelProperty(value = "服务模式ID(1=现场 2=远程)")
    private Integer serviceModeId;

    @ApiModelProperty(value = "服务模式名称(1=现场 2=远程)")
    private String serviceModeName;

    @ApiModelProperty(value = "设备客户名称，对应报销系统的客户名称，custom_id都有，custom_corp如果是系统上的企业就有 ")
    private String customName;

    @ApiModelProperty(value = "设备网点名称，对应ATM系统的网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "设备网点地址，对应ATM系统的网点地址")
    private String deviceBranchAddress;

    @ApiModelProperty(value = "服务网点ID")
    private Long serviceBranchId;

    @ApiModelProperty(value = "服务网点名称，对应ATM系统的服务站")
    private String serviceBranchName;

    @ApiModelProperty(value = "交通工具名称，对应报销系统的交通工具名称")
    private String trafficName;

    @ApiModelProperty(value = "签到时间")
    private String signTime;

    @ApiModelProperty(value = "服务完成时间，这个是系统时间，绝对不能修改，否则会影响报销，对应报销系统的实际完成时间")
    private String finishTime;

    @ApiModelProperty(value = "服务工程师ID")
    private Long engineerId;

    @ApiModelProperty(value = "服务工程师手机号")
    private String engineerMobile;

    @ApiModelProperty(value = "协同工程师")
    private String togetherEngineers;

    @ApiModelProperty(value = "协同工程师手机号")
    private String togetherEngineerMobiles;

    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "设备大类名称")
    private String largeClassName;

    @ApiModelProperty(value = "设备小类ID")
    private Long smallClassId;

    @ApiModelProperty(value = "设备大类ID")
    private Long largeClassId;
}
