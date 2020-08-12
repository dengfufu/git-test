package com.zjft.usp.anyfix.settle.model;

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
 * 委托商结算单
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("settle_demander")
@ApiModel(value="SettleDemander对象", description="委托商结算单")
public class SettleDemander implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算单编号")
    @TableId("settle_id")
    private Long settleId;

    @ApiModelProperty(value = "结算单号", notes = "用于显示")
    private String settleCode;

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "供应商编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "结算方式", notes = "1=按周期结算，2=按单结算")
    private Integer settleWay;

    @ApiModelProperty(value = "结算起始日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startDate;

    @ApiModelProperty(value = "结算截止日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endDate;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "委托协议编号")
    private Long contId;

    @ApiModelProperty(value = "结算工单总数")
    private Integer workQuantity;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settleFee;

    @ApiModelProperty(value = "确认状态，1=待确认，2=确认通过，3=确认不通过")
    private Integer status;

    @ApiModelProperty(value = "付款状态，1=未付款，2=已付款，3=已收款")
    private Integer payStatus;

    @ApiModelProperty(value = "开票状态，1=未开票，2=已开票")
    private Integer invoiceStatus;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "结算人员")
    private Long operator;

    @ApiModelProperty(value = "结算时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @ApiModelProperty(value = "确认人员")
    private Long checkUser;

    @ApiModelProperty(value = "确认时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date checkTime;

    @ApiModelProperty(value = "确认备注")
    private String checkNote;

    @ApiModelProperty(value = "收款账号")
    private String accountNumber;

    @ApiModelProperty(value = "户名")
    private String accountName;

    @ApiModelProperty(value = "开户行")
    private String accountBank;

    @ApiModelProperty(value = "银行账户ID")
    private Long accountId;

}
