package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: JFZOU
 * @Date: 2020-03-23 15:18
 * @Version 1.0
 */
@Data
public class CaseTraceDto {

    @ApiModelProperty(value = "CASE编号")
    private String ycaseid;
    @ApiModelProperty(value = "跟踪时间")
    private String tracedatetime;
    @ApiModelProperty(value = "跟踪类型")
    private String tracetype;
    @ApiModelProperty(value = "跟踪内容")
    private String tracedesc;
    @ApiModelProperty(value = "操作人ID")
    private String oppeople;
    @ApiModelProperty(value = "操作人NAME")
    private String oppeoplename;
    @ApiModelProperty(value = "操作时间")
    private String opdatetime;
    @ApiModelProperty(value = "定位地址")
    private String addrstr;
    @ApiModelProperty(value = "定位时间")
    private String postime;
    @ApiModelProperty(value = "定位数据")
    private String posx;
    @ApiModelProperty(value = "定位数据")
    private String posy;

}
