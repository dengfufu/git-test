package com.zjft.usp.device.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备规格表
 * </p>
 *
 * @author zgpi
 * @since 2020-01-20
 */
@Getter
@Setter
public class DeviceSpecificationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long smallClassId;

    private String name;

    private String enabled;

    private Long corp;

    private Long operator;

    private Date operateTime;

}
