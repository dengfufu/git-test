package com.zjft.usp.anyfix.work.remind.dto;

import com.zjft.usp.anyfix.work.remind.model.WorkRemindDeal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单预警处理Dto
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-05-13 23:15
 **/
@Data
public class WorkRemindDealDto extends WorkRemindDeal {
    @ApiModelProperty(value = "预警类型名称")
    private String remindTypeName;
}
