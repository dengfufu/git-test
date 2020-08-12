package com.zjft.usp.anyfix.baseinfo.dto;

import com.zjft.usp.anyfix.baseinfo.model.WorkType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单类型Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-26 15:58
 **/
@ApiModel("工单类型Dto")
@Getter
@Setter
public class WorkTypeDto extends WorkType {

    @ApiModelProperty(value = "委托商名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "系统类型名称")
    private String sysTypeName;

}
