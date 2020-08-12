package com.zjft.usp.anyfix.work.sign.controller;


import com.zjft.usp.anyfix.work.sign.dto.WorkSignDto;
import com.zjft.usp.anyfix.work.sign.service.WorkSignService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 工单签到表 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-09-23
 */
@Api(tags = "工单签到")
@RestController
@RequestMapping("/work-sign")
public class WorkSignController {

    @Autowired
    private WorkSignService workSignService;

    @ApiOperation(value = "工程师签到")
    @PostMapping(value = "/engineer/sign")
    public Result signWork(@RequestBody WorkSignDto WorkSignDto,
                           @LoginUser UserInfo userInfo,
                           @CommonReqParam ReqParam reqParam) {
        workSignService.signWork(WorkSignDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "查询签到记录")
    @GetMapping(value = "/query/{signId}")
    public Result<WorkSignDto> querySign(@PathVariable("signId") Long signId) {
        return Result.succeed(workSignService.querySign(signId));
    }

}
