package com.zjft.usp.wms.business.trans.dto;

import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.stock.dto.StockCommonDto;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import com.zjft.usp.wms.flow.dto.FlowInstanceTraceDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 物料调拨公共Dto
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-21 14:26
 **/
@Getter
@Setter
public class TransWareCommonDto extends TransWareCommon {
    @ApiModelProperty(value = "调拨明细列表")
    private List<TransDetailCommonSaveDto> transDetailCommonSaveDtoList;

    @ApiModelProperty(value = "调拨单对应的库存中用来发货物料")
    private List<StockCommonDto> stockCommonDtoList;

    @ApiModelProperty(value = "流程节点信息列表")
    private List<FlowInstanceNode> flowInstanceNodeList;

    @ApiModelProperty(value = "流程审批历史列表")
    private List<FlowInstanceTraceDto>  flowInstanceTraceDtoList;

    @ApiModelProperty(value = "发货明细列表")
    private List<ConsignDetailDto> consignDetailDtoList;

    @ApiModelProperty(value = "节点结束类型")
    private Integer nodeTypeId;

    @ApiModelProperty(value = "审批意见")
    private String doDescried;

    @ApiModelProperty(value = "业务大类名称")
    private String largeClassName;

    @ApiModelProperty(value = "业务小类名称")
    private String smallClassName;

    @ApiModelProperty(value = "出库库房名称")
    private String fromDepotName;

    @ApiModelProperty(value = "收货库房名称")
    private String toDepotName;

    @ApiModelProperty(value = "调拨单状态名称")
    private String transStatusName;

    @ApiModelProperty(value = "物料分类编号")
    private Long catalogId;

    @ApiModelProperty(value = "物料分类名称")
    private String catalogName;

    @ApiModelProperty(value = "物料品牌编号")
    private Long brandId;

    @ApiModelProperty(value = "物料品牌名称")
    private String brandName;

    @ApiModelProperty(value = "物料型号名称")
    private String modelName;

    @ApiModelProperty(value = "物料状态名称")
    private String statusName;

    @ApiModelProperty(value = "申请人姓名")
    private String createByName;

    @ApiModelProperty(value = "更新人姓名")
    private String updateByName;

    @ApiModelProperty(value = "批量操作明细id列表")
    private List<Long> idList;

    @ApiModelProperty(value = "当前节点名称")
    private String curNodeName;

    @ApiModelProperty(value = "当前节点类型")
    private Integer curNodeType;

    @ApiModelProperty(value = "流程节点类型,节点类型(10=填写节点20=普通审批节点30=会签审批节点40=发货节点50=收货节点60=确认节点)")
    private int flowNodeType;

    @ApiModelProperty(value = "是否调库")
    private Boolean isAdjust = true;

    @ApiModelProperty(value = "库存现有数")
    private int depotActualQty;

    @ApiModelProperty(value = "本次审批总数")
    private int currentAuditQty;

    @ApiModelProperty(value = "已审批总数")
    private int auditedQty;

    @ApiModelProperty(value = "待申请总数")
    private int toAuditQty;

    @ApiModelProperty(value = "调拨审批明细")
    private List<TransConsignAuditDto> transConsignAuditDtoList;

}
