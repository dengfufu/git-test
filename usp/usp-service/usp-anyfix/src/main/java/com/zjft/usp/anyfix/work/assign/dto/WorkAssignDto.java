package com.zjft.usp.anyfix.work.assign.dto;

import com.zjft.usp.anyfix.work.assign.model.WorkAssign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zphu
 * @date 2019/9/29 10:59
 * @Version 1.0
 **/
@ApiModel("分派工程师")
@Getter
@Setter
public class WorkAssignDto extends WorkAssign {

    @ApiModelProperty(value = "分派的工程师列表")
    private List<WorkAssignEngineerDto> assignEngineerList;
}
