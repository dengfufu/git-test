package com.zjft.usp.auth.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/13 11:24
 */
@ApiModel(value = "权限对象", description = "存放redis")
@Data
public class RightDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限编号")
    private Long rightId;

    @ApiModelProperty(value = "请求方法")
    private String pathMethod;

    @ApiModelProperty(value = "权限类型")
    private Integer rightType;

}
