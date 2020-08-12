package com.zjft.usp.pay.business.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 账户表
 * </p>
 *
 * @author CK
 * @since 2020-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account_info")
@ApiModel(value = "AccountInfo对象", description = "账户表")
public class AccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账户id 开通钱包，用户表和企业表增加关联(pay_account_id)")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "账户类型 c:个人 e:企业 p:平台")
    @TableField("account_type")
    private String accountType;

    @ApiModelProperty(value = "用户ID 用户ID，个人时非空")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "企业ID 企业ID，企业时非空")
    @TableField("corp_id")
    private Long corpId;

    @ApiModelProperty(value = "账户状态 10:激活 20:冻结")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "余额 余额 = 可用余额+冻结金额")
    @TableField("balance")
    private BigDecimal balance = new BigDecimal("0.00");

    @ApiModelProperty(value = "可用余额")
    @TableField("available_amount")
    private BigDecimal availableAmount = new BigDecimal("0.00");

    @ApiModelProperty(value = "冻结金额 使用场景有： 1.提现与资金到账期间冻结金额")
    @TableField("frozen_amount")
    private BigDecimal frozenAmount = new BigDecimal("0.00");

    @ApiModelProperty(value = "总收入")
    @TableField("total_income")
    private BigDecimal totalIncome = new BigDecimal("0.00");

    @ApiModelProperty(value = "总支出")
    @TableField("total_expend")
    private BigDecimal totalExpend = new BigDecimal("0.00");


    //===========
    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String corpName;

    @TableField(exist = false)
    private String accountTypeName;

    @TableField(exist = false)
    private String statusName;
}
