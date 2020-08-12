package com.zjft.usp.pay.business.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 提现申请表 
 * </p>
 *
 * @author CK
 * @since 2020-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("withdraw_apply")
@ApiModel(value="WithdrawApply对象", description="提现申请表 ")
public class WithdrawApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提现申请ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "提现金额")
    @TableField("amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "提现账户")
    @TableField("account_id")
    private Long accountId;

    @ApiModelProperty(value = "账户类型 c:个人；e:企业；不同类型，限额不同")
    @TableField("account_type")
    private String accountType;

    @ApiModelProperty(value = "申请人")
    @TableField("apply_user")
    private Long applyUser;

    @ApiModelProperty(value = "申请时间")
    @TableField("apply_time")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "审核人 平台原因或者是账户原因，需要审核，则不为空")
    @TableField("approve_user")
    private Long approveUser;

    @ApiModelProperty(value = "审核时间 平台原因或者是账户原因，需要审核，则不为空")
    @TableField("approve_time")
    private LocalDateTime approveTime;

    @ApiModelProperty(value = "审核结果 1:通过；0:不通过")
    @TableField("approve_result")
    private Integer approveResult;

    @ApiModelProperty(value = "审核意见")
    @TableField("approve_note")
    private String approveNote;

    @ApiModelProperty(value = "状态 100:创建申请；101:取消申请；200:提现处理中；300:提现成功；301:提现失败")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "收款方账号类型 1:支付宝2: 微信3: 银行卡")
    @TableField("withdraw_type")
    private Integer withdrawType;

    @ApiModelProperty(value = "收款方支付宝账号ID")
    @TableField("alipay_account_id")
    private String alipayAccountId;

    @ApiModelProperty(value = "收款方支付宝账号姓名")
    @TableField("alipay_account_name")
    private String alipayAccountName;

    @ApiModelProperty(value = "收款方微信账号ID")
    @TableField("weixin_account_id")
    private String weixinAccountId;

    @ApiModelProperty(value = "收款方微信账号姓名")
    @TableField("weixin_account_name")
    private String weixinAccountName;

    @ApiModelProperty(value = "银行账户卡号")
    @TableField("bank_account_card")
    private String bankAccountCard;

    @ApiModelProperty(value = "银行账户名称")
    @TableField("bank_account_name")
    private String bankAccountName;

    @ApiModelProperty(value = "银行账户类型 c:个人; e:企业")
    @TableField("bank_account_type")
    private String bankAccountType;

    @ApiModelProperty(value = "银行名称 如果为企业，必填")
    @TableField("bank_name")
    private String bankName;

    @ApiModelProperty(value = "开户行省份 如果为企业，必填")
    @TableField("bank_province")
    private String bankProvince;

    @ApiModelProperty(value = "开户行城市 如果为企业，必填")
    @TableField("bank_city")
    private String bankCity;

    @ApiModelProperty(value = "开户行支行 如果为企业，必填")
    @TableField("bank_branch")
    private String bankBranch;

    @ApiModelProperty(value = "转账交易号 第三方支付：支付平台返回")
    @TableField("transfer_trade_no")
    private String transferTradeNo;

    @ApiModelProperty(value = "对账状态 第三方平台资金类交易，需对账；10: 未对账；20: 对账成功；21: 对账失败")
    @TableField("check_status")
    private Integer checkStatus;


}
