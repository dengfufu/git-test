package com.zjft.usp.device.device.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.device.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备档案基本表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@ApiModel(value="DeviceInfo对象", description="设备档案基本表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_info")
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备ID")
    @TableId("device_id")
    private Long deviceId;

    @ApiModelProperty(value = "设备小类ID")
    private Long smallClassId;

    @ApiModelProperty(value = "设备规格ID")
    private Long specificationId;

    @ApiModelProperty(value = "设备品牌ID")
    private Long brandId;

    @ApiModelProperty(value = "设备型号ID")
    private Long modelId;

    @ApiModelProperty(value = "出厂序列号", notes = "设备出厂的序列号")
    private String serial;

    @ApiModelProperty(value = "服务委托方")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "出厂日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date factoryDate;

    @ApiModelProperty(value = "购买日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date purchaseDate;

    @ApiModelProperty(value = "保修开始日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date warrantyStartDate;

    @ApiModelProperty(value = "保修结束日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date warrantyEndDate;

    @ApiModelProperty(value = "保修状态", notes = "1=保内，2=保外")
    private Integer warrantyStatus;

    @ApiModelProperty(value = "维保方式", notes = "10=整机保，20=单次保")
    private Integer warrantyMode;

    @ApiModelProperty(value = "委保合同号")
    private String contNo;

    @ApiModelProperty(value = "保修说明")
    private String warrantyNote;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

}
