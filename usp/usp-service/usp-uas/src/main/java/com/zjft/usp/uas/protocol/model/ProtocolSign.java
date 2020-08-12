package com.zjft.usp.uas.protocol.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.uas.config.DateToLongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户签订协议
 * </p>
 *
 * @author CK
 * @since 2020-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_protocol_sign")
@ApiModel(value = "ProtocolSign", description = "用户/企业签订协议")
public class ProtocolSign implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "协议id")
    @TableField("protocol_id")
    private Integer protocolId;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "企业id")
    @TableField("corp_id")
    private Long corpId;

    @ApiModelProperty(value = "操作员id")
    @TableField("operator")
    private Long operator;

    @ApiModelProperty(value = "签订日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField("sign_date")
    private Date signDate;

}
