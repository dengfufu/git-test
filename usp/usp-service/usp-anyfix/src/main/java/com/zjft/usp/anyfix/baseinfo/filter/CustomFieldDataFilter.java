package com.zjft.usp.anyfix.baseinfo.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义字段filter
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/11 7:07 下午
 **/
@ApiModel(value = "自定义字段数据filter")
@Getter
@Setter
public class CustomFieldDataFilter extends Page {

    @ApiModelProperty(value = "业务表单id")
    private Long formId;
}
