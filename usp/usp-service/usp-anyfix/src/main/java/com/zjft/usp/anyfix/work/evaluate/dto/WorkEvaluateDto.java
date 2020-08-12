package com.zjft.usp.anyfix.work.evaluate.dto;

import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


/**
 * @author zphu
 * @date 2019/9/24 16:50
 * @Version 1.0
 **/
@Getter
@Setter
public class WorkEvaluateDto extends WorkEvaluate {

    @ApiModelProperty(value = "评价指标列表")
    private List<WorkEvaluateIndexDto> workEvaluateIndexDtoList;

    @ApiModelProperty(value = "评价标签列表")
    private List<WorkEvaluateTagDto> workEvaluateTagDtoList;

    @ApiModelProperty(value = "标签id列表")
    private String tagsStr;

    @ApiModelProperty(value = "标签value列表")
    private String tagsValStr;

    @ApiModelProperty(value = "查询评价月份")
    private String month;

    @ApiModelProperty(value = "查询评价指标")
    private String evaluateId;

    @ApiModelProperty(value = "查询评价指标分数")
    private Integer score;

    @ApiModelProperty(value = "多个工单号字符串，以逗号分隔")
    private String workIdsStr;

    @ApiModelProperty(value = "查询评价开始日期")
    private Date dateBegin;

    @ApiModelProperty(value = "查询评价结束日期")
    private Date dateEnd;

    @ApiModelProperty(value = "一定日期工程师完成的总工单数")
    private Integer workNum;

    @ApiModelProperty(value = "进行评价的公司")
    private Long demanderCorp;

    @ApiModelProperty(value = "进行评价的公司")
    private String demanderCorpName;

    @ApiModelProperty(value = "页码，从1开始")
    private int pageNum = 1;

    @ApiModelProperty(value = "页面大小")
    private int pageSize = 50;


    @ApiModelProperty(value = "统计时间")
    private int countTime;
}
