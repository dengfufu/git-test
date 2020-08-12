package com.zjft.usp.pay.business.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 退款申请表 
 * </p>
 *
 * @author CK
 * @since 2020-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("refund_apply")
@ApiModel(value="RefundApply对象", description="退款申请表 ")
public class RefundApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "退款申请ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "原支付订单ID")
    @TableField("pay_id")
    private Long payId;

    @ApiModelProperty(value = "退款金额")
    @TableField("refund_amount")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "申请人")
    @TableField("apply_user")
    private Long applyUser;

    @ApiModelProperty(value = "申请时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField("apply_time")
    private Date applyTime;

    @ApiModelProperty(value = "退款原因")
    @TableField("apply_reason")
    private String applyReason;

    @ApiModelProperty(value = "审核人")
    @TableField("approve_user")
    private Long approveUser;

    @ApiModelProperty(value = "审核时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField("approve_time")
    private Date approveTime;

    @ApiModelProperty(value = "审核结果 1:通过 0:不通过")
    @TableField("approve_result")
    private Integer approveResult;

    @ApiModelProperty(value = "审核意见")
    @TableField("approve_note")
    private String approveNote;

    @ApiModelProperty(value = "有效期 单位：天")
    @TableField("request_period")
    private Integer requestPeriod = 1;

    @ApiModelProperty(value = "过期时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField("expire_time")
    private Date expireTime;

    @ApiModelProperty(value = "状态 100:申请创建101:申请取消；200:审核通过；201:审核不通过；300:退款中；400:退款成功；401:退款失败")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "原交易支付方式 10:支付宝 20:微信 30:余额")
    @TableField("refund_way")
    private String refundWay;

    @ApiModelProperty(value = "退款交易号 第三方支付：支付平台返回、钱包余额：空")
    @TableField("refund_trade_no")
    private String refundTradeNo;

    @ApiModelProperty(value = "完成时间 退款完成时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField("refund_time")
    private Date refundTime;

    @ApiModelProperty(value = "对账状态 第三方平台资金类交易，需对账；10: 未对账；20: 对账成功；21: 对账失败")
    @TableField("check_status")
    private Integer checkStatus;


}
