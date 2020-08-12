package com.zjft.usp.zj.work.cases.atmcase.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.dto.WoResult;
import com.zjft.usp.zj.work.cases.atmcase.composite.IcbcMediaReplaceCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcMediaDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcMediaListDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.OldCaseDto;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaAddFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaCloseFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaDeleteFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaQueryFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 工行对接介质更换 前端控制器
 *
 * @author JFZOU
 * @version 1.0
 * @date 2020-03-17 16:40
 **/
@Api(tags = "工行对接介质更换请求")
@RestController
@RequestMapping("/zj/icbc/media")
public class IcbcMediaReplaceController {

    @Autowired
    private IcbcMediaReplaceCompoService icbcMediaReplaceCompoService;

    @ApiOperation(value = "进入工行介质交接列表")
    @PostMapping("/list")
    public Result<IcbcMediaListDto> listMediaReplace(
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam,
            @RequestBody IcbcMediaQueryFilter icbcMediaQueryFilter) {
        return Result.succeed(icbcMediaReplaceCompoService.listMediaReplace(userInfo, reqParam, icbcMediaQueryFilter));
    }


    @ApiOperation(value = "进入添加工行介质交接页面")
    @PostMapping("/addMediaReplace")
    public Result<OldCaseDto> addMediaReplace(@LoginUser UserInfo userInfo,
                                              @CommonReqParam ReqParam reqParam,
                                              @RequestBody IcbcMediaAddFilter icbcMediaAddFilter) {
        return Result.succeed(icbcMediaReplaceCompoService.addMediaReplace(userInfo, reqParam, icbcMediaAddFilter));
    }

    @ApiOperation(value = "处理添加工行介质交接请求")
    @PostMapping("/addMediaReplaceSubmit")
    public Result<Integer> addMediaReplaceSubmit(@LoginUser UserInfo userInfo,
                                                 @CommonReqParam ReqParam reqParam,
                                                 @RequestBody IcbcMediaDto icbcMediaDto) {
        return Result.succeed(icbcMediaReplaceCompoService.addMediaReplaceSubmit(userInfo, reqParam, icbcMediaDto));
    }

    @ApiOperation(value = "进入修改工行介质交接页面")
    @GetMapping("/modMediaReplace/{replaceId}")
    public Result<IcbcMediaDto> modMediaReplace(@LoginUser UserInfo userInfo,
                                                @CommonReqParam ReqParam reqParam,
                                                @PathVariable("replaceId") Long replaceId) {
        return Result.succeed(icbcMediaReplaceCompoService.modMediaReplace(userInfo, reqParam, replaceId));
    }

    @ApiOperation(value = "处理修改工行介质交接请求")
    @PostMapping("/modMediaReplaceSubmit")

    public Result<Integer> modMediaReplaceSubmit(@LoginUser UserInfo userInfo,
                                                 @CommonReqParam ReqParam reqParam,
                                                 @RequestBody IcbcMediaDto icbcMediaDto) {
        return Result.succeed(icbcMediaReplaceCompoService.modMediaReplaceSubmit(userInfo, reqParam, icbcMediaDto));
    }

    @ApiOperation(value = "处理删除工行维修登记请求")
    @PostMapping("/delIcbcReplaceSubmit")
    public Result<Integer> delIcbcReplaceSubmit(
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam,
            @RequestBody IcbcMediaDeleteFilter icbcMediaDeleteFilter
    ) {
        return Result.succeed(icbcMediaReplaceCompoService.delMediaReplaceSubmit(userInfo, reqParam, icbcMediaDeleteFilter));
    }

    @ApiOperation(value = "处理删除工行维修登记请求")
    @PostMapping("/checkMediaByClose")
    public Result<WoResult> checkMediaByClose(
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam,
            @RequestBody IcbcMediaCloseFilter icbcMediaCloseFilter
    ) {
        return Result.succeed(icbcMediaReplaceCompoService.checkMediaByClose(userInfo, reqParam, icbcMediaCloseFilter));
    }
}
