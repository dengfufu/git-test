package com.zjft.usp.anyfix.corp.manage.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 
 * </p>
 *
 * @author zrlin
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("demander_cont")
@ApiModel(value="DemanderCont对象", description="")
public class  DemanderCont implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键编号")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "引用委托商服务关系表主键")
    @TableField("ref_id")
    private Long refId;

    @ApiModelProperty(value = "协议号")
    @TableField("cont_no")
    private String contNo;

    @ApiModelProperty(value = "收费规则描述")
    @TableField("fee_rule_description")
    private String feeRuleDescription;

    @ApiModelProperty(value = "收费规则附件")
    @TableField("fee_rule_files")
    private String feeRuleFiles;

    @ApiModelProperty(value = "服务标准描述")
    @TableField("service_standard_note")
    private String serviceStandardNote;

    @ApiModelProperty(value = "服务标准附件")
    @TableField("service_standard_files")
    private String serviceStandardFiles;

    @ApiModelProperty(value = "协议起始日期")
    @TableField("start_date")
    private Timestamp startDate;

    @ApiModelProperty(value = "协议结束日期")
    @TableField("end_date")
    private Timestamp endDate;

    @ApiModelProperty(value = "操作人")
    @TableField("operator")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @TableField("operate_time")
    private Timestamp operateTime;


}
