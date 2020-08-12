package com.zjft.usp.anyfix.work.check.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工单审核信息表
 * </p>
 *
 * @author zgpi
 * @since 2020-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_check")
@ApiModel(value="WorkCheck对象", description="工单审核信息表")
public class WorkCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单ID")
    @TableId(value = "work_id")
    private Long workId;

    @ApiModelProperty(value = "服务商审核服务内容状态，1=待审核，2=审核通过，3=审核不通过")
    private Integer finishCheckStatus;

    @ApiModelProperty(value = "服务商审核服务内容备注")
    private String finishCheckNote;

    @ApiModelProperty(value = "服务商审核服务内容操作人")
    private Long finishCheckUser;

    @ApiModelProperty(value = "服务商审核服务内容操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date finishCheckTime;

    @ApiModelProperty(value = "服务商审核费用内容状态，1=待审核，2=审核通过，3=审核不通过")
    private Integer feeCheckStatus;

    @ApiModelProperty(value = "服务商审核费用内容备注")
    private String feeCheckNote;

    @ApiModelProperty(value = "服务商审核费用内容操作人")
    private Long feeCheckUser;

    @ApiModelProperty(value = "服务商审核费用内容操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date feeCheckTime;

    @ApiModelProperty(value = "委托商确认服务内容状态，1=待确认，2=确认通过，3=确认不通过")
    private Integer finishConfirmStatus;

    @ApiModelProperty(value = "委托商确认服务内容备注")
    private String finishConfirmNote;

    @ApiModelProperty(value = "委托商确认服务内容操作人")
    private Long finishConfirmUser;

    @ApiModelProperty(value = "委托商确认服务内容操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date finishConfirmTime;

    @ApiModelProperty(value = "委托商确认费用内容状态，1=待确认，2=确认通过，3=确认不通过")
    private Integer feeConfirmStatus;

    @ApiModelProperty(value = "委托商确认费用内容备注")
    private String feeConfirmNote;

    @ApiModelProperty(value = "委托商确认费用内容操作人")
    private Long feeConfirmUser;

    @ApiModelProperty(value = "委托商确认费用内容操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date feeConfirmTime;


}
