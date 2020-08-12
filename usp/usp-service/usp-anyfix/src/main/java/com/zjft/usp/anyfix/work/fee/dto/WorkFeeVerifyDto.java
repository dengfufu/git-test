package com.zjft.usp.anyfix.work.fee.dto;

import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 对账单Dto
 *
 * @author canlei
 * @version 1.0
 * @date 2020-05-12 10:30
 **/
@Getter
@Setter
@ApiModel(value = "对账单Dto")
public class WorkFeeVerifyDto extends WorkFeeVerify {

    @ApiModelProperty(value = "对账明细列表")
    private List<WorkFeeVerifyDetailDto> detailDtoList;

    @ApiModelProperty(value = "修改过对账费用的明细map")
    Map<Long, WorkFeeVerifyDetailDto> verifiedMap;

    @ApiModelProperty(value = "可对账工单列表")
    private List<WorkFeeDto> workFeeDtoList;

    @ApiModelProperty(value = "服务商名称")
    private String serviceCorpName;

    @ApiModelProperty(value = "委托商名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "添加人姓名")
    private String addUserName;

    @ApiModelProperty(value = "对账人姓名")
    private String checkUserName;

    @ApiModelProperty(value = "确认人姓名")
    private String confirmUserName;

    @ApiModelProperty(value = "对账单状态名称")
    private String statusName;

    @ApiModelProperty(value = "结算状态名称")
    private String settleStatusName;

    @ApiModelProperty(value = "行政区划名称")
    private String districtName;

    @ApiModelProperty(value = "确认结果", notes = "Y=通过，N=不通过")
    private String confirmResult;

    @ApiModelProperty(value = "委托商已确认工单数")
    private Integer confirmNum;

    @ApiModelProperty(value = "服务商已审核工单数")
    private Integer checkNum;

    @ApiModelProperty(value = "委托协议编号")
    private Long contId;

    @ApiModelProperty(value = "委托协议号")
    private String contNo;

    @ApiModelProperty(value = "对账单号前缀")
    private String prefix;

    @ApiModelProperty(value = "结算单编号")
    private Long settleId;

    @ApiModelProperty(value = "结算单明细编号")
    private Long settleDetailId;

}
