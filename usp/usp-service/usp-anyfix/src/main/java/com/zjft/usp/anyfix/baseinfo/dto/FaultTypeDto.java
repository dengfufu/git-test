package com.zjft.usp.anyfix.baseinfo.dto;

import com.zjft.usp.anyfix.baseinfo.model.FaultType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 故障现象Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-26 15:47
 **/
@ApiModel("故障现象Dto")
@Getter
@Setter
public class FaultTypeDto extends FaultType {

    @ApiModelProperty(value = "委托商名称")
    private String demanderCorpName;

}
