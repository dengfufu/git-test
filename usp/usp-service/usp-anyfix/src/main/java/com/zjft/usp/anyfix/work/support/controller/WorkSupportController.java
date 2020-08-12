package com.zjft.usp.anyfix.work.support.controller;


import com.zjft.usp.anyfix.corp.user.dto.CorpUserDto;
import com.zjft.usp.anyfix.work.support.dto.WorkSupportDto;
import com.zjft.usp.anyfix.work.support.service.WorkSupportService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 技术支持 前端控制器
 * </p>
 *
 * @author cxd
 * @since 2020-04-21
 */
@RestController
@RequestMapping("/work-support")
public class WorkSupportController {
    @Autowired
    private WorkSupportService workSupportService;

    @ApiOperation(value = "添加技术支持")
    @PostMapping(value = "/add")
    public Result addWorkSupport(@RequestBody WorkSupportDto workSupportDto,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workSupportDto.getCorpId())) {
            workSupportDto.setCorpId(reqParam.getCorpId());
        }
        workSupportService.addWorkSupport(workSupportDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "关闭技术支持")
    @PostMapping(value = "/finish")
    public Result finishWorkSupport(@RequestBody WorkSupportDto workSupportDto,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        workSupportService.finishWorkSupport(workSupportDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation("删除技术支持")
    @PostMapping(value = "/delWorkSupport")
    public Result delWorkSupport(@RequestBody WorkSupportDto workSupportDto,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        workSupportService.deleteByWorkId(workSupportDto.getWorkId(),userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("根据企业获取技术支持人员列表")
    @PostMapping(value = "/support/user")
    public Result<List<CorpUserDto>> queryListCorpUser(@RequestBody WorkSupportDto workSupportDto,
                                                       @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workSupportService.queryListCorpUser(workSupportDto,reqParam));
    }
}
