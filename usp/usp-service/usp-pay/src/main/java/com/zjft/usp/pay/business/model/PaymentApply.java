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
 * 支付申请表
 * </p>
 *
 * @author CK
 * @since 2020-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("payment_apply")
@ApiModel(value = "PaymentApply对象", description = "支付申请表 ")
public class PaymentApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付申请ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "入账账户ID")
    @TableField("payee_account_id")
    private Long payeeAccountId;

    @ApiModelProperty(value = "出账账户ID 使用账户余额交易，则非空")
    @TableField("payer_account_id")
    private Long payerAccountId;

    @ApiModelProperty(value = "商品订单ID: 结算单类型")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty(value = "订单类型 100000:支付(平台)消费;100001:余额消费")
    @TableField("order_type")
    private Integer orderType;

    @ApiModelProperty(value = "支付订单来源 100: 委托商结算单;101: 客户结算单;102: 工程师结算单;200: 充值订单")
    @TableField("order_source")
    private Integer orderSource;

    @ApiModelProperty(value = "商品名称")
    @TableField("order_name")
    private String orderName;

    @ApiModelProperty(value = "商品明细 用于保存支付订单商品信息快照")
    @TableField("order_detail")
    private String orderDetail;

    @ApiModelProperty(value = "商品订单金额")
    @TableField("order_amount")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "申请人")
    @TableField("apply_user")
    private Long applyUser;

    @ApiModelProperty(value = "申请时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField("apply_time")
    private Date applyTime;

    @ApiModelProperty(value = "支付订单有效期 单位：分钟。平台默认为5分钟")
    @TableField("order_period")
    private Integer orderPeriod;

    @ApiModelProperty(value = "支付订单过期时间")
    @TableField("expire_time")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date expireTime;

    @ApiModelProperty(value = "支付订单完成时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField("finish_time")
    private Date finishTime;

    @ApiModelProperty(value = "支付订单结束时间 支付完成后更新，到期后不得发起退款申请。结束时间 = 支付完成时间+平台退款期限")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty(value = "订单状态 100:订单创建 101:订单取消 200:支付中(一般对应支付平台订单已创建，未支付) 300:支付成功 301:支付失败")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "取消原因 订单取消原因 主动取消：用户输入；超时取消：订单超时取消")
    @TableField("cancel_reason")
    private String cancelReason;

    @ApiModelProperty(value = "支付方式 10:支付宝 20:微信 30:余额")
    @TableField("pay_way")
    private Integer payWay;

    @ApiModelProperty(value = "平台服务费率")
    @TableField("plat_fee_rate")
    private Float platFeeRate;

    @ApiModelProperty(value = "平台服务费用")
    @TableField("plat_fee_amount")
    private BigDecimal platFeeAmount = new BigDecimal(0.00);

    @ApiModelProperty(value = "支付平台费率 支付平台的费率，一般，支付宝：0.6% 微信：0.6%")
    @TableField("pay_fee_rate")
    private Float payFeeRate;

    @ApiModelProperty(value = "支付平台手续费 支付成功，通过支付平台获取数据")
    @TableField("pay_fee_amount")
    private BigDecimal payFeeAmount = new BigDecimal(0.00);

    @ApiModelProperty(value = "支付渠道 暂只支持：10:支付宝网页扫码支付11:支付宝 App支付20:微信网页扫码支付21:微信App支付30:钱包余额支付")
    @TableField("channel_type")
    private Integer channelType;

    @ApiModelProperty(value = "交易号 支付平台返回的交易号，用于通知，对账等")
    @TableField("trade_no")
    private String tradeNo;

    @ApiModelProperty(value = "是否退款 1: 是 0: 否")
    @TableField("is_refund")
    private Integer isRefund;

    @ApiModelProperty(value = "退款申请号")
    @TableField("refund_id")
    private Long refundId;

    @ApiModelProperty(value = "退款金额")
    @TableField("refund_amount")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "对账状态 第三方平台资金类交易，需对账。10: 未对账20: 对账成功21: 对账失败")
    @TableField("check_status")
    private Integer checkStatus;


    //===========
    @ApiModelProperty(value = "收款方企业id")
    @TableField(exist = false)
    private Long payeeCorpId;

    @ApiModelProperty(value = "收款方企业名称")
    @TableField(exist = false)
    private String payeeCorpName;

    @ApiModelProperty(value = "付款方企业id")
    @TableField(exist = false)
    private Long payerCorpId;

    @ApiModelProperty(value = "付款方企业名称")
    @TableField(exist = false)
    private String payerCorpName;

    @ApiModelProperty(value = "支付方式名称")
    @TableField(exist = false)
    private String payWayName;

    @ApiModelProperty(value = "申请人名称")
    @TableField(exist = false)
    private String applyUserName;

    @ApiModelProperty(value = "状态")
    @TableField(exist = false)
    private String statusName;

    @ApiModelProperty(value = "订单类型")
    @TableField(exist = false)
    private String orderTypeName;

    @ApiModelProperty(value = "支付来源")
    @TableField(exist = false)
    private String orderSourceName;
}
