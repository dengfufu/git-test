package com.zjft.usp.zj.work.cases.atmcase.filter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 厂商备件FILTER类
 *
 * @author zgpi
 * @date 2020-4-3 15:45
 **/
@ApiModel("厂商备件FILTER类")
@Data
public class VendorPartFilter {

    @ApiModelProperty("换上备件编号")
    private String newPartId;

    @ApiModelProperty("换上备件条形码")
    private String newBarCode;

    @ApiModelProperty(value = "备件编码")
    private String partCode;
}
