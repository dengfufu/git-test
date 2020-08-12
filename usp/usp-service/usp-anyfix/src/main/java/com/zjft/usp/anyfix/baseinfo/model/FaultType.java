package com.zjft.usp.anyfix.baseinfo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 故障现象表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fault_type")
@ApiModel(value="FaultType对象", description="故障现象表")
public class FaultType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "编号")
    private Integer id;

    @ApiModelProperty(value = "故障现象名")
    private String name;

    @ApiModelProperty(value = "服务委托方编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @ApiModelProperty(value = "操作人")
    private Long operator;


}
