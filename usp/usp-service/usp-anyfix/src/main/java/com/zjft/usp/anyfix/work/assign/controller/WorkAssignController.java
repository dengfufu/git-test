package com.zjft.usp.anyfix.work.assign.controller;

import com.zjft.usp.anyfix.corp.user.dto.ServiceBranchUserDto;
import com.zjft.usp.anyfix.work.assign.dto.WorkAssignDto;
import com.zjft.usp.anyfix.work.assign.filter.WorkAssignFilter;
import com.zjft.usp.anyfix.work.assign.service.WorkAssignService;
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

/**
 * 派单信息控制层
 *
 * @author zphu
 * @date 2019/9/23 9:20
 * @Version 1.0
 **/
@Api(tags = "派单")
@RestController
@RequestMapping("/work-assign")
public class WorkAssignController {

    @Autowired
    private WorkAssignService workAssignService;

    @ApiOperation(value = "获得可派单工程师列表")
    @PostMapping("/engineers")
    public Result<ListWrapper<ServiceBranchUserDto>> listEngineer(@RequestBody WorkAssignFilter workAssignFilter,
                                                                  @LoginUser UserInfo userInfo,
                                                                  @CommonReqParam ReqParam reqParam) {
        workAssignFilter.setUserId(userInfo.getUserId());
        workAssignFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(workAssignService.listEngineer(workAssignFilter));
    }

    @ApiOperation(value = "获得所有工程师列表")
    @PostMapping("/allEngineers")
    public Result<ListWrapper<ServiceBranchUserDto>> listAllEngineer(@RequestBody WorkAssignFilter workAssignFilter,
                                                                  @LoginUser UserInfo userInfo,
                                                                  @CommonReqParam ReqParam reqParam) {
        workAssignFilter.setUserId(userInfo.getUserId());
        workAssignFilter.setCorpId(reqParam.getCorpId());
        workAssignFilter.setForAll("1");
        return Result.succeed(workAssignService.listEngineer(workAssignFilter));
    }

    @ApiOperation(value = "派单")
    @PostMapping("/assign")
    public Result assignWork(@RequestBody WorkAssignDto workAssignDto,
                             @LoginUser UserInfo userInfo,
                             @CommonReqParam ReqParam reqParam) {
        workAssignService.assignWork(workAssignDto, userInfo, reqParam);
        workAssignService.assignWorkListener(workAssignDto.getWorkId());
        return Result.succeed();
    }

    @ApiOperation(value = "转派单")
    @PostMapping("/turn/assign")
    public Result turnAssignWork(@RequestBody WorkAssignDto workAssignDto,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        workAssignService.turnAssignWork(workAssignDto, userInfo, reqParam);
        workAssignService.assignWorkListener(workAssignDto.getWorkId());
        return Result.succeed();
    }

}
