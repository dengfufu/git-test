package com.zjft.usp.anyfix.work.evaluate.controller;


import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto;
import com.zjft.usp.anyfix.work.evaluate.service.WorkEvaluateService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户评价表 前端控制器
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@Api(tags = "客户评价")
@RestController
@RequestMapping("/work-evaluate")
public class WorkEvaluateController {

    @Autowired
    private WorkEvaluateService workEvaluateService;

    @ApiOperation(value = "添加客户评价")
    @PostMapping("/add")
    public Result add(@RequestBody WorkEvaluateDto workEvaluateDto,
                      @LoginUser UserInfo userInfo,
                      @CommonReqParam ReqParam reqParam) {
        workEvaluateService.addWorkEvaluate(workEvaluateDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "查询客户评价信息")
    @GetMapping("/workId/{workId}")
    public Result<WorkEvaluateDto> selectByWorkId(@PathVariable("workId") Long workId) {
        return Result.succeed(workEvaluateService.selectByWorkId(workId));
    }

    @ApiOperation(value = "根据日期查询工程师收到的评价记录")
    @PostMapping("/list")
    public Result<List<WorkEvaluateDto>> listByDate(@RequestBody WorkEvaluateDto workEvaluateDto,
                                                    @LoginUser UserInfo userInfo,@CommonReqParam ReqParam reqParam) {
        return Result.succeed(workEvaluateService.listByDate(workEvaluateDto,userInfo,reqParam));
    }

    @ApiOperation(value = "根据指标和日期查询工程师收到的评价记录")
    @PostMapping("/listByIndex")
    public Result<List<WorkEvaluateDto>> listByIndex(@RequestBody WorkEvaluateDto workEvaluateDto,
                                                    @LoginUser UserInfo userInfo,@CommonReqParam ReqParam reqParam) {
        return Result.succeed(workEvaluateService.listByIndex(workEvaluateDto,userInfo,reqParam));
    }


    @ApiOperation(value = "根据指标和日期查询工程师收到的评价记录")
    @PostMapping("/count")
    public Result< Map<String,Object> > listWorkEvaluateCountDto(@RequestBody WorkEvaluateDto workEvaluateDto,
                                                                       @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workEvaluateService.listWorkEvaluateCountDto(workEvaluateDto,reqParam.getCorpId()));
    }

}
