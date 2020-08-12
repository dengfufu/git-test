package com.zjft.usp.uas.corp.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author canlei
 * @date 2019-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_corp_oper")
@ApiModel("企业操作记录")
public class CorpOperation extends Model<CorpOperation> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "txid")
    @ApiModelProperty("交易编号")
    private Long txId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("企业编号")
    private Long corpId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("用户编号")
    private Long userId;

    @Deprecated
    @ApiModelProperty("应用编号")
    private Integer appId;

    @ApiModelProperty("客户端应用编号")
    private String clientId;

    @ApiModelProperty("操作类型")
    private Short operType;

    @ApiModelProperty("操作内容")
    private String details;

    @ApiModelProperty("操作时间")
    private Date operTime;

    @ApiModelProperty("经度")
    private BigDecimal lon;

    @ApiModelProperty("纬度")
    private BigDecimal lat;
}