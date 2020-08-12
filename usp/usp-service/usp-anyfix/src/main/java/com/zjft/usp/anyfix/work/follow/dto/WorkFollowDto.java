package com.zjft.usp.anyfix.work.follow.dto;

import com.zjft.usp.anyfix.work.follow.model.WorkFollow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cxd
 * @date 2020/4/14 10:59
 * @Version 1.0
 **/
@ApiModel(value = "跟进记录")
@Data
public class WorkFollowDto extends WorkFollow {
    @ApiModelProperty(value = "操作人")
    private String operatorName;
}
