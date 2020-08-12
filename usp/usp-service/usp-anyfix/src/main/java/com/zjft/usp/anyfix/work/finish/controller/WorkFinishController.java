package com.zjft.usp.anyfix.work.finish.controller;


import com.zjft.usp.anyfix.work.finish.composite.WorkFinishCompoService;
import com.zjft.usp.anyfix.work.finish.dto.WorkFinishDto;
import com.zjft.usp.anyfix.work.finish.service.WorkFinishService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
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

/**
 * <p>
 * 工单服务完成表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@Api(tags = "服务完成")
@RestController
@RequestMapping("/work-finish")
public class WorkFinishController {

    @Autowired
    private WorkFinishService workFinishService;
    @Autowired
    private WorkFinishCompoService workFinishCompoService;

    @ApiOperation(value = "工程师现场服务工单")
    @PostMapping(value = "/engineer/locale/finish")
    public Result localeServiceWork(@RequestBody WorkFinishDto workFinishDto,
                                    @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        workFinishCompoService.localeServiceWork(workFinishDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "工程师远程服务工单")
    @PostMapping(value = "/engineer/remote/finish")
    public Result remoteServiceWork(@RequestBody WorkFinishDto workFinishDto,
                                    @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        workFinishService.remoteServiceWork(workFinishDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "工程师补录工单")
    @PostMapping(value = "/engineer/supplement")
    public Result supplementWork(@RequestBody WorkDto workDto,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        List<Long> workIdList = workFinishCompoService.supplementCreateWork(workDto, userInfo, reqParam);
        return Result.succeed(workIdList.get(0));
    }

    @ApiOperation(value = "工程师补传工单附件")
    @PostMapping(value = "/engineer/supplementFiles")
    public Result supplementFiles(@RequestBody WorkFinishDto workFinishDto,
                                  @LoginUser UserInfo userInfo,
                                  @CommonReqParam ReqParam reqParam) {
        workFinishCompoService.supplementFiles(workFinishDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "工程师修改现场完成信息")
    @PostMapping(value = "/update/locale/finish")
    public Result updateLocaleFinish(@RequestBody WorkFinishDto workFinishDto,
                                     @LoginUser UserInfo userInfo,
                                     @CommonReqParam ReqParam reqParam) {
        this.workFinishCompoService.updateLocaleFinish(workFinishDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "工程师修改工单费用")
    @PostMapping(value = "/update/work-fee")
    public Result updateWorkFee(@RequestBody WorkFinishDto workFinishDto,
                                @LoginUser UserInfo userInfo,
                                @CommonReqParam ReqParam reqParam) {
        this.workFinishCompoService.updateWorkFee(workFinishDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "修改服务内容或附件")
    @PostMapping(value = "/mod")
    public Result modFinish(@RequestBody WorkDto workDto, @RequestParam("type") String type,
                            @LoginUser UserInfo userInfo,
                            @CommonReqParam ReqParam reqParam) {
        workFinishCompoService.modFinish(workDto, type, userInfo, reqParam);
        return Result.succeed();
    }


}
