package com.zjft.usp.wms.business.consign.filter;

import com.zjft.usp.common.model.Page;
import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.consign.model.ConsignMain;
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
public class ConsignFilter extends Page {

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;

    @ApiModelProperty(value = "发货库房ID")
    private Long fromDepotId;

    @ApiModelProperty(value = "收货库房ID")
    private Long toDepotId;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "型号ID")
    private Long modelId;

    @ApiModelProperty(value = "分类Id")
    private Long catalogId;

    @ApiModelProperty(value = "对应明细ID(可以是审批明细ID,也可以是调拨申请明细ID、出库申请明细ID，如果是直接发货，如待修返还则可以为0)")
    private Long formDetailId;

    @ApiModelProperty(value = "是否签收状态")
    private String signed;
}
