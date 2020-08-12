package com.zjft.usp.anyfix.work.fee.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 对账单filter
 *
 * @author canlei
 * @version 1.0
 * @date 2020-05-12 10:32
 **/
@Data
@ApiModel(value = "对账单filter")
public class WorkFeeVerifyFilter extends Page {

    @ApiModelProperty(value = "对账单名称")
    private String verifyName;

    @ApiModelProperty(value = "服务商编号")
    private Long serviceCorp;

    @ApiModelProperty(value = "委托商编号列表")
    private List<Long> demanderCorpList;

    @ApiModelProperty(value = "委托商编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "当前企业编号")
    private Long currentCorp;

    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @ApiModelProperty(value = "截止日期")
    private Date endDate;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "对账单状态")
    private Integer status;

    @ApiModelProperty(value = "对账单状态，多个用逗号分隔")
    private String statuses;

    @ApiModelProperty(value = "对账单结算状态")
    private Integer settleStatus;

    @ApiModelProperty(value = "对账单结算状态，多个用逗号分隔")
    private String settleStatuses;

    @ApiModelProperty(value = "委托商结算单编号")
    private Long settleId;

}
