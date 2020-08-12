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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 工单邮寄费
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_post")
@ApiModel(value="WorkPost对象", description="工单邮寄费")
public class WorkPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邮寄单主键")
    @TableId("post_id")
    private Long postId;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "邮寄方式,1=快递，2=物流，3=托运")
    private Integer postWay;

    @ApiModelProperty(value = "快递公司名称")
    private String postCorpName;

    @ApiModelProperty(value = "邮寄单号")
    private String postNumber;

    @ApiModelProperty(value = "邮寄费")
    private BigDecimal postage;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
