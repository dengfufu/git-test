package com.zjft.usp.device.baseinfo.dto;

import com.zjft.usp.device.baseinfo.model.DeviceBrand;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 设备品牌DTO
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/25 2:37 下午
 **/
@Getter
@Setter
public class DeviceBrandDto extends DeviceBrand {

    private Long smallClassId;

    /**
     * 型号列表
     */
    private List<DeviceModelDto> deviceModelDtoList;

    /**
     * 委托商名称
     */
    private String corpName;
}
