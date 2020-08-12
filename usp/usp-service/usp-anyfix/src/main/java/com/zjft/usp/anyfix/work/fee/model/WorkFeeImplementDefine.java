package com.zjft.usp.anyfix.work.fee.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 实施发生费用定义
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_fee_implement_define")
@ApiModel(value="WorkFeeImplementDefine对象", description="实施发生费用定义")
public class WorkFeeImplementDefine implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单支出费用编号")
    @TableId(value = "implement_id")
    private Long implementId;

    @ApiModelProperty(value = "支出费用类型", notes = "1=郊区交通费，2=长途交通费，3=住宿费，4=出差补助，5=邮寄费")
    private Integer implementType;

    @ApiModelProperty(value = "支出费用名称")
    private String implementName;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "服务商企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "说明")
    private String note;

    @ApiModelProperty(value = "是否有效", notes = "Y=有效，N=失效")
    private String enabled;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
