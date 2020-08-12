package com.zjft.usp.anyfix.work.deal.controller;

import com.zjft.usp.anyfix.work.deal.dto.EngineerDto;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zrlin
 * @date 2019-11-04 11:29
 */
@Api(tags = "工单处理信息请求")
@RestController
@RequestMapping("/work-deal")
public class WorkDealController {

    @Autowired
    private WorkDealService workDealService;

    @ApiOperation(value = "企业工程师的接单人员列表")
    @PostMapping(value = "/engineer/list")
    public Result<List<EngineerDto>> queryEngineerDto(@RequestBody WorkFilter workFilter) {
        return Result.succeed(workDealService.queryEngineerDto(workFilter));
    }

    @ApiOperation(value = "修改协同工程师")
    @PostMapping(value = "/update/togetherEngineers")
    public Result updateTogetherEngineers(@RequestBody WorkDto workDto, @LoginUser UserInfo userInfo) {
        this.workDealService.updateTogetherEngineers(workDto, userInfo.getUserId());
        return Result.succeed();
    }

}
