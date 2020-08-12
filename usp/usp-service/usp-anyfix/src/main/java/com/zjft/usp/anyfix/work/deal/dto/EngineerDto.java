package com.zjft.usp.anyfix.work.deal.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zrlin
 * @date 2019-11-04 13:46
 */
@Data
public class EngineerDto {

    @ApiModelProperty(value = "工程师编号")
    private Long engineerId;

    @ApiModelProperty(value = "工程师头像")
    private String faceImg;


    @ApiModelProperty(value = "工程师名字")
    private String engineerName;

    @ApiModelProperty(value = "工程接单增加量")
    private Integer changeCount;

}
