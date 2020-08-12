package com.zjft.usp.anyfix.work.check.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单审核DTO
 *
 * @author zgpi
 * @date 2020/5/11 17:00
 */
@ApiModel("工单审核DTO")
@Getter
@Setter
public class WorkCheckDto implements Serializable {

    private static final long serialVersionUID = 6220472547937962717L;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "工单状态")
    private Integer workStatus;

    @ApiModelProperty(value = "工单ID列表")
    private List<Long> workIdList;

    @ApiModelProperty(value = "服务商审核服务内容状态，1=待审核，2=审核通过，3=审核不通过")
    private Integer finishCheckStatus;

    @ApiModelProperty(value = "服务商审核服务内容状态名称")
    private String finishCheckStatusName;

    @ApiModelProperty(value = "服务商审核服务内容备注")
    private String finishCheckNote;

    @ApiModelProperty(value = "服务商审核服务内容操作人")
    private Long finishCheckUser;

    @ApiModelProperty(value = "服务商审核服务内容操作人姓名")
    private String finishCheckUserName;

    @ApiModelProperty(value = "服务商审核服务内容操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date finishCheckTime;

    @ApiModelProperty(value = "服务商审核费用内容状态，1=待审核，2=审核通过，3=审核不通过")
    private Integer feeCheckStatus;

    @ApiModelProperty(value = "服务商审核费用内容状态名称")
    private String feeCheckStatusName;

    @ApiModelProperty(value = "服务商审核费用内容备注")
    private String feeCheckNote;

    @ApiModelProperty(value = "服务商审核费用内容操作人")
    private Long feeCheckUser;

    @ApiModelProperty(value = "服务商审核费用内容操作人姓名")
    private String feeCheckUserName;

    @ApiModelProperty(value = "服务商审核费用内容操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date feeCheckTime;

    @ApiModelProperty(value = "委托商确认服务内容状态，1=待确认，2=确认通过，3=确认不通过")
    private Integer finishConfirmStatus;

    @ApiModelProperty(value = "委托商确认服务内容状态名称")
    private String finishConfirmStatusName;

    @ApiModelProperty(value = "委托商确认服务内容备注")
    private String finishConfirmNote;

    @ApiModelProperty(value = "委托商确认服务内容操作人")
    private Long finishConfirmUser;

    @ApiModelProperty(value = "委托商确认服务内容操作人姓名")
    private String finishConfirmUserName;

    @ApiModelProperty(value = "委托商确认服务内容操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date finishConfirmTime;

    @ApiModelProperty(value = "委托商确认费用内容状态，1=待确认，2=确认通过，3=确认不通过")
    private Integer feeConfirmStatus;

    @ApiModelProperty(value = "委托商确认费用内容状态名称")
    private String feeConfirmStatusName;

    @ApiModelProperty(value = "委托商确认费用内容备注")
    private String feeConfirmNote;

    @ApiModelProperty(value = "委托商确认费用内容操作人")
    private Long feeConfirmUser;

    @ApiModelProperty(value = "委托商确认费用内容操作人姓名")
    private String feeConfirmUserName;

    @ApiModelProperty(value = "委托商确认费用内容操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date feeConfirmTime;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "是否有工单费用")
    private Integer workFeeStatus;

}
