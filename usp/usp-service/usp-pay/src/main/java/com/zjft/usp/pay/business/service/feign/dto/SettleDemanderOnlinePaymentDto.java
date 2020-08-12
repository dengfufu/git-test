package com.zjft.usp.pay.business.service.feign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * 委托商结算单在线付款Dto
 */
@Setter
@Getter
@ApiModel(value = "委托商结算单在线付款Dto")
public class SettleDemanderOnlinePaymentDto {

    @ApiModelProperty(value = "结算单编号")
    private Long settleId;

    @ApiModelProperty(value = "付款操作人")
    private Long payOperator;

    @ApiModelProperty(value = "付款操作时间")
    private Date payOperateTime;
}
