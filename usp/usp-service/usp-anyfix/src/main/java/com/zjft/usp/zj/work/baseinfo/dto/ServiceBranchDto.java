package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 服务站DTO类
 *
 * @author zgpi
 * @date 2020/3/23 20:14
 */
@ApiModel(value = "服务站DTO类")
@Data
public class ServiceBranchDto implements Serializable {
    private static final long serialVersionUID = 6043640181799294592L;

    @ApiModelProperty(value = "服务站ID")
    private String branchId;

    @ApiModelProperty(value = "服务站名称")
    private String branchName;
}
