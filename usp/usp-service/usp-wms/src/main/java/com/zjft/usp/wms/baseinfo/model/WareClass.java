package com.zjft.usp.wms.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物品分类表
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@ApiModel(value = "WareClass对象", description = "物品分类表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ware_class")
public class WareClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "样图：图片文件ID")
    private Long wareImg;

    @ApiModelProperty(value = "是否通用")
    private Integer isCommon;

    @ApiModelProperty(value = "是否可用", notes = "Y=可用，N=不可用")
    private String enabled;

    @ApiModelProperty(value = "服务商")
    private Long corpId;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
