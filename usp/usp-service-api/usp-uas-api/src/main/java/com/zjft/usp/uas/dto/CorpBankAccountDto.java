package com.zjft.usp.uas.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业银行账户Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-07-23 14:40
 **/
@ApiModel(value = "企业银行账户Dto")
@Data
public class CorpBankAccountDto {

    @ApiModelProperty(value = "账户ID")
    private Long accountId;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "银行账号")
    private String accountNumber;

    @ApiModelProperty(value = "银行账户名")
    private String accountName;

    @ApiModelProperty(value = "开户行")
    private String accountBank;

    @ApiModelProperty(value = "是否默认账户")
    private String isDefault;

}
