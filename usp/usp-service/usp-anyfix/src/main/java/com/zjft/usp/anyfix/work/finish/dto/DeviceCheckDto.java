package com.zjft.usp.anyfix.work.finish.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 服务完成dto
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/2/6 13:09
 **/
@ApiModel("设备档案检查dto")
@Getter
@Setter
public class DeviceCheckDto {

    @ApiModelProperty(value = "结果类型", notes = "1=新增档案")
    private Integer resultType;

    @ApiModelProperty(value = "设备ID")
    private Long deviceId;
}
