package com.zjft.usp.anyfix.baseinfo.dto;

import com.zjft.usp.anyfix.baseinfo.model.ServiceItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 服务项目dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-05-18 08:51
 **/
@Getter
@Setter
@ApiModel(value = "服务项目dto")
public class ServiceItemDto extends ServiceItem {

    @ApiModelProperty(value = "委托商企业名称")
    private String demanderCorpName;

}
