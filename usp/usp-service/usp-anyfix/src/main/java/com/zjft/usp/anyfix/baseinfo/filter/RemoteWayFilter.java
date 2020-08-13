package com.zjft.usp.anyfix.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/16 15:12
 */
@ApiModel("远程处理方式查询条件")
@Getter
@Setter
public class RemoteWayFilter extends Page {

    @ApiModelProperty(value = "服务商")
    private Long serviceCorp;

    @ApiModelProperty(value = "远程处理方式名称")
    private String name;

    @ApiModelProperty(value = "是否可用")
    private String enabled;

//    @ApiModelProperty(value = "编号")
//    private Integer id;
}
