package com.zjft.usp.anyfix.work.auto.dto;

import com.zjft.usp.anyfix.work.auto.model.WorkDispatchServiceBranch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ljzhu
 * @date 2019-09-26 18:29
 * @note
 */
@ApiModel("自动分配服务网点")
@Getter
@Setter
public class WorkDispatchServiceBranchDto extends WorkDispatchServiceBranch {

    @ApiModelProperty(value = "服务商企业名称")
    private String serviceCorpName;

    @ApiModelProperty(value = "客户企业")
    private Long customCorp;

    @ApiModelProperty(value = "客户企业名称")
    private String customCorpName;

    @ApiModelProperty(value = "供应商企业")
    private Long demanderCorp;

    @ApiModelProperty(value = "供应商企业名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "工单类型")
    private String workType;

    @ApiModelProperty(value = "工单类型名称")
    private String workTypeName;

    @ApiModelProperty(value = "设备大类")
    private Long largeClassId;

    @ApiModelProperty(value = "设备大类名称")
    private String largeClassName;

    @ApiModelProperty(value = "设备小类")
    private String smallClassId;

    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "设备品牌")
    private String brandId;

    @ApiModelProperty(value = "设备品牌名称")
    private String brandName;

    @ApiModelProperty(value = "设备型号")
    private String modelId;

    @ApiModelProperty(value = "设备型号名称")
    private String modelName;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "行政区划名称")
    private String districtName;

    @ApiModelProperty(value = "设备网点")
    private String deviceBranch;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "服务网点名称")
    private String serviceBranchName;

    @ApiModelProperty(value = "受理方式名称")
    private String handleTypeName;
}
