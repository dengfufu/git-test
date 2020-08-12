package com.zjft.usp.wms.business.outcome.dto;

import cn.hutool.core.date.DateTime;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 入库明细Dto
 * @author zphu
 * @date 2019/11/21 16:42
 * @Version 1.0
 **/
@Data
public class OutcomeDetailCommonSaveDto extends OutcomeDetailCommonSave {

    @ApiModelProperty(value = "企业ID")
    private Long corpId;

    @ApiModelProperty(value = "流程实例ID")
    private Long flowInstanceId;

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

    @ApiModelProperty(value = "出库单保存状态(10=待提交20=已提交)")
    private Integer saveStatus;

    @ApiModelProperty(value = "出库描述")
    private String description;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private DateTime createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateBy;

    @ApiModelProperty(value = "修改时间(先默认当前时间，修改再刷新)")
    private DateTime updateTime;

    @ApiModelProperty(value = "物料状态名称")
    private String statusName;

    @ApiModelProperty(value = "库存状态名称")
    private String situationName;

    @ApiModelProperty(value = "物料型号名称")
    private String modelName;

    @ApiModelProperty(value = "工单ID")
    private Long workId;

    @ApiModelProperty(value = "厂商应还入库单号ID")
    private Long incomeId;

    @ApiModelProperty(value = "设备型号ID")
    private Long deviceModelId;

    @ApiModelProperty(value = "设备出厂序列号")
    private String deviceSn;
}
