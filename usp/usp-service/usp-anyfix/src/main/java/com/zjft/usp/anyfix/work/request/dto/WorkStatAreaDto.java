package com.zjft.usp.anyfix.work.request.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 区域下单统计量查询
 *
 * @author zrlin
 * @version 1.0
 * @date 2019/11/01 17:06
 **/
@ApiModel("工单统计")
@Getter
@Setter
public class WorkStatAreaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单数量")
    private Integer totalCount;

    @ApiModelProperty(value = "工单的变化量")
    private Integer changeCount;

    @ApiModelProperty(value = "省份代码")
    private String provinceCode;

    @ApiModelProperty(value = "省份名称")
    private String provinceName;

}
