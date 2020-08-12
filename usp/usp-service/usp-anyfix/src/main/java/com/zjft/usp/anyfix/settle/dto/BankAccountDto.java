package com.zjft.usp.anyfix.settle.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行账号Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-30 15:58
 **/
@Data
@ApiModel(value = "银行账号Dto")
public class BankAccountDto {

    @ApiModelProperty(value = "银行账号")
    private String accountNumber;

    @ApiModelProperty(value = "户名")
    private String accountName;

    @ApiModelProperty(value = "开户银行")
    private String accountBank;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

}
