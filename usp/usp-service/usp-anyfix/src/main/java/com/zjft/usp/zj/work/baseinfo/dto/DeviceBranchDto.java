package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 网点DTO类
 *
 * @author zgpi
 * @date 2020/3/24 15:42
 */
@ApiModel(value = "网点DTO类")
@Data
public class DeviceBranchDto implements Serializable {

    private static final long serialVersionUID = -7971845043108472040L;

    @ApiModelProperty(value = "网点编号")
    private String branchId;

    @ApiModelProperty(value = "网点名称")
    private String branchName;

    @ApiModelProperty(value = "银行编号")
    private String bankCode;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "支行名称")
    private String bankSubBranchName;
}
