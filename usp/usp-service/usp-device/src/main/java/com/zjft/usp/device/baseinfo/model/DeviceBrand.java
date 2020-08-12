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
 * 设备品牌表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_brand")
@ApiModel(value="DeviceBrand对象", description="设备品牌表")
public class DeviceBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "品牌ID")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "品牌logo")
    private Long logo;

    @ApiModelProperty(value = "品牌名称")
    private String name;

    @ApiModelProperty(value = "品牌简称")
    private String shortName;

    @ApiModelProperty(value = "网址")
    private String website;

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
