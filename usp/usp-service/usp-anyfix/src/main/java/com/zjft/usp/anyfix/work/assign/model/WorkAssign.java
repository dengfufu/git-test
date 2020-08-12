package com.zjft.usp.anyfix.work.assign.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 派单基本表
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@ApiModel(value="WorkAssign对象", description="派单基本表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_assign")
public class WorkAssign extends Model<WorkAssign> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("派单主键")
    @TableId("assign_id")
    private Long assignId;

    @ApiModelProperty("工单ID")
    @TableId("work_id")
    private Long workId;

    @ApiModelProperty("客户ID")
    private Long staffId;

    @ApiModelProperty("派单备注")
    private String remark;

    @ApiModelProperty("派单时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date assignTime;

    @ApiModelProperty("是否有效")
    private String enabled;
}
