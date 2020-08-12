package com.zjft.usp.anyfix.settle.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import com.zjft.usp.anyfix.settle.enums.PayMethodEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 委托商结算单付款表
 * </p>
 *
 * @author canlei
 * @since 2020-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("settle_demander_payment")
@ApiModel(value = "SettleDemanderPayment对象", description = "委托商结算单付款表")
public class SettleDemanderPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "付款编号")
    @TableId(value = "pay_id")
    private Long payId;

    @ApiModelProperty(value = "结算单编号")
    private Long settleId;

    @ApiModelProperty(value = "付款方式，1=在线支付，2=线下支付")
    private Integer payMethod = PayMethodEnum.OFFLINE.getCode();

    @ApiModelProperty(value = "付款人")
    private Long payer;

    @ApiModelProperty(value = "付款人姓名")
    private String payerName;

    @ApiModelProperty(value = "付款时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date payTime;

    @ApiModelProperty(value = "付款操作人")
    private Long payOperator;

    @ApiModelProperty(value = "付款操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date payOperateTime;

    @ApiModelProperty(value = "付款凭证附件，多个用逗号隔开")
    private String payFiles;

    @ApiModelProperty(value = "收款人")
    private Long receiptUser;

    @ApiModelProperty(value = "收款时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date receiptTime;

    @ApiModelProperty(value = "收款操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date receiptOperateTime;

    @ApiModelProperty(value = "收款凭证附件，多个用逗号隔开")
    private String receiptFiles;

    @ApiModelProperty(value = "开票人")
    private Long invoiceUser;

    @ApiModelProperty(value = "开票时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date invoiceTime;

    @ApiModelProperty(value = "开票操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date invoiceOperateTime;

    @ApiModelProperty(value = "开票附件")
    private String invoiceFiles;

}
