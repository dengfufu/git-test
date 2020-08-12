package com.zjft.usp.uas.corp.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

/**
 * 企业认证申请filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-11 19:52
 **/
@Getter
@Setter
public class CorpVerifyAppFilter extends Page {

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("企业名称")
    private String corpName;

    @ApiModelProperty("状态list")
    private List<Integer> statusList;

    @ApiModelProperty("法人代表姓名")
    private String larName;

    @ApiModelProperty("申请用户编号")
    private Long applyUserId;

    @ApiModelProperty("申请人姓名")
    private String applyUserName;

    @ApiModelProperty("省编号")
    private String province;

    @ApiModelProperty("市编号")
    private String city;

    @ApiModelProperty("申请时间开始")
    private Date applyTimeStart;

    @ApiModelProperty("申请时间结束")
    private Date applyTimeEnd;

}
