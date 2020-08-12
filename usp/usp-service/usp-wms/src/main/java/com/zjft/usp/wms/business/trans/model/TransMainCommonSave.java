package com.zjft.usp.wms.business.trans.model;

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
 * 调拨基本信息共用暂存表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("trans_m_common_save")
@ApiModel(value = "TransMainCommonSave对象", description = "调拨基本信息共用暂存表")
public class TransMainCommonSave implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "调拨单ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;

    @ApiModelProperty(value = "出库库房ID")
    private Long fromDepotId;

    @ApiModelProperty(value = "收货库房ID")
    private Long toDepotId;

    @ApiModelProperty(value = "申请日期")
    private String applyDate;

    @ApiModelProperty(value = "调拨单保存状态(10=待提交20=已提交)")
    private Integer saveStatus;

    @ApiModelProperty(value = "优先级别")
    private Long priorityLevel;

    @ApiModelProperty(value = "出库描述")
    private String description;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间(先默认当前时间，修改再刷新)")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date updateTime;


}
