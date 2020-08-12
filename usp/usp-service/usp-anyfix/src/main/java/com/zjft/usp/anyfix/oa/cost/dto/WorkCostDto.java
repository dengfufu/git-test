package com.zjft.usp.anyfix.oa.cost.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: JFZOU
 * @Date: 2020-02-29 9:53
 */

@ApiModel(value = "工单报修原始数据信息")
@Data
public class WorkCostDto implements Serializable {

    @ApiModelProperty(value = "工单ID，需要配合手机号表示惟一")
    private String workId;

    @ApiModelProperty(value = "工单号，不同委托商之间存在重号的情况")
    private String workCode;

    @ApiModelProperty(value = "委托商名称，由于工单编号不同企业会重新编号，因此需要与工单编号一起使用以示区分")
    private String demandedCorpName;

    @ApiModelProperty(value = "工单类型名称，对应报销系统的CASE类型")
    private String workTypeName;

    @ApiModelProperty(value = "工单状态名称")
    private String workStatusName;

    @ApiModelProperty(value = "手机号，工单系统没有报销系统的用户名，两边系统需要通过手机号对接")
    private String mobile;

    @ApiModelProperty(value = "用户姓名，取旧平台传过来的用户名称")
    private String userName;

    @ApiModelProperty(value = "设备客户名称，对应报销系统的客户名称，custom_id都有，custom_corp如果是系统上的企业就有 ")
    private String customName;

    @ApiModelProperty(value = "设备网点名称，对应报销系统的网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "设备网点地址，对应报销系统的网点地址")
    private String deviceBranchAddress;

    @ApiModelProperty(value = "服务网点名称，对应报销系统的服务站")
    private String serviceBranchName;

    @ApiModelProperty(value = "交通工具名称，对应报销系统的交通工具名称")
    private String trafficName;

    @ApiModelProperty(value = "签到时间，对应报销系统的实际出发时间")
    private String goTime;

    @ApiModelProperty(value = "服务完成时间，这个是系统时间，绝对不能修改，否则会影响报销，对应报销系统的实际完成时间")
    private String finishTime;
}
