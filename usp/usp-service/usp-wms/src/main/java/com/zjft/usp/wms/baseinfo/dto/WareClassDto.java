package com.zjft.usp.wms.baseinfo.dto;

import com.zjft.usp.wms.baseinfo.model.WareClass;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 物品分类dto
 *
 * @author zgpi
 * @date 2019/10/14 1:17 下午
 **/
@ApiModel("物品分类dto")
@Getter
@Setter
public class WareClassDto extends WareClass {

    @ApiModelProperty("是否可用")
    private String enabledName;

    @ApiModelProperty("服务商名称")
    private String serviceCorpName;

    @ApiModelProperty("是否公共")
    private String isCommonName;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

    @ApiModelProperty("品牌列表")
    private List<Long> brandIdList;

    @ApiModelProperty("设备小类列表")
    private List<Long> smallClassIdList;

    @ApiModelProperty(value = "品牌名称", notes = "多个用,隔开")
    private String brandNames;

    @ApiModelProperty(value = "设备小类名称", notes = "多个用,隔开")
    private String smallClassNames;

}