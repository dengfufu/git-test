package com.zjft.usp.anyfix.work.fee.model;

import java.math.BigDecimal;

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
 * 分类费用定义
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_fee_assort_define")
@ApiModel(value="WorkFeeAssortDefine对象", description="分类费用定义")
public class WorkFeeAssortDefine implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类费用编号")
    @TableId(value = "assort_id")
    private Long assortId;

    @ApiModelProperty(value = "分类费用名称")
    private String assortName;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "适用起始日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startDate;

    @ApiModelProperty(value = "适用结束日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endDate;

    @ApiModelProperty(value = "是否用于协同工程师", notes = "Y=是，N=否")
    private String together;

    @ApiModelProperty(value = "服务方式")
    private Integer serviceMode;

    @ApiModelProperty(value = "服务项目")
    private Integer serviceItem;

    @ApiModelProperty(value = "费用金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "条件配置编号")
    private Long conditionId;

    @ApiModelProperty(value = "规则说明")
    private String note;

    @ApiModelProperty(value = "是否有效，Y=有效，N=失效")
    private String enabled;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
