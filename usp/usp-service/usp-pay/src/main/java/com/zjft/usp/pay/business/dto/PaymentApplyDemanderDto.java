package com.zjft.usp.pay.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author: CK
 * @create: 2020-05-26 13:55
 */
@Setter
@Getter
public class PaymentApplyDemanderDto {

    @ApiModelProperty(value = "出账账户ID")
    private Long payerCorpId;

    @ApiModelProperty(value = "入账账户ID")
    private Long payeeCorpId;

    @ApiModelProperty(value = "商品订单ID: 结算单类型")
    private Long orderId;

    @ApiModelProperty(value = "商品名称: 结算单名称")
    private String orderName;

    @ApiModelProperty(value = "商品订单金额: 计算单金额")
    private BigDecimal orderAmount;
}
