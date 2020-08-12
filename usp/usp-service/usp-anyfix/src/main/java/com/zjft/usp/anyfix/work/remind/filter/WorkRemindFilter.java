package com.zjft.usp.anyfix.work.remind.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单预警filter
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-23 13:38
 **/
@ApiModel(value = "工单预警filter")
@Data
public class WorkRemindFilter extends Page {
    @ApiModelProperty(value = "委托商编号")
    private long demanderCorp;

    @ApiModelProperty(value = "工单类型编号")
    private int workType;

    @ApiModelProperty(value = "是否启用")
    private String enabled;

    @ApiModelProperty(value = "客户")
    private Long customCorp;

    @ApiModelProperty(value = "客户ID")
    private Long customId;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "设备小类")
    private Long smallClassId;

    @ApiModelProperty(value = "设备型号")
    private Long deviceModel;

    @ApiModelProperty(value = "设备品牌")
    private Long deviceBrand;

    @ApiModelProperty(value = "服务网点")
    private String serviceBranches;

    @ApiModelProperty(value = "工单预警类型")
    private String[] remindTypes;
}
