package com.zjft.usp.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 公共参数
 * @author zgpi
 * @version 1.0
 * @date 2019-08-15 15:32
 **/
@Data
public class ReqParam {

    private String version;
    private Long txId;
    private Integer retryFlag;
    private BigDecimal lon;
    private BigDecimal lat;
    private String deviceId;
    private Integer deviceType;
    private String osVersion;

    private Long corpId;
}
