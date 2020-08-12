package com.zjft.usp.zj.work.cases.atmcase.dto.partreplace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 厂商备件DTO类
 *
 * @author zgpi
 * @date 2020-4-1 20:56
 **/
@ApiModel("厂商备件DTO类")
@Data
public class VendorPartDto {

    @ApiModelProperty(value = "是否需要厂商返还")
    private String isNeedReturn;
}
