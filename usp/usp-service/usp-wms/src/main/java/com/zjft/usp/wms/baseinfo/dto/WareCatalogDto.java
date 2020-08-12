package com.zjft.usp.wms.baseinfo.dto;

import com.zjft.usp.wms.baseinfo.model.WareCatalog;
import com.zjft.usp.wms.baseinfo.model.WareCatalogSpecs;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : dcyu
 * @Date : 2019/11/18 18:57
 * @Desc : 物料分类信息数据传输对象
 * @Version 1.0.0
 */
@Data
public class WareCatalogDto extends WareCatalog {

    @ApiModelProperty("父分类名称")
    String parentName;

    @ApiModelProperty("物料分类规格属性")
    String specsNames;

    @ApiModelProperty("物料分类规格属性列表")
    List<WareCatalogSpecs> wareCatalogSpecsList;

    @ApiModelProperty("物料分类子列表")
    List<WareCatalogDto> children;
}
