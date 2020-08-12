package com.zjft.usp.anyfix.corp.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 数据项配置filter
 *
 * @author zgpi
 * @date 2020/6/30 15:13
 */
@ApiModel(value="数据项配置filter")
@Getter
@Setter
public class ServiceConfigFilter implements Serializable {

    private static final long serialVersionUID = 1676545890405788555L;

    @ApiModelProperty(value = "委托商服务商关系ID")
    private Long referId;

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "工单号")
    private Long workId;

    @ApiModelProperty(value = "配置项ID列表")
    private List<Integer> itemIdList;
}
