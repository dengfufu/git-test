package com.zjft.usp.anyfix.work.remind.dto;

import com.zjft.usp.anyfix.work.remind.model.WorkRemindDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单预警明细Dto
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-23 21:18
 **/
@Data
public class WorkRemindDetailDto extends WorkRemindDetail {
    @ApiModelProperty(value = "预警类型名称")
    private String remindTypeName;
}
