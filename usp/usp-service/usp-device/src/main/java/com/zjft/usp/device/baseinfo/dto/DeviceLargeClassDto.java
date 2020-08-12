package com.zjft.usp.device.baseinfo.dto;

import com.zjft.usp.device.baseinfo.model.DeviceLargeClass;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 设备大类DTO
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/25 2:24 下午
 **/
@Getter
@Setter
public class DeviceLargeClassDto extends DeviceLargeClass {

    /**
     * 小类列表
     */
    private List<DeviceSmallClassDto> deviceSmallClassDtoList;

    /**
     * 委托商名称
     */
    private String corpName;
}
