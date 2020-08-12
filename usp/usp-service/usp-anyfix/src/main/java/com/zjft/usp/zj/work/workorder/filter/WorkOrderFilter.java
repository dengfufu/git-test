package com.zjft.usp.zj.work.workorder.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * 派工filter
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-19 15:12
 **/
@ApiOperation(value = "派工filter")
@Data
public class WorkOrderFilter extends Page {
    @ApiModelProperty(value = "行方标志")
    private String serviceId;

    @ApiModelProperty(value = "派工单编号")
    private String workOrderId;

    @ApiModelProperty(value = "报修单号")
    private String repairId;
}
