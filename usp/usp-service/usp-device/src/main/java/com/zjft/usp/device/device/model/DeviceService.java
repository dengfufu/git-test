package com.zjft.usp.device.device.model;

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
 * 设备服务信息表
 * </p>
 *
 * @author zgpi
 * @since 2019-10-16
 */
@ApiModel(value="DeviceService对象", description="设备服务信息表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("device_service")
public class DeviceService implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备ID")
    @TableId("device_id")
    private Long deviceId;

    @ApiModelProperty(value = "服务网点")
    private Long serviceBranch;

    @ApiModelProperty(value = "服务主管")
    private Long workManager;

    @ApiModelProperty(value = "服务工程师")
    private Long engineer;

    @ApiModelProperty(value = "服务说明")
    private String serviceNote;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @ApiModelProperty(value = "工单ID", notes = "通过工单自动生成的档案")
    private Long workId;

}
