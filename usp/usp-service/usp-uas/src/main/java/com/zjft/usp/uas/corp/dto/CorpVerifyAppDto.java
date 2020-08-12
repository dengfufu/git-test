package com.zjft.usp.uas.corp.dto;

import com.zjft.usp.uas.corp.model.CorpVerifyApp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * TODO
 *
 * @author user
 * @version 1.0
 * @date 2019-09-27 14:21
 **/
@Getter
@Setter
public class CorpVerifyAppDto extends CorpVerifyApp {

    @ApiModelProperty("公司名称")
    private String corpName;

    @ApiModelProperty("审核结果")
    private String checkResult;

    @ApiModelProperty("状态List")
    private List<Short> statusList;

    @ApiModelProperty("行政区划名称")
    private String region;

    @ApiModelProperty("申请人真实姓名")
    private String applyUserName;

}
