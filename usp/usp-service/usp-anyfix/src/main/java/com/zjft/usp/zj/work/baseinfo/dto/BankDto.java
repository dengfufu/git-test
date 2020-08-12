package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 银行DTO类
 *
 * @author zgpi
 * @date 2020/3/23 20:14
 */
@ApiModel(value = "服务站DTO类")
@Data
public class BankDto implements Serializable {

    private static final long serialVersionUID = -2001325289308350421L;

    @ApiModelProperty(value = "总行编号")
    private String headBankCode;

    @ApiModelProperty(value = "银行编号")
    private String bankCode;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "服务网点")
    private String serviceBranch;

    @ApiModelProperty(value = "银行级别", notes = "1=总行 2=分行")
    private Integer bankLevel;

}
