package com.zjft.usp.anyfix.corp.branch.dto;

import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 服务网点
 *
 * @author user
 * @version 1.0
 * @date 2019-10-12 13:44
 **/
@Getter
@Setter
public class ServiceBranchDto extends ServiceBranch {

    @ApiModelProperty(value = "省+市+区名称")
    private String region;

    @ApiModelProperty(value = "网点类型")
    private String typeName;

    @ApiModelProperty(value = "上级网点名称")
    private String upperBranchName;

    @ApiModelProperty(value = "下级网点数量")
    private Integer lowerBranchCount;
}
