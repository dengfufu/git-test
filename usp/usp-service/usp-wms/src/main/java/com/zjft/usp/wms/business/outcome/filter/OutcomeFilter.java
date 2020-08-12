package com.zjft.usp.wms.business.outcome.filter;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjft.usp.common.model.Page;
import com.zjft.usp.wms.config.DateToLongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 分页查询出库列表
 * @author zphu
 * @date 2019/11/25 14:17
 * @Version 1.0
 **/
@Data
public class OutcomeFilter extends Page {

    @ApiModelProperty(value = "出库明细ID")
    private Long id;

    @ApiModelProperty(value = "出库明细ID的List")
    private List<Long> idList;

    @ApiModelProperty(value = "出库单编号")
    private String outcomeCode;

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "分组ID(拆单后的分组号，如果本单继续拆单也是使用同一个分组号)")
    private Long groupId;

    @ApiModelProperty(value = "业务大类ID(为了查询方便，使用冗余字段）")
    private Integer largeClassId;

    @ApiModelProperty(value = "业务小类ID(为了查询方便，使用冗余字段）")
    private Integer smallClassId;

    @ApiModelProperty(value = "库房ID")
    private Long depotId;

    @ApiModelProperty(value = "申请日期")
    private String applyDate;

    @ApiModelProperty(value = "出库单状态(10=待出库20=已出库)")
    private List<Integer> outcomeStatusList;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间区间集合")
    @JsonSerialize(using = DateToLongSerializer.class)
    private List<Date> createTimeList;

    @ApiModelProperty(value = "规格值json格式{“内存”：“4G”,“颜色”：“红色”,“硬盘”：“256G”}")
    private String normsValue;

    @ApiModelProperty(value = "出厂序列号")
    private String sn;

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "物料状态")
    private Integer status;

    @ApiModelProperty(value = "存储状态")
    private Integer situation;

    @ApiModelProperty(value = "当前用户id")
    private Long userId;

    @ApiModelProperty(value = "当前用户切换的公司Id")
    private Long userCorpId;

    @ApiModelProperty(value = "物料产权")
    private Long propertyRight;

    @ApiModelProperty(value = "物料分类")
    private Long catalogId;

    @ApiModelProperty(value = "物料品牌")
    private Long brandId;

    @ApiModelProperty(value = "型号ID")
    private Long modelId;

    @ApiModelProperty(value = "流程节点类型,节点类型(10=填写节点20=普通审批节点30=会签审批节点40=发货节点50=收货节点60=确认节点)")
    private List<Integer> flowNodeTypeList;
}
