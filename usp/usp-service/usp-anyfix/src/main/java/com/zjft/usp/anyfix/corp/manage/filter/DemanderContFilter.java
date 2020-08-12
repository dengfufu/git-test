package com.zjft.usp.anyfix.corp.manage.filter;

import com.zjft.usp.common.model.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 委托协议filter
 */
@Data
public class DemanderContFilter extends Page {

    private Long refId;

    private Long demanderCorp;

    private Long serviceCorp;

    private Timestamp dispatchTime;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

}
