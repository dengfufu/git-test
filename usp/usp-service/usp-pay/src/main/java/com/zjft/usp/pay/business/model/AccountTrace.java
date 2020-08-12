package com.zjft.usp.pay.business.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.pay.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 账户流水表
 * </p>
 *
 * @author CK
 * @since 2020-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account_trace")
@ApiModel(value = "AccountTrace对象", description = "账户流水表 ")
public class AccountTrace implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流水ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "账户ID")
    @TableField("account_id")
    private Long accountId;

    @ApiModelProperty(value = "金额")
    @TableField("amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "账户余额")
    @TableField("balance")
    private BigDecimal balance;

    @ApiModelProperty(value = "资金方向 10:收入  20:支出")
    @TableField("direction")
    private Integer direction;

    @ApiModelProperty(value = "时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField("time")
    private Date time;

    @ApiModelProperty(value = "流水类型 100000:支付(平台)消费;100001:余额消费;200000:提现;300000:退款;900000:退支付手续费(平台收入);900001:支付手续费(平台支出);900010:平台费用(平台收入);900011:退平台费用(平台支出);900020:入驻费用(平台收入);900021:退入驻费用(平台支出);900030:营销活动(平台支出)")
    @TableField("trace_type")
    private Integer traceType;

    @ApiModelProperty(value = "申请ID 支付申请ID/退款申请ID/提现申请ID")
    @TableField("apply_id")
    private Long applyId;

    @ApiModelProperty(value = "申请来源 100：支付；200：提现 ；300：退款")
    @TableField("apply_source")
    private Integer applySource;

    @ApiModelProperty(value = "申请名称 支付类申请描述如：委托商结算单- SD20200516000005；充值-100；提现-100；退款-10")
    @TableField("apply_name")
    private String applyName;


    //===========
    @TableField(exist = false)
    private String traceTypeName;

    @TableField(exist = false)
    private String applySourceName;

    @TableField(exist = false)
    private String directionName;

}
