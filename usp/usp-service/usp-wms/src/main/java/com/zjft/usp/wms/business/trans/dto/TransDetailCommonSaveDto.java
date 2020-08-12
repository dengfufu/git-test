package com.zjft.usp.wms.business.trans.dto;

import com.zjft.usp.wms.business.stock.dto.StockCommonDto;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.trans.model.TransDetailCommonSave;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 类作用说明
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-21 14:28
 **/
@ApiModel(value = "调拨单保存明细Dto")
@Getter
@Setter
public class TransDetailCommonSaveDto extends TransDetailCommonSave {

    @ApiModelProperty(value = "物料分类编号")
    private Long catalogId;

    @ApiModelProperty(value = "物料分类名称")
    private String catalogName;

    @ApiModelProperty(value = "物料品牌编号")
    private Long brandId;

    @ApiModelProperty(value = "物料品牌名称")
    private String brandName;

    @ApiModelProperty(value = "物料型号名称")
    private String modelName;

    @ApiModelProperty(value = "物料状态名称")
    private String statusName;
}
