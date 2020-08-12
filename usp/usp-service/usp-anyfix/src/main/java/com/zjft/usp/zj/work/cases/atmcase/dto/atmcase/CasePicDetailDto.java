package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author user
 * @version 1.0
 * @date 2020-04-08 13:44
 **/
@ApiModel("Case照片明细")
@Data
public class CasePicDetailDto {

    @ApiModelProperty(value = "文件编号")
    private Long fileId;

    @ApiModelProperty(value = "Case照片主表编号")
    private Long mainId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件标题")
    private String fileTitle;

    @ApiModelProperty(value = "文件类别")
    private String fileCatalog;

    @ApiModelProperty(value = "内容类型")
    private String contentType;

    @ApiModelProperty(value = "压缩前文件大小")
    private Long oldFileSize;

    @ApiModelProperty(value = "压缩后文件大小")
    private Long newFileSize;

    @ApiModelProperty(value = "审核状态", notes = "0=未审核， 30=审核通过，40=审核不通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核备注")
    private String auditNote;

    @ApiModelProperty(value = "上传来源")
    private Integer uploadSource;

    @ApiModelProperty(value = "上传人")
    private String uploader;

    @ApiModelProperty(value = "上传时间")
    private String uploadTime;

    @ApiModelProperty(value = "文件查看路径")
    private String photoUrl;

    @ApiModelProperty(value = "照片类型")
    private Integer photoType;

    @ApiModelProperty(value = "机器号")
    private String machineCode;

    @ApiModelProperty(value = "case编号")
    private String ycaseId;

}
