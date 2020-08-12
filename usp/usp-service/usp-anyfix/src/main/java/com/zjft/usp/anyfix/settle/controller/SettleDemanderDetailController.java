package com.zjft.usp.anyfix.settle.controller;


import com.zjft.usp.anyfix.settle.dto.SettleDemanderDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleDemanderDetailFilter;
import com.zjft.usp.anyfix.settle.service.SettleDemanderDetailService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 供应商结算单明细 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
@Api(tags = "供应商结算单明细")
@RestController
@RequestMapping("/settle-demander-detail")
public class SettleDemanderDetailController {

    @Autowired
    private SettleDemanderDetailService settleDemanderDetailService;

    @ApiOperation("查询结算对账单明细")
    @PostMapping(value = "/listSettleVerify")
    public Result<List<WorkFeeVerifyDto>> listVerifyByFilter(@RequestBody SettleDemanderDetailFilter settleDemanderDetailFilter) {
        return Result.succeed(this.settleDemanderDetailService.listWorkFeeVerifyByFilter(settleDemanderDetailFilter));
    }

    @ApiOperation("分页查询结算对账单明细")
    @PostMapping(value = "/querySettleVerify")
    public Result<ListWrapper<WorkFeeVerifyDto>> queryVerifyByFilter(@RequestBody SettleDemanderDetailFilter settleDemanderDetailFilter) {
        return Result.succeed(this.settleDemanderDetailService.queryWorkFeeVerifyByFilter(settleDemanderDetailFilter));
    }

    @ApiOperation(value = "查询结算工单")
    @PostMapping(value = "/listSettleWork")
    public Result<List<WorkFeeDto>> listWorkByFilter(@RequestBody SettleDemanderDetailFilter settleDemanderDetailFilter) {
        return Result.succeed(this.settleDemanderDetailService.listWorkByFilter(settleDemanderDetailFilter));
    }

    @ApiOperation(value = "分页查询结算工单")
    @PostMapping(value = "/querySettleWork")
    public Result<ListWrapper<WorkFeeDto>> queryWorkByFilter(@RequestBody SettleDemanderDetailFilter settleDemanderDetailFilter) {
        return Result.succeed(this.settleDemanderDetailService.queryWorkByFilter(settleDemanderDetailFilter));
    }

}
