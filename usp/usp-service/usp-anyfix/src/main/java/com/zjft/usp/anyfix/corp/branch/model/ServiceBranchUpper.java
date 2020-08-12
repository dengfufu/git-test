package com.zjft.usp.anyfix.corp.branch.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务网点上级表
 * </p>
 *
 * @author zgpi
 * @since 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("service_branch_upper")
@ApiModel(value="ServiceBranchUpper对象", description="服务网点上级表")
public class ServiceBranchUpper implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "网点编号")
    private Long branchId;

    @ApiModelProperty(value = "上级网点编号")
    private Long upperBranchId;


}
