package com.zjft.usp.zj.work.baseinfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * CASE基础数据DTO类
 *
 * @author zgpi
 * @date 2020/3/23 20:14
 */
@ApiModel(value = "CASE基础数据DTO类")
@Data
public class CaseBaseDto implements Serializable {

    private static final long serialVersionUID = 8732633839884850098L;

    @ApiModelProperty(value = "工程师用户名")
    private String userId;

    @ApiModelProperty(value = "工程师编号")
    private Long engineerId;

    @ApiModelProperty(value = "工程师姓名", notes = "包含城市")
    private String engineerName;

    @ApiModelProperty(value = "当前时间")
    private String currentTime;

    @ApiModelProperty(value = "保修状态")
    private Map<String, String> warrantyMap;

    @ApiModelProperty(value = "分布列表")
    private List<AreaDto> areaList;

    @ApiModelProperty(value = "权限")
    private String rightA;
}
