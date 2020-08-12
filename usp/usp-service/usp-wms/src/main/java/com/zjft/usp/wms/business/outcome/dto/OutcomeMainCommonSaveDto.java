package com.zjft.usp.wms.business.outcome.dto;

import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeMainCommonSave;
import com.zjft.usp.wms.business.stock.dto.StockCommonResultDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zphu
 * @date 2019/11/28 17:25
 * @Version 1.0
 **/
@Data
public class OutcomeMainCommonSaveDto extends OutcomeMainCommonSave {

    @ApiModelProperty(value = "出库详情")
    List<OutcomeDetailCommonSaveDto> outcomeDetailCommonSaveDtoList;

    @ApiModelProperty(value = "库存详情dto")
    List<StockCommonResultDto> stockCommonResultDtoList;

    @ApiModelProperty(value = "修改人名称")
    private String updateNameBy;

    @ApiModelProperty(value = "小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "库房名称")
    private String depotName;

    @ApiModelProperty(value = "协助经办人")
    private Long assistUserId;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;
}
