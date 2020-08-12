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
 * 协议元数据
 * </p>
 *
 * @author CK
 * @since 2020-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uas_protocol_def")
@ApiModel(value = "ProtocolDef", description = "协议定义")
public class ProtocolDef implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "协议名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "协议地址")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "所属模块")
    @TableField("module")
    private String module;

    @ApiModelProperty(value = "更新日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    @TableField("update_date")
    private Date updateDate;

}
