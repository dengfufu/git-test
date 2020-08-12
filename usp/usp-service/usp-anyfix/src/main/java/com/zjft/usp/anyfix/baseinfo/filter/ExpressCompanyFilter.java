package com.zjft.usp.anyfix.baseinfo.filter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 快递公司filter
 *
 * @author zgpi
 * @date 2020/4/20 10:46
 **/
@ApiModel(value = "快递公司filter")
@Getter
@Setter
public class ExpressCompanyFilter {

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "快递公司名称")
    private String name;
}
