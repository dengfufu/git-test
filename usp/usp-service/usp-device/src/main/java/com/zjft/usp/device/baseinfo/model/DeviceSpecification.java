package com.zjft.usp.device.baseinfo.model;

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
 * 设备规格表
 * </p>
 *
 * @author zgpi
 * @since 2020-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_specification")
@ApiModel(value="DeviceSpecification对象", description="设备规格表")
public class DeviceSpecification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "小类ID")
    private Long smallClassId;

    @ApiModelProperty(value = "规格名称")
    private String name;

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
