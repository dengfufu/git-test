package com.zjft.usp.uas.corp.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 企业银行账户表
 * </p>
 *
 * @author zgpi
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_corp_bank_account")
@ApiModel(value="CorpBankAccount对象", description="企业银行账户表")
public class CorpBankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账户ID")
    @TableId("account_id")
    private Long accountId;

    @ApiModelProperty(value = "企业ID")
    @TableField("corp_id")
    private Long corpId;

    @ApiModelProperty(value = "银行账号")
    @TableField("account_number")
    private String accountNumber;

    @ApiModelProperty(value = "银行账户名")
    @TableField("account_name")
    private String accountName;

    @ApiModelProperty(value = "开户行")
    @TableField("account_bank")
    private String accountBank;

    @ApiModelProperty(value = "是否默认账户")
    @TableField("is_default")
    private String isDefault;


}
