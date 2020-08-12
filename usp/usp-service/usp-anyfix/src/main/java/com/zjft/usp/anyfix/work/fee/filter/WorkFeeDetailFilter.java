package com.zjft.usp.anyfix.work.fee.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单费用明细filter
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-24 13:39
 **/
@Data
@ApiModel(value = "工单费用明细filter")
public class WorkFeeDetailFilter extends Page {

    @ApiModelProperty(value = "工单编号")
    private Long workId;

    @ApiModelProperty(value = "费用类型", notes = "1=分类费用，2=实施发生费用")
    private Integer feeType;

}
