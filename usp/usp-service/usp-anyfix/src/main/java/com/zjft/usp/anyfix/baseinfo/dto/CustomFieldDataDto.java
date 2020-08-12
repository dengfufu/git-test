package com.zjft.usp.anyfix.baseinfo.dto;

import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  自定义字段数据dto
 *
 * @date: 2019/1/13 10:31
 * @author: cxd
 * @version: 1.0
 */
@ApiModel("自定义字段dto")
@Data
public class CustomFieldDataDto extends CustomFieldData {

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;
}




