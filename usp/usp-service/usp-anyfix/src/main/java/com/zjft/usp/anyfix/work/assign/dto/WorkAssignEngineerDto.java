package com.zjft.usp.anyfix.work.assign.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 派工工程师
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/5 14:04
 */
@ApiModel("派工工程师")
@Getter
@Setter
public class WorkAssignEngineerDto {

    @ApiModelProperty(value = "人员编号")
    private Long userId;

    @ApiModelProperty(value = "人员姓名")
    private String userName;
}
