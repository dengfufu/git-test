package com.zjft.usp.device.device.dto;

import com.zjft.usp.device.device.model.DeviceLocate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备位置信息
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-23 15:03
 **/
@ApiModel(value = "设备位置信息Dto")
@Getter
@Setter
public class DeviceLocateDto extends DeviceLocate {

    @ApiModelProperty(value = "用户企业")
    private Long customCorp;

    @ApiModelProperty(value = "用户企业名称")
    private String customCorpName;

    @ApiModelProperty("行政区划名称")
    private String districtName;

    @ApiModelProperty("设备网点名称")
    private String deviceBranchName;

    @ApiModelProperty("设备状态名称")
    private String statusName;

}
