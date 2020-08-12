package com.zjft.usp.anyfix.corp.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 服务网点人员表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("service_branch_user_trace")
public class ServiceBranchUserTrace implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("branch_id")
    @ApiModelProperty("网点编号")
    private Long branchId;

    @TableField("user_id")
    @ApiModelProperty("用户编号")
    private Long userId;

    @TableField("operate")
    @ApiModelProperty("操作: 1-加入 2-离开")
    private int operate;

    @TableField("operator")
    @ApiModelProperty("操作人,userId")
    private Long operator;

    @TableField("client_id")
    @ApiModelProperty("操作客户端")
    private String clientId;

    @TableField("operate_time")
    @ApiModelProperty("操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

}
