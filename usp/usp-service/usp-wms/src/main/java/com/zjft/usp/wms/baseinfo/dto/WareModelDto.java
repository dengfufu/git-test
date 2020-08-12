package com.zjft.usp.wms.baseinfo.dto;

import com.zjft.usp.wms.baseinfo.model.WareModel;
import com.zjft.usp.wms.baseinfo.model.WareModelImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author : dcyu
 * @Date : 2019/11/19 11:26
 * @Desc : 物料型号数据传输对象
 * @Version 1.0.0
 */
@Data
public class WareModelDto extends WareModel {

    @ApiModelProperty("分类名称")
    private String catalogName;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("型号图片")
    String[] images;

    @ApiModelProperty("型号图片")
    List<WareModelImage> wareModelImageList;
}
