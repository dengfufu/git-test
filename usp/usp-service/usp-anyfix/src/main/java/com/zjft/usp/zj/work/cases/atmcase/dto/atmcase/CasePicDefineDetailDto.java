package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author canlei
 * @version 1.0
 * @date 2020-04-08 13:52
 **/
@ApiModel(value = "CASE照片定义明细")
@Data
public class CasePicDefineDetailDto {

    @ApiModelProperty(value = "文件编号")
    private Long fileId;

    @ApiModelProperty(value = "CASE照片主表编号")
    private Long mainId;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件标题")
    private String fileTitle;

    @ApiModelProperty(value = "文件路径")
    private String fileCatalog;

    @ApiModelProperty(value = "内容类型")
    private String contentType;

    @ApiModelProperty(value = "文件大小")
    private Long filesize;

    @ApiModelProperty(value = "照片名称")
    private String photoName;

    @ApiModelProperty(value = "上传路径")
    private String uploadFilePath;

    @ApiModelProperty(value = "照片查看路径")
    private String photoUrl;

}
