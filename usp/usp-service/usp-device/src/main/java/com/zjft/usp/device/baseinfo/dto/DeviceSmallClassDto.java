package com.zjft.usp.device.baseinfo.dto;

import com.zjft.usp.device.baseinfo.model.DeviceSmallClass;
import com.zjft.usp.device.baseinfo.model.DeviceSpecification;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * 设备分类DTO
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/9/25 11:25 上午
 **/
@Getter
@Setter
public class DeviceSmallClassDto extends DeviceSmallClass {

    /**
     * 大类名称
     */
    private String largeClassName;

    /**
     * 是否可用
     */
    private String enabledName;

    /**
     * 企业客户
     */
    private String customCorpName;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 设备规格列表
     */
    private List<DeviceSpecification> deviceSpecificationList;

    /**
     * 委托商名称
     */
    private String corpName;
}
