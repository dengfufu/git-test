package com.zjft.usp.wms.business.income.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 入库基本信息共用暂存表
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("income_m_common_save")
@ApiModel(value = "IncomeMainCommonSave对象", description = "入库基本信息共用暂存表")
public class IncomeMainCommonSave implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "入库单ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "流程实例ID")
    private Long flowInstanceId;

    @ApiModelProperty(value = "业务大类ID")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID")
    private Integer smallClassId;

    @ApiModelProperty(value = "库房ID")
    private Long depotId;

    @ApiModelProperty(value = "入库日期")
    private String incomeDate;

    @ApiModelProperty(value = "入库单保存状态(10=待提交20=已提交)")
    private Integer saveStatus;

    @ApiModelProperty(value = "物料产权")
    private Long propertyRight;

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
