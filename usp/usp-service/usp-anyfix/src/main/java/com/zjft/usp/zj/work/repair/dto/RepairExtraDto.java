package com.zjft.usp.zj.work.repair.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 老平台报修附加表信息
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-18 15:19
 **/
@Data
public class RepairExtraDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报修编号")
    private String repairId;
    @ApiModelProperty(value = "详细地址")
    private String location;
    @ApiModelProperty(value = "委托商")
    private String vendorId;
    @ApiModelProperty(value = "产品规格")
    private String specification;
    @ApiModelProperty(value = "产品类型ID")
    private long productTypeId;
    @ApiModelProperty(value = "型号名称")
    private String modelName;
    @ApiModelProperty(value = "故障代码")
    private String faultCode;
    @ApiModelProperty(value = "产品说明")
    private String productNote;
    @ApiModelProperty(value = "维保方式")
    private String serviceMode;
    @ApiModelProperty(value = "分布")
    private String zoneName;
    @ApiModelProperty(value = "委保合同号")
    private String contractNo;
    @ApiModelProperty(value = "委托商名称")
    private String vendorName;
    @ApiModelProperty(value = "产品类型名称")
    private String productTypeName;

}
