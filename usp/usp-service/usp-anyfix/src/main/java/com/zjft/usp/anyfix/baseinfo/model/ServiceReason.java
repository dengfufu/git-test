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
 * 服务商原因表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("service_reason")
@ApiModel(value="ServiceReason对象", description="服务商原因表")
public class ServiceReason implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "编号")
    private Integer id;

    @ApiModelProperty(value = "原因标题")
    private String name;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "原因类型 1=客服退单、2=客服撤回派工、3=工程师拒绝派单、4=工程师退回派工")
    private Integer reasonType;

    @ApiModelProperty(value = "详细描述")
    private String description;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

}
