package com.zjft.usp.pay.business.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付通知表 
 * </p>
 *
 * @author CK
 * @since 2020-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("payment_notify")
@ApiModel(value="PaymentNotify对象", description="支付通知表 ")
public class PaymentNotify implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "通知ID")
    @TableField("id")
    private Long id;

    @ApiModelProperty(value = "支付申请ID")
    @TableField("pay_id")
    private Long payId;

    @ApiModelProperty(value = "应用ID 业务/应用ID，用于查找应用公钥信息，对通知信息进行加密")
    @TableField("app_id")
    private Long appId;

    @ApiModelProperty(value = "通知类型 100:订单创建；101:订单取消；200:支付中；300:支付成功；301:支付失败")
    @TableField("notify_type")
    private Integer notifyType;

    @ApiModelProperty(value = "通知URL 对应状态下的通知URL，用于通知应用/业务方进行后续处理")
    @TableField("notify_url")
    private String notifyUrl;

    @ApiModelProperty(value = "最后通知时间 最后一次通知时间：1.成功通知时间2.达上限次数的时间")
    @TableField("last_notify_time")
    private LocalDateTime lastNotifyTime;

    @ApiModelProperty(value = "第几次尝试")
    @TableField("try_times")
    private Integer tryTimes;

    @ApiModelProperty(value = "最多尝试次数")
    @TableField("limit_times")
    private Integer limitTimes;

    @ApiModelProperty(value = "商品名称")
    @TableField("order_name")
    private String orderName;

    @ApiModelProperty(value = "通知送达时间")
    @TableField("notified_time")
    private LocalDateTime notifiedTime;

    @ApiModelProperty(value = "通知状态 100: 成功；200: 失败")
    @TableField("status")
    private String status;


}
