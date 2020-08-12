package com.zjft.usp.pay.business.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author: CK
 * @create: 2020-05-26 13:55
 */
@Setter
@Getter
public class PaymentApplyFilter extends Page {

    @ApiModelProperty(value = "企业id")
    private Long corpId;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "商品名称")
    private String orderName;

    @ApiModelProperty(value = "订单状态 100:订单创建 101:订单取消 200:支付中(一般对应支付平台订单已创建，未支付) 300:支付成功 301:支付失败")
    private Integer status;

    @ApiModelProperty(value = "申请时间范围-开始")
    private Date startApplyTime;

    @ApiModelProperty(value = "申请时间范围-结束")
    private Date endApplyTime;
}
