package com.zjft.usp.anyfix.work.sign.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.anyfix.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工单签到表
 * </p>
 *
 * @author canlei
 * @since 2019-09-23
 */
@ApiModel("签到")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_sign")
public class WorkSign implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "签到ID")
    @TableId("sign_id")
    private Long signId;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "签到时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signTime;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "签到地点")
    private String signAddress;

    @ApiModelProperty(value = "签到照片")
    private Long signImg;

    @ApiModelProperty(value = "偏差")
    private Integer deviation;

    @ApiModelProperty(value = "是否为协同工程师", notes = "Y=是，N=否")
    private String together;

    @ApiModelProperty(value = "交通工具", notes = "1=公交，2=摩的，3=步行，4=长途汽车，5=现场，6=出租车，7=火车，8=飞机，9=公司车，10=银行车，11=郊县汽车，12=轮渡")
    private Integer traffic;

    @ApiModelProperty(value = "交通工具说明")
    private String trafficNote;

    @ApiModelProperty(value = "操作人")
    private Long operator;

    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date operateTime;


}
