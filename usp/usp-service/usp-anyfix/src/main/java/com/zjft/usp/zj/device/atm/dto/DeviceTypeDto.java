package com.zjft.usp.zj.device.atm.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * ATM设备类型
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-19 10:46
 **/
@Data
public class DeviceTypeDto implements Serializable {
    private String tcode;
    private String tname;
    private String manufacturer;
    private String barcodeCode;
    private String bvTypeCode;
    private String ifVersionStat;
    private String jqflCode;
    private Integer pmTemplate;
}
