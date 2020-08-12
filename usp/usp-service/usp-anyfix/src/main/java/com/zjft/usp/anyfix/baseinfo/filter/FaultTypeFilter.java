package com.zjft.usp.anyfix.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 故障现象filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-11-12 16:33
 **/
@ApiModel(value = "故障现象filter")
@Getter
@Setter
public class FaultTypeFilter extends Page {

    @ApiModelProperty(value = "供应商企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "现象名称")
    private String name;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

}
