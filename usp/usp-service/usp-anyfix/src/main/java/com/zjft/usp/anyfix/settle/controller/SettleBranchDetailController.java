package com.zjft.usp.anyfix.settle.controller;


import com.zjft.usp.anyfix.settle.dto.SettleBranchDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleBranchDetailFilter;
import com.zjft.usp.anyfix.settle.service.SettleBranchDetailService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 网点结算单明细 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Api(tags = "网点结算单明细")
@RestController
@RequestMapping("/settle-branch-detail")
public class SettleBranchDetailController {

    @Autowired
    private SettleBranchDetailService settleBranchDetailService;

    @ApiOperation(value = "根据单号查询明细")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SettleBranchDetailDto>> pageDetail(@RequestBody SettleBranchDetailFilter settleBranchDetailFilter){
        return Result.succeed(this.settleBranchDetailService.pageByFilter(settleBranchDetailFilter));
    }

}
