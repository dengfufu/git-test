package com.zjft.usp.zj.work.cases.atmcase.dto.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-03-24 17:27
 * @Version 1.0
 */
@ApiModel(value = "图片JSON类")
@Data
public class ImageJsonToDto {

    @ApiModelProperty(value = "工单编号")
    private String workCode;

    @ApiModelProperty(value = "终端号")
    private String deviceCode;

    @ApiModelProperty(value = "机器型号")
    private String deviceModel;

    @ApiModelProperty(value = "制造号")
    private String serial;

    @ApiModelProperty(value = "图片BASE64")
    private String base64Img;

    @ApiModelProperty(value = "照片类型", notes = "10=设备照片，20=备件换上照片，30=备件换下照片")
    private Integer photoType;

}
