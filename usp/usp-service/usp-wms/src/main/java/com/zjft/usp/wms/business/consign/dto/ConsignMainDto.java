package com.zjft.usp.wms.business.consign.dto;

import com.zjft.usp.wms.business.consign.model.ConsignMain;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 发货Dto
 * @author zphu
 * @date 2019/12/4 9:28
 * @Version 1.0
 **/
@Data
public class ConsignMainDto extends ConsignMain {

    @ApiModelProperty(value = "发货详情dto")
    List<ConsignDetailDto> consignDetailDtoList;

    @ApiModelProperty(value = "调拨发货详情dto")
    List<TransWareCommonDto> transWareCommonDtoList;

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;

    @ApiModelProperty(value = "对应明细ID(可以是审批明细ID,也可以是调拨申请明细ID、出库申请明细ID，如果是直接发货，如待修返还则可以为0)")
    private Long formDetailId;

    @ApiModelProperty(value = "发货库房ID")
    private Long fromDepotId;

    @ApiModelProperty(value = "收货库房ID")
    private Long toDepotId;

}
