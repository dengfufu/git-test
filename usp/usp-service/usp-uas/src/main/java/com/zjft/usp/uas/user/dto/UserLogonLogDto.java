package com.zjft.usp.uas.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户登录日志DTO
 *
 * @author zgpi
 * @date 2020/5/25 14:25
 */
@Getter
@Setter
public class UserLogonLogDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String userName;

    private String operateTypeName;

    private String logonTypeName;

    private String logonResultName;

    private String deviceTypeName;

    private String mobile;

    private Integer logonType;

    private Long txId;

    private String clientId;

    private Integer appId;

    private BigDecimal lon;

    private BigDecimal lat;

    private String deviceId;

    private Integer deviceType;

    private String osVersion;

    private String details;

    private Date operateTime;

    /** 所属公司ID列表 **/
    private List<Long> corpIdList;

    /** 所属公司名称列表 **/
    private String corpName;

}
