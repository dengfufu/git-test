package com.zjft.usp.wms.business.income.dto;

import com.zjft.usp.wms.business.book.dto.BookSaleBorrowResultDto;
import com.zjft.usp.wms.business.income.model.IncomeWareCommon;
import com.zjft.usp.wms.flow.dto.FlowInstanceTraceDto;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 物料入库公共Dto
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-11 11:19
 **/
@ApiModel(value = "物料入库公共Dto类")
@Getter
@Setter
public class IncomeWareCommonDto extends IncomeWareCommon {

    @ApiModelProperty(value = "入库明细列表")
    private List<IncomeDetailCommonSaveDto> incomeDetailCommonSaveDtoList;

    @ApiModelProperty(value = "审批历史")
    private List<FlowInstanceTraceDto> flowInstanceTraceDtoList;

    @ApiModelProperty(value = "审批节点")
    private List<FlowInstanceNode> flowInstanceNodeList;

    @ApiModelProperty(value = "销售借用销账明细")
    private List<BookSaleBorrowResultDto> bookSaleBorrowResultDtoList;

    @ApiModelProperty(value = "保存状态")
    private Integer saveStatus;

    @ApiModelProperty(value = "入库Id集合")
    private List<Long> incomeIdList;

    @ApiModelProperty(value = "财务记账公司ID")
    private Long financialRecord;

    @ApiModelProperty(value = "采购合同号")
    private String contId;

    @ApiModelProperty(value = "采购明细号")
    private String purchaseDetailId;

    @ApiModelProperty(value = "流程节点结束类型")
    private int nodeEndTypeId;

    @ApiModelProperty(value = "审核备注")
    private String auditNote;

    @ApiModelProperty(value = "流程节点类型")
    private int flowNodeType;

    @ApiModelProperty(value = "创建人姓名")
    private String createNameBy;

    @ApiModelProperty(value = "更新人姓名")
    private String updateNameBy;

    @ApiModelProperty(value = "库房名称")
    private String depotName;

    @ApiModelProperty(value = "大类类型名称")
    private String largeClassName;

    @ApiModelProperty(value = "小类类型名称")
    private String smallClassName;

    @ApiModelProperty(value = "物料产权名称")
    private String propertyRightName;

    @ApiModelProperty(value = "物料状态名称")
    private String statusName;

    @ApiModelProperty(value = "库存状态名称")
    private String situationName;

    @ApiModelProperty(value = "备件分类编号")
    private Long catalogId;

    @ApiModelProperty(value = "备件分类名称")
    private String catalogName;

    @ApiModelProperty(value = "备件品牌编号")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "物料型号名称")
    private String modelName;

    @ApiModelProperty(value = "当前节点名称")
    private String curNodeName;

    @ApiModelProperty(value = "当前节点类型")
    private Integer curNodeType;

    @ApiModelProperty(value = "当前审批人编号list")
    private List<Long> auditUserList;

    @ApiModelProperty(value = "当前审批人姓名，多个用逗号隔开")
    private String auditUserNames;

}
