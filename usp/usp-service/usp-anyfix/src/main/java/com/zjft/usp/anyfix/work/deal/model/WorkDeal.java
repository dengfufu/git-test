package com.zjft.usp.anyfix.work.deal.model;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 工单处理信息表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
@ApiModel(value = "WorkDeal对象", description = "工单处理信息表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_deal")
public class WorkDeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单ID")
    @TableId("work_id")
    private Long workId;

    @ApiModelProperty(value = "服务委托方")
    private Long demanderCorp;

    @ApiModelProperty(value = "工单编号")
    private String workCode;

    @ApiModelProperty(value = "设备ID")
    private Long deviceId;

    @ApiModelProperty(value = "工单状态")
    private Integer workStatus;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "服务网点")
    private Long serviceBranch;

    @ApiModelProperty(value = "分配人员")
    private Long dispatchStaff;

    @ApiModelProperty(value = "分配时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date dispatchTime;

    @ApiModelProperty(value = "受理人员")
    private Long handleStaff;

    @ApiModelProperty(value = "受理时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date handleTime;

    @ApiModelProperty(value = "服务方式")
    private Integer serviceMode;

    @ApiModelProperty(value = "派单模式")
    private Integer assignMode;

    @ApiModelProperty(value = "派单人员")
    private Long assignStaff;

    @ApiModelProperty(value = "派单时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date assignTime;

    @ApiModelProperty(value = "服务工程师")
    private Long engineer;

    @ApiModelProperty(value = "协同工程师，多个用逗号分隔")
    private String togetherEngineers;

    @ApiModelProperty(value = "外部协同人员")
    private String helpNames;

    @ApiModelProperty(value = "认领时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date acceptTime;

    @ApiModelProperty(value = "预约时间开始")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeBegin;

    @ApiModelProperty(value = "预约时间结束")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date bookTimeEnd;

    @ApiModelProperty(value = "签到时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signTime;

    @ApiModelProperty(value = "服务开始时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startTime;

    @ApiModelProperty(value = "服务完成时间", notes = "手动输入")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endTime;

    @ApiModelProperty(value = "服务完成操作时间", notes = "系统自动获取")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date finishTime;

    @ApiModelProperty(value = "客户评价时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date evaluateTime;

    @ApiModelProperty(value = "服务商审核服务状态", notes = "1=待审核，2=审核通过，3=审核不通过")
    private Integer finishCheckStatus;

    @ApiModelProperty(value = "服务商审核服务操作时间")
    private Date finishCheckTime;

    @ApiModelProperty(value = "服务商审核费用状态", notes = "1=待审核，2=审核通过，3=审核不通过")
    private Integer feeCheckStatus;

    @ApiModelProperty(value = "服务商审核费用操作时间")
    private Date feeCheckTime;

    @ApiModelProperty(value = "委托商确认服务状态", notes = "1=待确认，2=确认通过，3=确认不通过")
    private Integer finishConfirmStatus;

    @ApiModelProperty(value = "委托商确认服务操作时间")
    private Date finishConfirmTime;

    @ApiModelProperty(value = "委托商确认费用状态", notes = "1=待确认，2=确认通过，3=确认不通过")
    private Integer feeConfirmStatus;

    @ApiModelProperty(value = "委托商确认费用操作时间")
    private Date feeConfirmTime;

    @ApiModelProperty(value = "回访人员")
    private Long reviewStaff;

    @ApiModelProperty(value = "回访时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date reviewTime;

    @ApiModelProperty(value = "交通工具", notes = "1=公交，2=摩的，3=步行，4=长途汽车，5=现场，6=出租车，7=火车，8=飞机，9=公司车，10=银行车，11=郊县汽车，12=轮渡")
    private Integer traffic;

    @ApiModelProperty(value = "交通工具说明")
    private String trafficNote;

    @ApiModelProperty(value = "出发时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date goTime;

    @ApiModelProperty(value = "撤单时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date recallTime;

    @ApiModelProperty(value = "撤单人")
    private Long recallStaff;

    @ApiModelProperty(value = "退单时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date returnTime;

    @ApiModelProperty(value = "退单人")
    private Long returnStaff;

    @ApiModelProperty(value = "是否已经回访", notes = "1=已回访，非1未回访")
    private Integer isReview;

    @ApiModelProperty(value = "委托商结算状态", notes = "1=未结算，2=结算中，3=已结算")
    private Integer settleDemanderStatus;

    @ApiModelProperty(value = "费用录入状态", notes = "1未录入完成，2=已录入完成")
    private Integer workFeeStatus;

    @ApiModelProperty(value = "人天")
    private BigDecimal manDay;
}
