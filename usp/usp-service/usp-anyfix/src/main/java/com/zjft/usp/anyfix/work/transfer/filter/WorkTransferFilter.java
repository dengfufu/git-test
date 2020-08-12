package com.zjft.usp.anyfix.work.transfer.filter;

import com.zjft.usp.anyfix.work.transfer.model.WorkTransfer;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-03-02 10:04
 * @Version 1.0
 */
@ApiModel(value = "工单流转表过滤条件")
@Data
public class WorkTransferFilter extends WorkTransfer{

    /**操作月份*/
    private String operateMonth;

    /**流转方式列表*/
    private List<Integer> modeList;
}
