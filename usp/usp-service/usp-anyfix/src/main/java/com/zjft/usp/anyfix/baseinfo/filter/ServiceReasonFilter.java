package com.zjft.usp.anyfix.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/16 15:12
 */
@ApiModel("服务商原因条件")
@Getter
@Setter
public class ServiceReasonFilter extends Page {

    @ApiModelProperty(value = "服务商企业")
    private Long serviceCorp;

    @ApiModelProperty(value = "原因类型")
    private Integer reasonType;

    @ApiModelProperty(value = "原因名称")
    private String name;

    @ApiModelProperty(value = "是否可用")
    private String enabled;
}
