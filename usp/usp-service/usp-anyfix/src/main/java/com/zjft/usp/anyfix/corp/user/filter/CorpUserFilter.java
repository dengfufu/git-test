package com.zjft.usp.anyfix.corp.user.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/11/27 15:14
 */
@ApiModel("企业人员")
@Getter
@Setter
public class CorpUserFilter extends Page {

    @ApiModelProperty(value = "企业编号")
    private Long corpId;

    @ApiModelProperty(value = "人员编号")
    private Long userId;

    @ApiModelProperty("用户编号列表")
    private List<Long> userIdList;

    @ApiModelProperty(value = "服务网点")
    private Long serviceBranch;

    @ApiModelProperty(value = "设备网点")
    private Long deviceBranch;
}
