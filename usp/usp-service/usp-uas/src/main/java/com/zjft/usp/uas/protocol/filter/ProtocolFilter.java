package com.zjft.usp.uas.protocol.filter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ProtocolFilter {
    @ApiModelProperty("用户id")
    Long userId;
    @ApiModelProperty("企业id")
    Long corpId;
    @ApiModelProperty("协议所属模块")
    String module;
}
