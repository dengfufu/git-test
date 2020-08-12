package com.zjft.usp.anyfix.work.fee.dto;

import com.zjft.usp.anyfix.work.fee.model.WorkFeeAssortDefine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工单分类费用定义Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-21 09:49
 **/
@Data
@ApiModel(value = "工单分类费用定义Dto")
public class WorkFeeAssortDto extends WorkFeeAssortDefine {

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
    private String workType;

    @ApiModelProperty(value = "工单类型名称")
    private String workTypeName;

    @ApiModelProperty(value = "设备大类")
    private Long largeClassId;

    @ApiModelProperty(value = "设备大类名称")
    private String largeClassName;

    @ApiModelProperty(value = "设备小类编号列表", notes = "为设备大类下的所有设备小类编号")
    private List<Long> smallClassIdList;

    @ApiModelProperty(value = "设备小类")
    private String smallClassId;

    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "设备规格")
    private String specification;

    @ApiModelProperty(value = "设备规格名称")
    private String specificationName;

    @ApiModelProperty(value = "设备品牌")
    private String brandId;

    @ApiModelProperty(value = "设备品牌名称")
    private String brandName;

    @ApiModelProperty(value = "设备型号")
    private String modelId;

    @ApiModelProperty(value = "设备型号名称")
    private String modelName;

    @ApiModelProperty(value = "分布", notes = "1=市区，2=郊县")
    private Integer zone;

    @ApiModelProperty(value = "市郊名称")
    private String zoneName;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "行政区划名称")
    private String districtName;

    @ApiModelProperty(value = "行政区划取反")
    private String districtNegate;

    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    @ApiModelProperty(value = "服务项目名称")
    private String serviceItemName;

    @ApiModelProperty(value = "是否已被使用")
    private Boolean used;

    @ApiModelProperty(value = "仅更新是否可用")
    private String updateEnabled;

}
