package com.zjft.usp.uas.corp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 人员规模
 *
 * @author canlei
 * @version 1.0
 * @date 2019-08-29 11:17
 **/
@Data
public class StaffScopeDto {

    @ApiModelProperty("人员规模code")
    private int code;

    @ApiModelProperty("人员规模范围name")
    private String name;

}
