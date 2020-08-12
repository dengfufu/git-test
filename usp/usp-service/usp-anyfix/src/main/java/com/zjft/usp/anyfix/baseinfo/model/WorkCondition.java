package com.zjft.usp.anyfix.baseinfo.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.model
 * @date 2019-09-26 17:49
 * @note
 */
@ApiModel("自动分配规则对象")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("work_condition")
public class WorkCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "客户关系编号", notes = "客户关系表主键")
    private Long customId;

    @ApiModelProperty(value = "客户企业编号")
    private Long customCorp;

    @ApiModelProperty(value = "供应商企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "工单类型")
    private String workType;

    @ApiModelProperty(value = "设备大类")
    private Long largeClassId;

    @ApiModelProperty(value = "设备小类")
    private String smallClassId;

    @ApiModelProperty(value = "设备品牌")
    private String brandId;

    @ApiModelProperty(value = "设备型号")
    private String modelId;

    @ApiModelProperty(value = "设备规格")
    private String specification;

    @ApiModelProperty(value = "市郊", notes = "1=市区，2=郊县")
    private Integer zone;

    @ApiModelProperty(value = "行政区划")
    private String district;

    @ApiModelProperty(value = "行政区划取反", notes = "Y=是，N=否")
    private String districtNegate;

    @ApiModelProperty(value = "设备网点")
    private String deviceBranch;

    @ApiModelProperty(value = "设备ID")
    private Long deviceId;

}
