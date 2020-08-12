package com.zjft.usp.anyfix.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单类型filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-11-11 09:09
 **/
@ApiModel(value = "工单类型filter")
@Getter
@Setter
public class WorkTypeFilter extends Page {

    @ApiModelProperty(value = "委托商企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "系统类型")
    private Integer sysType;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

}
