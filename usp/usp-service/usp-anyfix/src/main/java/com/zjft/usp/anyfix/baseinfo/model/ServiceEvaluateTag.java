package com.zjft.usp.anyfix.baseinfo.model;

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
 * 服务评价标签表
 * </p>
 *
 * @author zphu
 * @since 2019-09-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("service_evaluate_tag")
@ApiModel(value="ServiceEvaluateTag对象", description="服务评价标签表")
public class ServiceEvaluateTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "tag_id")
    @ApiModelProperty(value = "标签编号")
    private Integer tagId;

    @ApiModelProperty(value = "指标名称")
    private String name;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
