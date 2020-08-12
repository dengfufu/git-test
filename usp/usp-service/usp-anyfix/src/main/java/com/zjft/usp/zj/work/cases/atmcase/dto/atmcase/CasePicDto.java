package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author canlei
 * @version 1.0
 * @date 2020-04-08 10:26
 **/
@ApiModel("CASE照片Dto")
@Data
public class CasePicDto {

    @ApiModelProperty(value = "CASE照片主表编号")
    private Long mainId;

    @ApiModelProperty(value = "CASE编号")
    private String ycaseId;

    @ApiModelProperty(value = "修改人")
    private String modWho;

    @ApiModelProperty(value = "修改时间")
    private String modTime;

    @ApiModelProperty(value = "不上传设备照片原因")
    private String reason;

    @ApiModelProperty(value = "CASE状态")
    private Integer status;

    @ApiModelProperty(value = "规范")
    private String normative;

    @ApiModelProperty(value = "不上传备件换上照片原因")
    private String upPartReason;

    @ApiModelProperty(value = "不上传备件换下照片原因")
    private String downPartReason;

    @ApiModelProperty(value = "不正常")
    private String abnormal;

    @ApiModelProperty(value = "是否关单之前上传")
    private String uploadBeforeClose;

    @ApiModelProperty(value = "是否有备件更换")
    private String hasPartReplace;

    @ApiModelProperty(value = "备件更换类型")
    private Integer partReplaceType;

    @ApiModelProperty(value = "照片数量")
    private Integer photoNum;

    @ApiModelProperty(value = "照片类型")
    private Integer photoType;

    @ApiModelProperty(value = "上传数量")
    private Integer uploadNum;

}
