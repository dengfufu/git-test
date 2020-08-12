package com.zjft.usp.anyfix.work.auto.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/13 19:14
 */
@ApiModel("工单自动处理")
@Getter
@Setter
public class WorkAutoDealFilter extends Page {

    @ApiModelProperty(value = "服务商企业")
    private Long serviceCorp;

    @ApiModelProperty(value = "客户企业")
    private Long customCorp;

    @ApiModelProperty(value = "供应商企业")
    private Long demanderCorp;

}
