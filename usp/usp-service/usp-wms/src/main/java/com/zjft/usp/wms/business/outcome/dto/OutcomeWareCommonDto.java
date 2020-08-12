package com.zjft.usp.wms.business.outcome.dto;

import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.flow.dto.FlowInstanceTraceDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 物料出库公共Dto
 * @Author: JFZOU
 * @Date: 2019-11-21 9:16
 */
@Data
public class OutcomeWareCommonDto extends OutcomeWareCommon {

    @ApiModelProperty(value = "出库物料列表")
    private List<OutcomeDetailCommonSaveDto> outcomeDetailCommonSaveDtoList;

    @ApiModelProperty(value = "出库记录id集合")
    private List<Long> idList;

    @ApiModelProperty(value = "协助经办人")
    private Long assistUserId;

    @ApiModelProperty(value = "供应商")
    private Long supplierId;

    @ApiModelProperty(value = "工单号")
    private Long workId;

    @ApiModelProperty(value = "厂商应还入库单号ID")
    private Long incomeId;

    @ApiModelProperty(value = "设备型号ID")
    private Long deviceModelId;

    @ApiModelProperty(value = "设备出厂序列号")
    private String deviceSn;

    @ApiModelProperty(value = "型号名称")
    private String modelName;

    @ApiModelProperty(value = "分类名称")
    private String catalogName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "创建人")
    private String createNameBy;

    @ApiModelProperty(value = "更新人")
    private String updateNameBy;

    @ApiModelProperty(value = "大类名称")
    private String largeClassName;

    @ApiModelProperty(value = "小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "库房名称")
    private String depotName;

    @ApiModelProperty(value = "产权名称")
    private String propertyRightName;

    @ApiModelProperty(value = "物料状态名称")
    private String statusName;

    @ApiModelProperty(value = "存储状态名称")
    private String situationName;

    @ApiModelProperty(value = "出库申请主表暂存ID")
    private Long mainId;

    @ApiModelProperty(value = "流程节点结束类型")
    private int nodeEndTypeId;

    @ApiModelProperty(value = "审核备注")
    private String auditNote;

    @ApiModelProperty(value = "流程节点类型,节点类型(10=填写节点20=普通审批节点30=会签审批节点40=发货节点50=收货节点60=确认节点)")
    private int flowNodeType;

    @ApiModelProperty(value = "审批历史")
    private List<FlowInstanceTraceDto> flowInstanceTraceDtoList;

    @ApiModelProperty(value = "审批节点")
    private List<FlowInstanceNode> flowInstanceNodeList;

    @ApiModelProperty(value = "当前节点名称")
    private String curNodeName;

    @ApiModelProperty(value = "当前节点类型")
    private Integer curNodeType;

    @ApiModelProperty(value = "当前审批人编号list")
    private List<Long> auditUserList;

    @ApiModelProperty(value = "当前审批人姓名，多个用逗号隔开")
    private String auditUserNames;

    @ApiModelProperty(value = "是否调库")
    private Boolean isAdjust = true;

}
