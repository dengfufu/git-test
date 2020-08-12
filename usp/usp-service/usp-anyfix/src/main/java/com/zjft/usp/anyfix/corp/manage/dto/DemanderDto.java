package com.zjft.usp.anyfix.corp.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 服务委托方Dto
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/1 10:54
 */
@ApiModel(value = "服务委托方Dto")
@Getter
@Setter
public class DemanderDto {

    @ApiModelProperty(value = "关系编号")
    private Long id;

    @ApiModelProperty(value = "服务委托方编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务委托方名称")
    private String demanderCorpName;
}
