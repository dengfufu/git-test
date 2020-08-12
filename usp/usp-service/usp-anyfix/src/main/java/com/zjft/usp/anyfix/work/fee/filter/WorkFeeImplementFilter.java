package com.zjft.usp.anyfix.work.fee.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实施发生费用定义filter
 *
 * @author canlei
 * @version 1.0
 * @date 2020-04-20 09:11
 **/
@Data
@ApiModel(value = "实施发生费用定义filter")
public class WorkFeeImplementFilter extends Page {

    @ApiModelProperty(value = "费用名称")
    private String implementName;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "委托商")
    private Long demanderCorp;

    @ApiModelProperty(value = "是否有效")
    private String enabled;

}
