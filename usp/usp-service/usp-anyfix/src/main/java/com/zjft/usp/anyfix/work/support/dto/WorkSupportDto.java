package com.zjft.usp.anyfix.work.support.dto;

import com.zjft.usp.anyfix.work.support.model.WorkSupport;
import com.zjft.usp.anyfix.work.support.model.WorkSupportRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author cxd
 * @date 2020/4/14 10:59
 * @Version 1.0
 **/
@ApiModel(value = "技术支持dto")
@Data
public class WorkSupportDto extends WorkSupport {

    @ApiModelProperty(value = "处理人")
    private String handlerName;

    @ApiModelProperty(value = "严重度")
    private String severityName;

    @ApiModelProperty(value = "跟踪记录列表")
    private List<WorkSupportRecord> recordList;
}
