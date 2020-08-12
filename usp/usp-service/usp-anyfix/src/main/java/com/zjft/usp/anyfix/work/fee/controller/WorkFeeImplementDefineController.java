package com.zjft.usp.anyfix.work.fee.controller;


import com.zjft.usp.anyfix.work.fee.dto.WorkFeeImplementDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeImplementFilter;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeImplementDefineService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 实施发生费用定义 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
@RestController
@RequestMapping("/work-fee-implement-define")
public class WorkFeeImplementDefineController {

    @Autowired
    private WorkFeeImplementDefineService workFeeImplementDefineService;

    @ApiOperation("分页查询")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WorkFeeImplementDto>> query(@RequestBody WorkFeeImplementFilter workFeeImplementFilter,
                                                          @LoginUser UserInfo userInfo,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workFeeImplementDefineService.query(workFeeImplementFilter, reqParam));
    }

    @ApiOperation(value = "添加")
    @PostMapping(value = "/add")
    public Result add(@RequestBody WorkFeeImplementDto workFeeImplementDto,
                    @LoginUser UserInfo userInfo,
                    @CommonReqParam ReqParam reqParam) {
        this.workFeeImplementDefineService.add(workFeeImplementDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public Result update(@RequestBody WorkFeeImplementDto workFeeImplementDto,
                         @LoginUser UserInfo userInfo) {
        this.workFeeImplementDefineService.update(workFeeImplementDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "查询明细")
    @GetMapping(value = "/{implementId}")
    public Result<WorkFeeImplementDto> viewDetail(@PathVariable("implementId") Long implementId) {
        return Result.succeed(this.workFeeImplementDefineService.getDtoById(implementId));
    }

    @ApiOperation(value = "根据工单查询实施发生费用定义，包含定义与已生成的费用明细")
    @PostMapping(value = "/listByWork")
    public Result<List<WorkFeeImplementDto>> listByWorkId(@RequestBody WorkDto workDto){
        return Result.succeed(workFeeImplementDefineService.listDtoByWork(workDto));
    }

}
