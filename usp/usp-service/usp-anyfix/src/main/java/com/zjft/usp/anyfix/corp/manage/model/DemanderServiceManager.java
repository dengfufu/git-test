package com.zjft.usp.anyfix.corp.manage.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 委托商与服务商客户经理关系表
 * </p>
 *
 * @author zgpi
 * @since 2020-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("demander_service_manager")
@ApiModel(value="DemanderServiceManager对象", description="委托商与服务商客户经理关系表")
public class DemanderServiceManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "关系ID")
    private Long referId;

    @ApiModelProperty(value = "委托商编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "客户经理")
    private Long manager;


}
