package com.zjft.usp.device.device.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.device.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备位置表
 * </p>
 *
 * @author zgpi
 * @since 2019-10-16
 */
@ApiModel(value="DeviceLocate对象", description="设备位置表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_locate")
public class DeviceLocate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备ID")
    @TableId("device_id")
    private Long deviceId;

    @ApiModelProperty(value = "委托方与客户关系编号")
    private Long customId;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "市郊")
    private Integer zone;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "设备网点")
    private Long branchId;

    @ApiModelProperty(value = "设备唯一编号，用户企业定义的设备号")
    private String deviceCode;

    @ApiModelProperty(value = "设备状态，1=运行 2=暂停 3=死亡")
    private Integer status;

    @ApiModelProperty(value = "安装日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date installDate;

    @ApiModelProperty(value = "设备描述")
    private String description;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
