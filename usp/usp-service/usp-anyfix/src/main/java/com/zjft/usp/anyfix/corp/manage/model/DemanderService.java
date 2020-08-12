package com.zjft.usp.anyfix.corp.manage.model;

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
 * 服务委托方与服务商关系表
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("demander_service")
@ApiModel(value="DemanderService对象", description="服务委托方与服务商关系表")
public class DemanderService implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "服务委托方企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "是否可用，Y=可用，N=不可用")
    private String enabled;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    private Timestamp operateTime;

    @ApiModelProperty(value = "服务商描述")
    private String serviceDescription;

    @ApiModelProperty(value = "协议号")
    private String contNo;

    @ApiModelProperty(value = "收费规则描述")
    private String feeRuleDescription;

    @ApiModelProperty(value = "收费规则附件")
    private String feeRuleFiles;

    @ApiModelProperty(value = "服务标准描述")
    private String serviceStandardNote;

    @ApiModelProperty(value = "服务标准附件")
    private String serviceStandardFiles;

}
