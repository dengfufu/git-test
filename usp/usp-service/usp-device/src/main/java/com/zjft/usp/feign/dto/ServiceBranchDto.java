package com.zjft.usp.feign.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.device.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务网点
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-30 10:14
 **/
@ApiModel("服务网点")
@Getter
@Setter
public class ServiceBranchDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("服务网点编号")
    private Long branchId;

    @ApiModelProperty("服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty("网点名称")
    private String branchName;

    @ApiModelProperty("省份代码")
    private String province;

    @ApiModelProperty("城市代码")
    private String city;

    @ApiModelProperty("区县代码")
    private String district;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("网点类型")
    private Integer type;

    @ApiModelProperty("网点电话")
    private String branchPhone;

    @ApiModelProperty("联系人编号")
    private Long contactId;

    @ApiModelProperty("联系人姓名")
    private String contactName;

    @ApiModelProperty("联系人电话")
    private String contactPhone;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("是否可用")
    private String enabled;

    @ApiModelProperty("经度")
    private BigDecimal lon;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("操作人")
    private Long operator;

    @ApiModelProperty("操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

}
