package com.zjft.usp.anyfix.work.request.controller;

import com.zjft.usp.anyfix.work.request.composite.WorkStatCompoService;
import com.zjft.usp.anyfix.work.request.dto.WorkStatAreaDto;
import com.zjft.usp.anyfix.work.request.dto.WorkStatDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 工单统计 控制层
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/23 11:07 上午
 **/
@Api(tags = "工单统计")
@RestController
@RequestMapping("/work-stat")
public class WorkStatController {

    @Autowired
    private WorkStatCompoService workStatCompoService;

    @ApiOperation("统计各个状态的工单数量")
    @PostMapping(value = "/status/count")
    public Result<List<WorkStatDto>> countWorkStatus(@RequestBody WorkFilter workFilter,
                                                     @LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workStatCompoService.countWorkStatus(workFilter, userInfo, reqParam));
    }

    @ApiOperation("统计当前用户各个状态的工单数量")
    @PostMapping(value = "/user/status/count")
    public Result<List<WorkStatDto>> countUserWorkStatus(@RequestBody WorkFilter workFilter,
                                                         @LoginUser UserInfo userInfo,
                                                         @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workStatCompoService.countUserWorkStatus(workFilter, userInfo, reqParam));
    }

    @ApiOperation("统计当前用户需录入费用的工单数量")
    @PostMapping(value = "/user/work-fee/count")
    public Result<Integer> countUserWorkFee(@RequestBody WorkFilter workFilter,
                                            @LoginUser UserInfo userInfo,
                                            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workStatCompoService.countUserWorkFee(workFilter, userInfo, reqParam));
    }

    @ApiOperation("统计当前用户审核不通过的工单数量")
    @PostMapping(value = "/user/reject/count")
    public Result<Integer> countUserReject(@RequestBody WorkFilter workFilter,
                                            @LoginUser UserInfo userInfo,
                                            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workStatCompoService.countUserReject(workFilter, userInfo, reqParam));
    }

    @ApiOperation("查询前四个区域的工单统计量和增长量")
    @PostMapping(value = "/area/count")
    public Result<List<WorkStatAreaDto>> countWorkArea(@RequestBody WorkFilter workFilter,
                                                       @LoginUser UserInfo userInfo,
                                                       @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workStatCompoService.countWorkArea(workFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "统计待处理工单状态数量", notes = "包括新建和服务完成工单")
    @PostMapping(value = "/deal/count")
    public Result<Map<String, Object>> countWorkDeal(@RequestBody WorkFilter workFilter,
                                                     @LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workStatCompoService.countWorkDeal(workFilter, userInfo, reqParam));
    }

}
