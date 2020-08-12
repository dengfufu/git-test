package com.zjft.usp.device.baseinfo.dto;

import com.zjft.usp.device.baseinfo.model.DeviceModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * 设备型号DTO
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/25 2:39 下午
 **/
@ApiModel(value = "设备型号DTO")
@Getter
@Setter
public class DeviceModelDto extends DeviceModel {

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "设备小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "是否可用")
    private String enabledName;

    @ApiModelProperty(value = "设备大类编号")
    private Long largeClassId;

    @ApiModelProperty(value = "设备大类名称")
    private String largeClassName;

    /**
     * 委托商名称
     */
    private String corpName;

}
