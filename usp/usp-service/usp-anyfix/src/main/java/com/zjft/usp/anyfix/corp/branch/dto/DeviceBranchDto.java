package com.zjft.usp.anyfix.corp.branch.dto;


import com.zjft.usp.anyfix.corp.branch.model.DeviceBranch;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * DeviceBranchDto 用于查询返回设备网点
 *
 * @author zrlin
 * @date 2019-10-08 09:27
 */
@ApiModel(value = "设备网点Dto")
@Getter
@Setter
public class DeviceBranchDto extends DeviceBranch {

    @ApiModelProperty("行政区划")
    private String areaName;

    @ApiModelProperty("客户名称")
    private String customCorpName;

    @ApiModelProperty("网点全称")
    private String fullName;

    @ApiModelProperty("上级网点全称")
    private String upperFullName;

    @ApiModelProperty("客户档案")
    private DemanderCustom demanderCustom;

    @ApiModelProperty("委托商编号")
    private Long demanderCorp;

    @ApiModelProperty("委托商编号")
    private String demanderCorpName;
}
