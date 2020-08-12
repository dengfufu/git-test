package com.zjft.usp.anyfix.corp.user.filter;


import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工程师技能表filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-15 09:35
 **/
@Getter
@Setter
public class StaffSkillFilter extends Page {

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("工程师姓名")
    private String userName;

    @ApiModelProperty("用户编号")
    private Long userId;

    @ApiModelProperty("工单类型编号")
    private Long workType;

    @ApiModelProperty("设备大类")
    private Long largeClassId;

    @ApiModelProperty("设备小类")
    private Long smallClassId;

    @ApiModelProperty("设备品牌")
    private Long brandId;

    @ApiModelProperty("设备型号")
    private Long modelId;

}
