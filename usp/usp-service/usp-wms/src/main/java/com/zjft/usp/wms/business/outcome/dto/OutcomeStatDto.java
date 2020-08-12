package com.zjft.usp.wms.business.outcome.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 入库单统计Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-12-03 15:12
 **/
@ApiModel(value = "出库单统计Dto")
@Data
public class OutcomeStatDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程节点类型,节点类型(10=填写节点20=普通审批节点30=会签审批节点40=发货节点50=收货节点60=确认节点)\")")
    private Integer flowNodeType;

    @ApiModelProperty(value = "出库单数量")
    private Long outcomeNumber;

}
