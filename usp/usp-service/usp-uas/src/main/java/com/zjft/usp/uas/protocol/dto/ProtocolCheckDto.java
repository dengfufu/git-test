package com.zjft.usp.uas.protocol.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.uas.config.DateToLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author: CK
 * @create: 2020-06-18 11:25
 */
@Setter
@Getter
public class ProtocolCheckDto {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "协议名称")
    private String name;

    @ApiModelProperty(value = "协议地址")
    private String url;

    @ApiModelProperty(value = "更新日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date updateDate;

    @ApiModelProperty(value = "签订日期")
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date signDate;

    //=========
    @ApiModelProperty(value = "是否需要重新签约")
    private Boolean needSign;
}
