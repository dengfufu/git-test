package com.zjft.usp.anyfix.work.fee.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 委托商对账单表
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_fee_verify")
@ApiModel(value="WorkFeeVerify对象", description="委托商对账单表")
public class WorkFeeVerify implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "对账单系统编号")
    @TableId(value = "verify_id")
    private Long verifyId;

    @ApiModelProperty(value = "对账单名称")
    private String verifyName;

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "委托商编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "对账起始日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startDate;

    @ApiModelProperty(value = "对账截止日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endDate;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "对账工单总数")
    private Integer workQuantity;

    @ApiModelProperty(value = "工单总费用")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "对账后总费用")
    private BigDecimal verifyAmount;

    @ApiModelProperty(value = "对账单状态，1=待对账，2=待确认，3=确认不通过，4=确认通过")
    private Integer status;

    @ApiModelProperty(value = "结算状态", notes = "1=待结算，2=已结算")
    private Integer settleStatus;

    @ApiModelProperty(value = "添加人")
    private Long addUser;

    @ApiModelProperty(value = "添加时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date addTime;

    @ApiModelProperty(value = "对账人")
    private Long checkUser;

    @ApiModelProperty(value = "对账时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date checkTime;

    @ApiModelProperty(value = "对账备注")
    private String checkNote;

    @ApiModelProperty(value = "确认人")
    private Long confirmUser;

    @ApiModelProperty(value = "确认时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date confirmTime;

    @ApiModelProperty(value = "确认备注")
    private String confirmNote;


}
