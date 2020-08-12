package com.zjft.usp.anyfix.work.remind.controller;

import com.zjft.usp.anyfix.work.remind.composite.WorkRemindCompoService;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindDealDto;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindMainDto;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindTypeDto;
import com.zjft.usp.anyfix.work.remind.filter.WorkRemindFilter;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-04-20 10:34
 * @Version 1.0
 */
@Api(tags = "工单预警请求")
@RestController
@RequestMapping("/work-remind")
public class WorkRemindController {
    @Autowired
    private WorkRemindCompoService workRemindCompoService;

    @ApiOperation(value = "查询工单预警设置")
    @PostMapping("/query")
    public Result<ListWrapper<WorkRemindMainDto>> queryWorkRemind(@RequestBody WorkRemindFilter workRemindFilter) {
        return Result.succeed(workRemindCompoService.queryWorkRemind(workRemindFilter));
    }

    @ApiOperation(value = "获得单个工单预警详情")
    @GetMapping(value = "/detail/{remindId}")
    public Result<WorkRemindMainDto> findWorkRemind(@PathVariable("remindId") Long remindId,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workRemindCompoService.findWorkRemind(remindId, reqParam));
    }

    @ApiOperation(value = "获取工单预警类型")
    @PostMapping("/listWorkRemindType")
    public Result<List<WorkRemindTypeDto>> listWorkRemindType() {
        return Result.succeed(workRemindCompoService.listWorkRemindType());
    }

    @ApiOperation(value = "添加工单预警设置")
    @PostMapping("/add")
    public Result addWorkRemind(@RequestBody WorkRemindMainDto workRemindMainDto, @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        workRemindCompoService.addWorkRemind(workRemindMainDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改工单预警设置")
    @PostMapping("/mod")
    public Result modWorkRemind(@RequestBody WorkRemindMainDto workRemindMainDto, @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        workRemindCompoService.modWorkRemind(workRemindMainDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "删除工单类型")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long remindId) {
        workRemindCompoService.removeById(remindId);
        return Result.succeed();
    }

    @ApiOperation(value = "修改预警时间")
    @PostMapping("/modRemindTime")
    public Result modRemindTime(@RequestBody WorkRemindDealDto workRemindDealDto, @LoginUser UserInfo userInfo) {
        workRemindCompoService.modRemindTime(workRemindDealDto, userInfo);
        return Result.succeed();
    }

}
