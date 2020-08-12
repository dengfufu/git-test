package com.zjft.usp.wms.business.trans.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 调拨单统计Dto
 *
 * @author zelin
 * @version 1.0
 * @date 2019-12-03 15:12
 **/
@ApiModel(value = "调拨单单统计Dto")
@Data
public class TransStatCountDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程节点类型,节点类型(10=填写节点20=普通审批节点30=会签审批节点40=发货节点50=收货节点60=确认节点)\")")
    private Integer flowNodeType;

    @ApiModelProperty(value = "出库单数量")
    private Long transNumber;

    @ApiModelProperty(value = "调拨单状态")
    private Integer transStatus;

}
