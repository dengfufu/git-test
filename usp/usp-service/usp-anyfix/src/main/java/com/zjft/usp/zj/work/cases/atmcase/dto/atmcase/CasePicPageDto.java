package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author canlei
 * @version 1.0
 * @date 2020-04-08 09:12
 **/
@ApiModel("CASE照片页面初始化Dto")
@Data
public class CasePicPageDto {

    @ApiModelProperty("case")
    private CaseDto caseDto;

    @ApiModelProperty(value = "是否需要上传换上备件照片", notes = "Y=是，N=否")
    private String isNeedUploadUpPartPhoto;

    @ApiModelProperty(value = "是否需要上传换下备件照片", notes = "Y=是，N=否")
    private String isNeedUploadDownPartPhoto;

    @ApiModelProperty(value = "CASE照片Dto")
    private CasePicDto casePicDto;

    @ApiModelProperty(value = "CASE照片定义Dto")
    private CasePicDefineDto casePicDefineDto;

    @ApiModelProperty(value = "备件更换照片定义列表")
    private List<Object> partReplacePicDefineList;

}