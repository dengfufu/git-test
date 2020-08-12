package com.zjft.usp.zj.work.cases.atmcase.dto.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author: JFZOU
 * @Date: 2020-03-22 15:43
 * @Version 1.0
 */

@ApiModel(value = "CASE图片DTO类")
@Data
public class ImageDto {
    @ApiModelProperty(value = "文件Id（随机主键）")
    private long fileId;
    @ApiModelProperty(value = "图片类型")
    private int picType;
    @ApiModelProperty(value = "CASE编号")
    private String caseId;
    @ApiModelProperty(value = "终端号")
    private String atmCode;
    @ApiModelProperty(value = "原文件名称")
    private String srcFileName;
    @ApiModelProperty(value = "原文件标题")
    private String srcFileTitle;
    @ApiModelProperty(value = "文件存放子目录")
    private String srcFileCatalog;
    @ApiModelProperty(value = "文件内容类型")
    private String srcContentType;
    @ApiModelProperty(value = "压缩前文件大小")
    private long srcSize;
    @ApiModelProperty(value = "上传的原文件后缀")
    private String srcExtension;
    @ApiModelProperty(value = "压缩后文件大小")
    private long destSize;
    @ApiModelProperty(value = "压缩后新文件ID yyMMddHHmmssSSS+6位随机数+1位顺序号")
    private String destFileId;
    @ApiModelProperty(value = "压缩后的新文件后缀")
    private String destExtension;
    @ApiModelProperty(value = "照片上传来源，10=PC，20=APP，30=微信")
    private int uploadSource;
    @ApiModelProperty(value = "上传人")
    private String uploader;
    @ApiModelProperty(value = "上传时间")
    private Timestamp uploadTime;
    @ApiModelProperty(value = "文件名称")
    private String fileFileName;
    @ApiModelProperty(value = " 文件类型")
    private String fileContentType;
    @ApiModelProperty(value = " 照片路径")
    private String picUrl;
}
