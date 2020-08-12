package com.zjft.usp.pay.business.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 交易状态确认表 
 * </p>
 *
 * @author CK
 * @since 2020-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("trade_confirm")
@ApiModel(value="TradeConfirm对象", description="交易状态确认表 ")
public class TradeConfirm implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "交易类型 10: 支付 20：退款 30：提现(转账)")
    @TableField("trade_type")
    private Integer tradeType;

    @ApiModelProperty(value = "申请ID 支付申请，退款申请，提现申请 ID")
    @TableField("trade_apply_id")
    private Long tradeApplyId;

    @ApiModelProperty(value = "交易状态 支付交易：200-支付中(一般对应支付平台订单已创建，未支付)  300-支付成功 301-支付失败  退款交易：300-退款中 400-退款成功 401-退款失败  提现交易： 200-提现处理中 300-提现成功 301-提现失败")
    @TableField("trade_status")
    private Integer tradeStatus;

    @ApiModelProperty(value = "频率 m:分钟，比如每分钟确认支付状态 h:小时，每小时确认退款信息 d:天，每天转账状态")
    @TableField("freq")
    private String freq;


}
