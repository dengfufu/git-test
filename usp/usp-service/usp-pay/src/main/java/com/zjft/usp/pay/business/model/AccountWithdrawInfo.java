package com.zjft.usp.pay.business.model;

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
 * 账号提现信息表 
 * </p>
 *
 * @author CK
 * @since 2020-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account_withdraw_info")
@ApiModel(value="AccountWithdrawInfo对象", description="账号提现信息表 ")
public class AccountWithdrawInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提现账户")
    @TableField("account_id")
    private Long accountId;

    @ApiModelProperty(value = "提现账号类型 1:支付宝2: 微信3: 银行卡")
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

    @ApiModelProperty(value = "是否默认提现账户 1:是 0:否")
    @TableField("is_default")
    private Integer isDefault;


}
