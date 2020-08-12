package com.zjft.usp.uas.protocol.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProtocolSignDto {
    @ApiModelProperty(value = "用户id")
    Long userId;

    @ApiModelProperty(value = "企业id")
    Long corpId;

    @ApiModelProperty(value = "操作员id，企业开通时，不为空")
    Long operator;

    @ApiModelProperty(value = "签约的协议列表")
    List<Integer> protocolIds;
}
