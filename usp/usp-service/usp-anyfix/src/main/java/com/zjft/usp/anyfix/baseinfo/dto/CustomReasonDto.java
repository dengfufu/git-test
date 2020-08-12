package com.zjft.usp.anyfix.baseinfo.dto;

import com.zjft.usp.anyfix.baseinfo.model.CustomReason;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户撤单原因Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-26 15:24
 **/
@ApiModel("客户撤单原因Dto")
@Getter
@Setter
public class CustomReasonDto extends CustomReason {

    @ApiModelProperty(value = "委托商名称")
    private String customCorpName;

}
