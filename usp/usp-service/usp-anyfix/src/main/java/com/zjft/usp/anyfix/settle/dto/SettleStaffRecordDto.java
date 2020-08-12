package com.zjft.usp.anyfix.settle.dto;

import com.zjft.usp.anyfix.settle.model.SettleStaffRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 结算记录
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-11 15:44
 **/
@Getter
@Setter
public class SettleStaffRecordDto extends SettleStaffRecord {

    @ApiModelProperty("结算人员姓名")
    private String operatorName;

    @ApiModelProperty("结算员工编号list")
    private List<Long> userIdList;

}
