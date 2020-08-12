package com.zjft.usp.pay.business.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 充值订单表 
 * </p>
 *
 * @author CK
 * @since 2020-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("recharge_order")
@ApiModel(value="RechargeOrder对象", description="充值订单表 ")
public class RechargeOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "充值订单ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "充值金额")
    @TableField("amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "充值账户")
    @TableField("account_id")
    private Long accountId;

    @ApiModelProperty(value = "账户类型 c:个人; e:企业")
    @TableField("account_type")
    private String accountType;

    @ApiModelProperty(value = "订单状态 100:订单创建; 101:订单取消; 200:支付中; 300:支付成功; 301:支付失败")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "支付订单号")
    @TableField("pay_id")
    private Long payId;


}
