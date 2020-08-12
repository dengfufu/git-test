package com.zjft.usp.anyfix.work.evaluate.dto;

import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluateIndex;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 评价指标dto
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/14 3:27 下午
 **/
@ApiModel("评价指标")
@Getter
@Setter
public class WorkEvaluateIndexDto extends WorkEvaluateIndex {

    @ApiModelProperty(value = "指标名称")
    private String evaluateName;

    @ApiModelProperty(value = "显示样式，1=星星， 2=列表")
    private Integer showType;

    @ApiModelProperty(value = "分数对应描述")
    private String label;

    @ApiModelProperty(value = "分数对应样式数量，比如几颗星")
    private Integer number;

    @ApiModelProperty(value = "指标满分对应样式数量，比如最多几颗星")
    private Integer maxNumber;


}
