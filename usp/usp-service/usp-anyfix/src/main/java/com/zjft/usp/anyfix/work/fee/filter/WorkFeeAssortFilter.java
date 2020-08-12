package com.zjft.usp.anyfix.work.fee.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单分类费用filter
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-21 10:14
 **/
@Data
@ApiModel(value = "工单分类费用filter")
public class WorkFeeAssortFilter extends Page {

    @ApiModelProperty(value = "委托商编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "分类费用名称")
    private String assortName;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

}
