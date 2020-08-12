package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工程师DTO类
 *
 * @author zgpi
 * @date 2020/3/27 11:16
 */
@ApiModel(value = "工程师DTO类")
@Data
public class EngineerDto implements Serializable {

    private static final long serialVersionUID = 1044592758914985890L;

    @ApiModelProperty(value = "工程师编号")
    private String engineerId;

    @ApiModelProperty(value = "工程师姓名")
    private String engineerName;

    @ApiModelProperty(value = "服务站")
    private String serviceBranch;
}
