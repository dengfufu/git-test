package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author canlei
 * @version 1.0
 * @date 2020-04-08 10:27
 **/
@ApiModel(value = "CASE照片定义Dto")
@Data
public class CasePicDefineDto {

    @ApiModelProperty(value = "CASE照片主表编号")
    private Long mainId;

    @ApiModelProperty(value = "CASE照片主表编号", notes = "String类型")
    private String mainIdStr;

    @ApiModelProperty(value = "总行编号")
    private String tcode;

    @ApiModelProperty(value = "产品系列")
    private String manufacturer;

    @ApiModelProperty(value = "提示")
    private String tips;

    @ApiModelProperty(value = "照片数量")
    private Integer photoNum;

}
