package com.zjft.usp.pay.business.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 钱包记录查询
 *
 * @author: CK
 * @create: 2020-06-02 16:47
 */
@ApiModel(value = "钱包记录查询filter")
@Setter
@Getter
public class AccountTraceFilter extends Page {

    @ApiModelProperty("钱包账户id")
    private Long accountId;

    @ApiModelProperty(value = "资金方向 10:收入  20:支出")
    private Integer direction;

    @ApiModelProperty(value = "流水类型 100000:支付(平台)消费;100001:余额消费;200000:提现;300000:退款;900000:退支付手续费(平台收入);900001:支付手续费(平台支出);900010:平台费用(平台收入);900011:退平台费用(平台支出);900020:入驻费用(平台收入);900021:退入驻费用(平台支出);900030:营销活动(平台支出)")
    private Integer traceType;

    @ApiModelProperty(value = "开始时间")
    private Date starTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
