package com.zjft.usp.device.baseinfo.model;

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
 * 设备小类表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@ApiModel(value="DeviceSmallClass对象", description="设备小类表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_small_class")
public class DeviceSmallClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "小类ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "大类ID")
    private Long largeClassId;

    @ApiModelProperty(value = "小类名称")
    private String name;

    @ApiModelProperty(value = "顺序号")
    private Integer sortNo;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "企业ID")
    private Long corp;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
