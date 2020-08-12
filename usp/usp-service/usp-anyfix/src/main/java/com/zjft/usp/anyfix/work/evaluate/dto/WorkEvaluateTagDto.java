package com.zjft.usp.anyfix.work.evaluate.dto;

import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluateTag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单评价标签Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-28 15:30
 **/
@ApiModel(value = "工单评价标签Dto")
@Getter
@Setter
public class WorkEvaluateTagDto extends WorkEvaluateTag {

    @ApiModelProperty(value = "标签名称")
    private String tagName;

}
