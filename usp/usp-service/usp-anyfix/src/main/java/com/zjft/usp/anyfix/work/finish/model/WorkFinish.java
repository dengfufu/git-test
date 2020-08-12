package com.zjft.usp.anyfix.work.finish.model;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 工单服务完成表
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@ApiModel(value="WorkFinish对象", description="工单服务完成表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_finish")
public class WorkFinish implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单编号，主键")
    @TableId("work_id")
    private Long workId;

    @ApiModelProperty(value = "是否解决")
    private String solved;

    @ApiModelProperty(value = "服务情况")
    private String description;

    @ApiModelProperty(value = "服务开始时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date startTime;

    @ApiModelProperty(value = "服务完成时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date endTime;

    @ApiModelProperty(value = "服务项目")
    private String serviceItem;

    @ApiModelProperty(value = "远程处理方式")
    private Integer remoteWay;

    @ApiModelProperty(value = "服务完成附件")
    private String files;

    @ApiModelProperty(value = "客户签名图片")
    private Long signature;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "维度")
    private BigDecimal lat;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;

    @ApiModelProperty(value = "附件状态，1为通过，2为不通过")
    private String filesStatus;

    @ApiModelProperty(value = "签名状态，1为通过，2为不通过")
    private String signatureStatus;

    @ApiModelProperty(value = "签名描述")
    private String signatureDescription;
}
