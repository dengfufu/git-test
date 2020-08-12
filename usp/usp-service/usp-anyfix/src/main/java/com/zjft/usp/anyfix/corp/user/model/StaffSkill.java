package com.zjft.usp.anyfix.corp.user.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工程师技能表
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("staff_skill")
@ApiModel("工程师技能")
public class StaffSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("服务委托方企业")
    private Long demanderCorp;

    @ApiModelProperty("工单类型多个用“,”隔开")
    private String workTypes;

    @ApiModelProperty("设备大类")
    private Long largeClassId;

    @ApiModelProperty("设备小类，多个用“,”隔开")
    private String smallClassIds;

    @ApiModelProperty("设备品牌，多个用“,”隔开")
    private String brandIds;

    @ApiModelProperty("设备型号，多个用“,”隔开")
    private String modelIds;


}
