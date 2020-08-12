package com.zjft.usp.anyfix.work.remind.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: JFZOU
 * @Date: 2020-04-17 16:34
 * @Version 1.0
 */

@Data
@TableName(value = "work_remind_m")
public class WorkRemindMain {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "预警ID，主键")
    @TableId("remind_id")
    private Long remindId;
    @ApiModelProperty(value = "预警名称")
    private String remindName;
    @ApiModelProperty(value = "企业ID")
    private Long corpId;
    @ApiModelProperty(value = "客户关系编号")
    private Long customId;
    @ApiModelProperty(value = "设备客户")
    private Long customCorp;
    @ApiModelProperty(value = "委托商企业ID")
    private Long demanderCorp;
    @ApiModelProperty(value = "服务商企业ID")
    private Long serviceCorp;
    @ApiModelProperty(value = "服务网点ID")
    private Long serviceBranch;
    @ApiModelProperty(value = "工单类型ID")
    private int workType;
    @ApiModelProperty(value = "设备大类ID")
    private Long largeClassId;
    @ApiModelProperty(value = "设备小类ID")
    private Long smallClassId;
    @ApiModelProperty(value = "品牌ID")
    private Long brandId;
    @ApiModelProperty(value = "型号ID")
    private Long modelId;
    @ApiModelProperty(value = "行政区划ID")
    private String district;
    @ApiModelProperty(value = "设备网点")
    private Long deviceBranch;
    @ApiModelProperty(value = "顺序号")
    private Long sortNo;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "启用")
    private String enabled;
    @ApiModelProperty(value = "建立人ID")
    private Long creator;
    @ApiModelProperty(value = "建立时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;
}
