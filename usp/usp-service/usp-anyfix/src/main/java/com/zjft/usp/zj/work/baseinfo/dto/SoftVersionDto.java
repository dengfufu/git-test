package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 软件版本Dto，适用于软件版本、SP软件版本、BV软件版本、其他软件版本
 *
 * @author canlei
 * @version 1.0
 * @date 2020-03-24 11:20
 **/
@ApiModel(value = "软件版本Dto")
@Data
public class SoftVersionDto {

    @ApiModelProperty(value = "版本编号")
    private String versionId;

    @ApiModelProperty(value = "版本名称")
    private String versionName;

}
