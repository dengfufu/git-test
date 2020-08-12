package com.zjft.usp.anyfix.work.fee.dto;

import com.zjft.usp.anyfix.work.fee.model.WorkBasicFeeRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单基础服务费结算规则Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-01-07 20:25
 **/
@ApiModel("工单基础服务费结算规则Dto")
@Getter
@Setter
public class WorkBasicFeeRuleDto extends WorkBasicFeeRule {

    @ApiModelProperty(value = "结算方式")
    private String settleTypeName;

    @ApiModelProperty(value = "客户关系编号", notes = "客户关系表主键")
    private Long customId;

    @ApiModelProperty(value = "客户企业")
    private Long customCorp;

    @ApiModelProperty(value = "客户企业名称")
    private String customCorpName;

    @ApiModelProperty(value = "服务商企业名称")
    private String serviceCorpName;

    @ApiModelProperty(value = "供应商企业")
    private Long demanderCorp;

    @ApiModelProperty(value = "供应商企业名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "服务方式名称")
    private String serviceModeName;

    @ApiModelProperty(value = "工单类型")
    private Integer workType;

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

    @ApiModelProperty(value = "设备网点编号")
    private String deviceBranch;

    @ApiModelProperty(value = "设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

}
