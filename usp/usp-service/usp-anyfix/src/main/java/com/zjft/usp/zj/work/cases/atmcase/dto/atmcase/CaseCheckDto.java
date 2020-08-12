package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * CASE检查DTO类
 *
 * @author zgpi
 * @date 2020/3/28 11:14
 */
@ApiModel(value = "CASE检查DTO类")
@Data
public class CaseCheckDto {

    @ApiModelProperty(value = "检查代码")
    private String checkCode;

    @ApiModelProperty(value = "检查信息")
    private String checkMsg;
}
