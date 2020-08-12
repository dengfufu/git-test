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
 * 服务评价指标表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("service_evaluate")
@ApiModel(value="ServiceEvaluate对象", description="服务评价指标表")
public class ServiceEvaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "指标编号")
    private Integer id;

    @ApiModelProperty(value = "指标名称")
    private String name;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "分值列表  形式为1，2，3，4，5")
    private String scores;

    @ApiModelProperty(value = "标签列表 形式为差，中，优")
    private String labels;

    @ApiModelProperty(value = "显示样式，1为星星，2为列表")
    private Integer showType;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否可用 1正常 2失效")
    private String enabled;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
