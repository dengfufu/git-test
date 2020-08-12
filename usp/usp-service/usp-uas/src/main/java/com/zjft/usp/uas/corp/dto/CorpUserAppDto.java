package com.zjft.usp.uas.corp.dto;

import com.zjft.usp.uas.corp.model.CorpUserApp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * TODO
 *
 * @author canlei
 * @version 1.0
 * @date 2019-09-27 14:44
 **/
@Getter
@Setter
public class CorpUserAppDto extends CorpUserApp {

    @ApiModelProperty("加入企业, true:自动、false:手动审核")
    private Boolean autoJoin;

    @ApiModelProperty("自动加入企业的动态验证码")
    private String captcha;

    @ApiModelProperty("角色编号列表")
    private List<Long> roleIdList;

    @ApiModelProperty("公司名称")
    private String corpName;

    @ApiModelProperty("审核结果")
    private String checkResult;

    @ApiModelProperty("审核人姓名")
    private String checkUserName;

    @ApiModelProperty("申请人手机号")
    private String mobile;

}
