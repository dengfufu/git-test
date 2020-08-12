package com.zjft.usp.device.device.dto;

import com.zjft.usp.device.device.model.DeviceService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备服务信息Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-23 15:15
 **/
@ApiModel(value = "设备服务信息Dto")
@Getter
@Setter
public class DeviceServiceDto extends DeviceService {

    @ApiModelProperty("服务网点名称")
    private String serviceBranchName;

    @ApiModelProperty("服务主管姓名")
    private String workManagerName;

    @ApiModelProperty("服务工程师姓名")
    private String engineerName;

}
