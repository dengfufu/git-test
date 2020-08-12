package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分布DTO类
 *
 * @author zgpi
 * @date 2020/4/13 11:18
 */
@ApiModel(value = "分布DTO类")
@Data
public class AreaDto implements Serializable {

    private static final long serialVersionUID = 1079951981885418774L;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;
}
