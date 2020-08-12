package com.zjft.usp.anyfix.work.auto.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ljzhu
 * @date 2019-09-27 08:39
 * @note
 */
@ApiModel(value="WorkDispatchServiceBranch对象", description="自动分配服务网点表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_dispatch_service_branch")
public class WorkDispatchServiceBranch implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "服务网点")
    private Long serviceBranch;

    @ApiModelProperty(value = "受理方式", notes = "1=自动受理，2=手动受理")
    private Integer handleType;

    @ApiModelProperty(value = "服务模式")
    private Integer serviceMode;

    @ApiModelProperty(value = "条件配置")
    private Long conditionId;
}
