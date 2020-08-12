package com.zjft.usp.wms.business.trans.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zrlin
 * @date 2019-12-24 10:08
 */
@ApiModel(value = "调拨申请发货审批")
@Getter
@Setter
public class TransConsignAuditDto {

    private Long id;
    private Integer passedQuantity;
    private Long fromDepotId;
    private String doDescribed;
}
