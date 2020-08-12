package com.zjft.usp.pay.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 账户+订单流水相关信息表
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
@Setter
@Getter
@Accessors(chain = true)
@ApiModel("支付、提现、退款、成功后, 账户记录信息DTO")
public class AccountInfoTraceDto {

    @ApiModelProperty(value = "付款账户")
    Long payerAccountId;

    @ApiModelProperty(value = "收款人账户")
    Long payeeAccountId;

    @ApiModelProperty(value = "提现账户")
    Long withdrawAccountId;

    @ApiModelProperty(value = "退款账户")
    Long refundAccountId;

    @ApiModelProperty(value = "发生金额")
    BigDecimal amount;

    @ApiModelProperty(value = "平台用: 支付平台手续费金额")
    BigDecimal payFeeAmount;

    @ApiModelProperty(value = "平台用: 畅修平台服务费金额")
    BigDecimal platFeeAmount;

//    @ApiModelProperty(value = "流水类型 100000:支付(平台)消费;100001:余额消费;200000:提现;300000:退款;900000:退支付手续费(平台收入);900001:支付手续费(平台支出);900010:平台费用(平台收入);900011:退平台费用(平台支出);900020:入驻费用(平台收入);900021:退入驻费用(平台支出);900030:营销活动(平台支出)")
//    private Integer traceType;

    @ApiModelProperty(value = "申请ID 支付申请ID/退款申请ID/提现申请ID")
    private Long applyId;

    @ApiModelProperty(value = "申请来源 100：支付；200：提现 ；300：退款")
    private Integer applySource;

    @ApiModelProperty(value = "申请名称 支付类申请描述如：委托商结算单- SD20200516000005；充值-100；提现-100；退款-10")
    private String applyName;

}
