package com.zjft.usp.anyfix.work.auto.controller;

import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceBranchDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkDispatchServiceBranchFilter;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceBranchService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ljzhu
 * @date 2019-09-26 18:17
 * @note
 */
@Api(tags = "分配服务商网点规则")
@RestController
@RequestMapping("/work-dispatch-service-branch")
public class WorkDispatchServiceBranchController {

    @Autowired
    private WorkDispatchServiceBranchService workDispatchServiceBranchService;

    @ApiOperation(value = "分页查询自动分配服务商网点规则列表")
    @PostMapping(value = "/query")
    public Result listWorkDispatchServiceBranch(@RequestBody WorkDispatchServiceBranchFilter filter) {
        ListWrapper<WorkDispatchServiceBranchDto> list = workDispatchServiceBranchService.query(filter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "添加自动分配服务商网点规则")
    @PostMapping(value = "/add")
    public Result addWorkDispatchServiceBranch(@RequestBody WorkDispatchServiceBranchDto workDispatchServiceBranchDto) {
        this.workDispatchServiceBranchService.add(workDispatchServiceBranchDto);
        return Result.succeed();
    }

    @ApiOperation(value = "修改自动分配服务商网点规则")
    @PostMapping(value = "/update")
    public Result modWorkDispatchServiceBranch(@RequestBody WorkDispatchServiceBranchDto workDispatchServiceBranchDto) {
        this.workDispatchServiceBranchService.mod(workDispatchServiceBranchDto);
        return Result.succeed();
    }

    @ApiOperation(value = "删除自动分配服务商网点规则")
    @DeleteMapping(value = "/{id}")
    public Result delWorkDispatchServiceBranch(@PathVariable("id") Long id) {
        this.workDispatchServiceBranchService.delById(id);
        return Result.succeed();
    }

}
