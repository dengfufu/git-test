package com.zjft.usp.zj.work.cases.atmcase.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.dto.WoResult;
import com.zjft.usp.zj.work.cases.atmcase.composite.AtmCaseCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.*;
import com.zjft.usp.zj.work.cases.atmcase.dto.file.ImageDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.DepotInfoDto;
import com.zjft.usp.zj.work.cases.atmcase.filter.AtmCaseFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.CaseDelayFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ATM机CASE 前端控制器
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:40
 **/
@Api(tags = "ATM机CASE请求")
@RestController
@RequestMapping("/zj/atmcase")
public class AtmCaseController {
    @Autowired
    private AtmCaseCompoService atmCaseCompoService;

    @ApiOperation(value = "是否存在相同CASE类型开启状态的CASE")
    @PostMapping("/ifExistSameMachineCase")
    public Result<CaseCheckDto> ifExistSameMachineCase(@RequestBody AtmCaseFilter atmCaseFilter,
                                                       @LoginUser UserInfo userInfo,
                                                       @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.ifExistSameMachineCase(atmCaseFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "检查CASE状态")
    @PostMapping("/checkCaseStatus")
    public Result checkCaseStatus(@RequestParam("workCode") String workCode,
                                  @LoginUser UserInfo userInfo,
                                  @CommonReqParam ReqParam reqParam) {
        atmCaseCompoService.checkCaseStatus(workCode, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "检查CASE状态")
    @PostMapping("/checkCaseStatusWoResult")
    public Result<WoResult> checkCaseStatusWoResult(@RequestParam("workCode") String workCode,
                                                    @LoginUser UserInfo userInfo,
                                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.checkCaseStatusWoResult(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "关闭CASE检查是否已做人脸识别签到")
    @PostMapping("/checkFaceSignForCloseCase")
    public Result<WoResult> checkFaceSignForCloseCase(@RequestParam("workCode") String workCode,
                                                      @LoginUser UserInfo userInfo,
                                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.checkFaceSignForCloseCase(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "关闭CASE检查是否已做行方陪同人员签到")
    @PostMapping("/checkEscortSignInForCloseCase")
    public Result<WoResult> checkEscortSignInForCloseCase(@RequestParam("workCode") String workCode,
                                                          @LoginUser UserInfo userInfo,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.checkEscortSignInForCloseCase(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "获得派工单信息，用于建CASE")
    @PostMapping("/findWorkOrderById")
    public Result<WorkOrderDto> findWorkOrderById(@RequestParam("workOrderId") String workOrderId,
                                                  @LoginUser UserInfo userInfo,
                                                  @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.findWorkOrderById(workOrderId, userInfo, reqParam));
    }

    @ApiOperation(value = "建立ATM机CASE")
    @PostMapping("/addCaseSubmit")
    public Result addCaseSubmit(@RequestBody CaseAddDto caseAddDto,
                                @LoginUser UserInfo userInfo,
                                @CommonReqParam ReqParam reqParam) {
        atmCaseCompoService.addCaseSubmit(caseAddDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "进入工程师签到页面")
    @GetMapping("/sign/{caseId}")
    public Result<CaseSignDto> sign(@PathVariable("caseId") String caseId,
                                    @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.sign(caseId, userInfo, reqParam));
    }

    @ApiOperation(value = "处理工程师签到请求")
    @PostMapping("/signSubmit")
    public Result<WoResult> signSubmit(@RequestBody CaseSignDto caseSignDto,
                                       @LoginUser UserInfo userInfo,
                                       @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.signSubmit(caseSignDto, userInfo, reqParam));
    }

    @ApiOperation(value = "查看CASE详情")
    @GetMapping("/detail/{caseId}")
    public Result<CaseDetailDto> viewCaseDetail(@PathVariable("caseId") String caseId, @LoginUser UserInfo userInfo,
                                                @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.viewCaseDetail(caseId, userInfo, reqParam));
    }

    @ApiOperation(value = "修改CASE提交")
    @PostMapping("/modCaseSubmit")
    public Result<WoResult> modCaseSubmit(@RequestBody CaseModDto caseModDto,
                                         @LoginUser UserInfo userInfo,
                                         @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.modCaseSubmit(caseModDto, userInfo, reqParam));
    }

    @ApiOperation(value = "进入取消工单页面")
    @GetMapping("/cancelCase/{workCode}")
    public Result<Map<String, Object>> cancelCase(@PathVariable("workCode") String workCode,
                             @LoginUser UserInfo userInfo,
                             @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.cancelCase(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "取消CASE提交")
    @PostMapping("/cancelCaseSubmit")
    public Result<WoResult> cancelCaseSubmit(@RequestBody CaseCancelDto caseCancelDto,
                                            @LoginUser UserInfo userInfo,
                                            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.cancelCaseSubmit(caseCancelDto, userInfo, reqParam));
    }

    @ApiOperation(value = "进入工程师延期页面")
    @GetMapping("/delay/{caseId}")
    public Result<CaseDelayDto> delayCase(@PathVariable("caseId") String caseId,
                                          @LoginUser UserInfo userInfo,
                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.delayCase(caseId, userInfo, reqParam));
    }

    @ApiOperation(value = "延期CASE提交")
    @PostMapping("/delayCaseSubmit")
    public Result delayCaseSubmit(@RequestBody CaseDelayDto caseDelayDto,
                                  @LoginUser UserInfo userInfo,
                                  @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.delayCaseSubmit(caseDelayDto, userInfo, reqParam));
    }

    /**
     * 计算预计完成时间
     *
     * @param userInfo
     * @param reqParam
     * @throws IOException
     */
    @PostMapping(value = "/calcExpectCompletionTime")
    public Result calcExpectCompletionTime(@RequestBody CaseDelayFilter atmDelayFilter,
                                           @LoginUser UserInfo userInfo,
                                           @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.calcExpectCompletionTime(
                atmDelayFilter.getWorkCode(),
                atmDelayFilter.getReBookTime(), userInfo, reqParam));
    }

    @ApiOperation(value = "处理关注CASE请求")
    @PostMapping("/concernCaseSubmit")
    public Result concernCaseSubmit(@RequestBody CaseDelayDto caseDelayDto,
                                    @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.concernCaseSubmit(caseDelayDto, userInfo, reqParam));
    }

    @ApiOperation(value = "处理监控CASE请求")
    @PostMapping("/dealMonitorCaseSubmit")
    public Result<WoResult> dealMonitorCaseSubmit(@RequestBody CaseMonitorDto caseMonitorDto,
                                                  @LoginUser UserInfo userInfo,
                                                  @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.dealMonitorCase(caseMonitorDto, userInfo, reqParam));
    }

    @ApiOperation(value = "进入关闭CASE页面")
    @GetMapping("/finish/{workCode}")
    public Result<CaseFinishPageDto> finishCase(@PathVariable("workCode") String workCode,
                                                @LoginUser UserInfo userInfo,
                                                @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.finishCase(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "关闭CASE提交")
    @PostMapping("/finishCaseSubmit")
    public Result<WoResult> finishCaseSubmit(@RequestBody Map<String, Object> requestMap,
                                            @LoginUser UserInfo userInfo,
                                            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.finishCaseSubmit(requestMap, userInfo, reqParam));
    }

    @ApiOperation(value = "根据CASE编号查找照片记录")
    @GetMapping("/listPic/{workCode}")
    public Result<List<ImageDto>> listPicByWorkCode(@PathVariable("workCode") String workCode,
                                                    @LoginUser UserInfo userInfo,
                                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.listPicByWorkCode(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "检索工单类型")
    @PostMapping("/findAppTypeAndSerType")
    public Result findAppTypeAndSerType(@RequestParam("workType") String workType,
                                        @RequestParam("workSubType") Integer workSubType,
                                        @LoginUser UserInfo userInfo,
                                        @CommonReqParam ReqParam reqParam) {
        return Result.succeedStr(this.atmCaseCompoService.findAppTypeAndSerType(workType, workSubType, userInfo, reqParam));
    }

    @ApiOperation(value = "校验关闭CASE")
    @PostMapping("/closeCaseOtherCheck")
    public Result<Map<String, Object>> closeCaseOtherCheck(@RequestBody AtmCaseFilter atmCaseFilter,
                                                           @LoginUser UserInfo userInfo,
                                                           @CommonReqParam ReqParam reqParam) {
        return Result.succeedStr(this.atmCaseCompoService.closeCaseOrtherCheck(atmCaseFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "检查定位信息")
    @GetMapping("/checkPosition/{workCode}")
    public Result<Map<String, Object>> checkPosition(@PathVariable("workCode") String workCode,
                                                     @LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.checkPosition(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "检查CASE照片上传")
    @GetMapping("/checkNeedUploadPic/{workCode}")
    public Result<Map<String, Object>> checkNeedUploadPic(@PathVariable("workCode") String workCode,
                                                          @LoginUser UserInfo userInfo,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.checkNeedUploadPic(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "根据新平台工单状态查询CASE信息")
    @PostMapping("/listCaseByWorkStatus")
    public Result<ListWrapper<CaseDto>> listCaseByWorkStatus(@RequestBody AtmCaseFilter atmCaseFilter,
                                                             @LoginUser UserInfo userInfo,
                                                             @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.listCaseByWorkStatus(atmCaseFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获得工程师的库房列表")
    @PostMapping("/listDepotByEngineer")
    public Result<List<DepotInfoDto>> listDepotByEngineer(@RequestParam("serviceBranch") String serviceBranch,
                                                          @RequestParam("engineerId") String engineerId,
                                                          @LoginUser UserInfo userInfo,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.listDepotByEngineer(serviceBranch, engineerId, userInfo, reqParam));
    }

    @ApiOperation(value = "检查CASE")
    @PostMapping("/checkYjCase")
    public Result<String> checkYjCase(@RequestParam("workCode") String workCode,
                                      @LoginUser UserInfo userInfo,
                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseCompoService.checkYjCase(workCode, userInfo, reqParam));
    }

    @ApiOperation(value = "查询工程师已关闭未审核CASE")
    @PostMapping("/listCloseNoAuditCase")
    public Result<List<CaseDto>> listCloseNoAuditCase(@LoginUser UserInfo userInfo,
                                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.listCloseNoAuditCase(userInfo, reqParam));
    }

    @ApiOperation(value = "上传位置信息")
    @PostMapping("/uploadLocation")
    public Result<WoResult> uploadLocation(@RequestParam("workCode") String workCode,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.atmCaseCompoService.uploadLocation(workCode, userInfo, reqParam));
    }

}
