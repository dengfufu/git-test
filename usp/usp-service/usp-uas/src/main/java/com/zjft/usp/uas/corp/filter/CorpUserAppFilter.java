package com.zjft.usp.uas.corp.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

/**
 * 加入企业申请filter
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-11 19:51
 **/
@Getter
@Setter
public class CorpUserAppFilter extends Page {

    @ApiModelProperty("企业编号")
    private Long corpId;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("申请时间起始")
    private List<Timestamp> applyTimeRange;

    @ApiModelProperty("审核状态list")
    private List<Integer> statusList;

    @ApiModelProperty("企业编号")
    private Integer status;

}
