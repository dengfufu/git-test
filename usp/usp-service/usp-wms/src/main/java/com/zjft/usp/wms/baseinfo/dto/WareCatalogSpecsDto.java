package com.zjft.usp.wms.baseinfo.dto;

import com.zjft.usp.wms.baseinfo.model.WareCatalogSpecs;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 物料规格Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-11-27 11:05
 **/
@ApiModel(value = "物料规格Dto")
@Getter
@Setter
public class WareCatalogSpecsDto extends WareCatalogSpecs {

    @ApiModelProperty(value = "规格值列表")
    private List<String> valueList;

}
