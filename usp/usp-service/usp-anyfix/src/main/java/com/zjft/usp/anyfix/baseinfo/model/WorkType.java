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
 * 工单类型表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_type")
@ApiModel(value="WorkType对象", description="工单类型表")
public class WorkType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "编号")
    private Integer id;

    @ApiModelProperty(value = "类型名称")
    private String name;

    @ApiModelProperty(value = "服务委托方编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "详细描述")
    private String description;

    @ApiModelProperty(value = "系统分类")
    private Integer sysType;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
