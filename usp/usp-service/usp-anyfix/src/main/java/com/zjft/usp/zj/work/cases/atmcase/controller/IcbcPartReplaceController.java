package com.zjft.usp.zj.work.cases.atmcase.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.composite.IcbcPartReplaceCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcPartReplaceDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcPartReplaceListDto;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcPartReplaceFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 工行对接维修登记 前端控制器
 *
 * @author JFZOU
 * @version 1.0
 * @date 2020-03-13 16:40
 **/
@Api(tags = "工行对接维修登记请求")
@RestController
@RequestMapping("/zj/icbc/replace")
public class IcbcPartReplaceController {

    @Autowired
    private IcbcPartReplaceCompoService icbcPartReplaceCompoService;

    @ApiOperation(value = "进入工行维修登记页面")
    @GetMapping("/list/{caseId}")
    public Result<IcbcPartReplaceListDto> listIcbcReplace(
            @PathVariable("caseId") String caseId,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(icbcPartReplaceCompoService.listBy(caseId, userInfo, reqParam));
    }

    @ApiOperation(value = "进入添加工行维修登记页面")
    @PostMapping("/addReplace")
    public Result<IcbcPartReplaceDto> addIcbcReplace(@LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam,
                                                     @RequestBody IcbcPartReplaceFilter icbcPartReplaceFilter) {
        return Result.succeed(icbcPartReplaceCompoService.addIcbcReplace(icbcPartReplaceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "处理添加工行维修登记请求")
    @PostMapping("/addReplaceSubmit")

    public Result<Integer> addIcbcReplaceSubmit(@LoginUser UserInfo userInfo,
                                                @CommonReqParam ReqParam reqParam,
                                                @RequestBody IcbcPartReplaceDto icbcPartReplaceDto) {
        return Result.succeed(icbcPartReplaceCompoService.addIcbcReplaceSubmit(userInfo, reqParam, icbcPartReplaceDto));
    }

    @ApiOperation(value = "进入修改工行维修登记页面")
    @PostMapping("/modReplace")
    public Result<IcbcPartReplaceDto> modIcbcReplace(@LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam,
                                                     @RequestBody IcbcPartReplaceFilter icbcPartReplaceFilter) {
        return Result.succeed(icbcPartReplaceCompoService.modIcbcReplace(icbcPartReplaceFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "处理修改工行维修登记请求")
    @PostMapping("/modReplaceSubmit")
    public Result<Integer> modIcbcReplaceSubmit(@LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam,
                                                     @RequestBody IcbcPartReplaceDto icbcPartReplaceDto) {
        return Result.succeed(icbcPartReplaceCompoService.modIcbcReplaceSubmit(icbcPartReplaceDto, userInfo, reqParam));
    }

    @ApiOperation(value = "处理删除工行维修登记请求")
    @GetMapping("/delete/{replaceId}")
    public Result<Integer> delIcbcReplaceSubmit(
            @PathVariable("replaceId") Long replaceId,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam
            ) {
        return Result.succeed(icbcPartReplaceCompoService.deleteIcbcReplaceSubmit(replaceId, userInfo, reqParam));
    }
}
