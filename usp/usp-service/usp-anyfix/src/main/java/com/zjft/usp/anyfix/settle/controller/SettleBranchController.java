package com.zjft.usp.anyfix.settle.controller;

import com.zjft.usp.anyfix.settle.dto.SettleBranchDto;
import com.zjft.usp.anyfix.settle.filter.SettleBranchFilter;
import com.zjft.usp.anyfix.settle.service.SettleBranchService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 网点结算单 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Api(tags = "网点结算")
@RestController
@RequestMapping("/settle-branch")
public class SettleBranchController {

    @Autowired
    private SettleBranchService settleBranchService;

    @ApiOperation(value = "添加网点结算单")
    @PostMapping(value = "/add")
    public Result addBranchSettle(@RequestBody SettleBranchDto branchSettleDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam){
        return this.settleBranchService.batchAddBranchSettle(branchSettleDto, userInfo.getUserId());
    }

    @ApiOperation(value = "根据id获取网点结算单")
    @GetMapping(value = "/{id}")
    public Result getBranchSettleById(@PathVariable long id){
        Assert.notNull(id, "id 不能为空！");
        return Result.succeed(this.settleBranchService.getById(id));
    }

    @ApiOperation(value = "查询网点结算单")
    @PostMapping(value = "/query")
    public Result listBranchSettle(@RequestBody SettleBranchFilter settleBranchFilter){
        return Result.succeed(this.settleBranchService.pageByFilter(settleBranchFilter));
    }

    @ApiOperation(value = "删除网点结算单")
    @DeleteMapping(value = "/{settleId}")
    public Result deleteById(@PathVariable("settleId") Long settleId){
        Assert.notNull(settleId, "结算单编号不能为空！");
        this.settleBranchService.deleteById(settleId);
        return Result.succeed("删除成功！");
    }

}
