package com.zjft.usp.anyfix.corp.manage.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户经理Filter
 *
 * @author canlei
 * @version 1.0
 * @date 2020-06-30 16:07
 **/
@ApiModel(value = "客户经理Filter")
@Getter
@Setter
public class DemanderServiceManagerFilter extends Page {

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "委托商编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "客户经理编号")
    private Long managerId;

}
