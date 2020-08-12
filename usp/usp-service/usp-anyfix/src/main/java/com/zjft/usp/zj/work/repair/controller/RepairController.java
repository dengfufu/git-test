package com.zjft.usp.zj.work.repair.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import com.zjft.usp.zj.work.repair.composite.RepairCompoService;
import com.zjft.usp.zj.work.repair.dto.BxImgDto;
import com.zjft.usp.zj.work.repair.dto.BxSendCreateFailDto;
import com.zjft.usp.zj.work.repair.dto.RepairDto;
import com.zjft.usp.zj.work.repair.filter.RepairFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 老平台报修 前面控制器
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:46
 **/
@Api(tags = "老平台报修请求")
@RestController
@RequestMapping("/zj/repair")
public class RepairController {
    @Autowired
    private RepairCompoService repairCompoService;

    @ApiOperation(value = "进入报修单查询页面")
    @PostMapping("/queryRepair")
    public Result<Map> queryRepair(@RequestBody RepairFilter repairFilter,
                                                      @LoginUser UserInfo userInfo,
                                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.queryRepair(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "进入未关闭的报修单查询页面")
    @PostMapping("/queryUnCloseRepair")
    public Result<Map> queryUnCloseRepair(@RequestBody RepairFilter repairFilter,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.queryUnCloseRepair(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "根据条件分页查询报修单")
    @PostMapping("/listRepair")
    public Result<ListWrapper<RepairDto>> listRepair(@RequestBody RepairFilter repairFilter,
                                                      @LoginUser UserInfo userInfo,
                                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.listRepair(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "根据条件分页查询未关闭的报修单")
    @PostMapping("/listUnCloseRepair")
    public Result<ListWrapper<RepairDto>> listUnCloseRepair(@RequestBody RepairFilter repairFilter,
                                                             @LoginUser UserInfo userInfo,
                                                             @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.listUnCloseRepair(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查看报修单详情")
    @PostMapping("/findRepairDetail")
    public Result<Map> findRepairDetail(@RequestBody RepairFilter repairFilter,
                                        @LoginUser UserInfo userInfo,
                                        @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.findRepairDetail(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "进入转处理页面")
    @PostMapping("/turnHandle")
    public Result<Map> turnHandle(@RequestBody RepairFilter repairFilter,
                                  @LoginUser UserInfo userInfo,
                                  @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.turnHandle(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "转处理提交")
    @PostMapping("/turnHandleSubmit")
    public Result turnHandleSubmit(@RequestBody RepairDto repairDto, @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        repairCompoService.turnHandleSubmit(repairDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "进入电话处理页面")
    @PostMapping("/phoneHandle")
    public Result<Map> phoneHandle(@RequestBody RepairFilter repairFilter,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.phoneHandle(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "电话处理提交")
    @PostMapping("/phoneHandleSubmit")
    public Result phoneHandleSubmit(@RequestBody RepairDto repairDto,
                                    @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        repairCompoService.phoneHandleSubmit(repairDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "进入退单页面")
    @PostMapping("/returnHandle")
    public Result<Map> returnHandle(@RequestBody RepairFilter repairFilter,
                                    @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.returnHandle(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "退单提交")
    @PostMapping("/returnHandleSubmit")
    public Result returnHandleSubmit(@RequestBody RepairDto repairDto,
                                     @LoginUser UserInfo userInfo,
                                     @CommonReqParam ReqParam reqParam) {
        repairCompoService.returnHandleSubmit(repairDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "进入关联CASE页面")
    @PostMapping("/associateCase")
    public Result<Map> associateCase(@RequestBody RepairFilter repairFilter,
                                     @LoginUser UserInfo userInfo,
                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.associateCase(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "关联CASE提交")
    @PostMapping("/associateCaseSubmit")
    public Result associateCaseSubmit(@RequestBody RepairDto repairDto,
                                      @LoginUser UserInfo userInfo,
                                      @CommonReqParam ReqParam reqParam) {
        repairCompoService.associateCaseSubmit(repairDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "查询补录失败的记录")
    @PostMapping("/listBxSendCreateFail")
    public Result<ListWrapper<BxSendCreateFailDto>> listBxSendCreateFail(@RequestBody RepairFilter repairFilter,
                                                                         @LoginUser UserInfo userInfo,
                                                                         @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.listBxSendCreateFail(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "进入补录失败修改页面")
    @PostMapping("/modBxSendCreateFail")
    public Result<Map> modBxSendCreateFail(@RequestBody RepairFilter repairFilter, @LoginUser UserInfo userInfo,
                                           @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.modBxSendCreateFail(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "补录失败修改提交")
    @PostMapping("/modBxSendCreateFailSubmit")
    public Result modBxSendCreateFailSubmit(@RequestBody BxSendCreateFailDto bxSendCreateFailDto,
                                            @LoginUser UserInfo userInfo,
                                            @CommonReqParam ReqParam reqParam) {
        repairCompoService.modBxSendCreateFailSubmit(bxSendCreateFailDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "获取银行编号与名称映射Map")
    @PostMapping("/findBankMap")
    public Result<Map> findBankMap(@RequestBody RepairFilter repairFilter, @LoginUser UserInfo userInfo,
                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.findBankMap(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获取银行编号与名称映射Map")
    @PostMapping("/findBureauAndBranchMap")
    public Result<Map> findBureauAndBranchMap(@RequestBody RepairFilter repairFilter, @LoginUser UserInfo userInfo,
                                                 @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.findBureauAndBranchMap(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "根据交易号获取工行对接报修图片")
    @PostMapping("/listFaultRepairPic")
    public Result<List<BxImgDto>> listFaultRepairPic(@RequestBody RepairFilter repairFilter, @LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.listFaultRepairPic(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "查看工行对接报修图片")
    @GetMapping("/viewFaultRepairPic/corpId/{corpId}/userId/{userId}/serviceId/{serviceId}/fileId/{fileId}")
    public void viewFaultRepairPic(@PathVariable("corpId") Long corpId,
            @PathVariable("userId") Long userId,
            @PathVariable("serviceId") String serviceId,
            @PathVariable("fileId") String fileId,
            HttpServletResponse response) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);

        ReqParam reqParam = new ReqParam();
        reqParam.setCorpId(corpId);
        repairCompoService.viewFaultRepairPic(fileId, serviceId, userInfo, reqParam, response);
    }

    @ApiOperation(value = "根据报修单的ID找到与之对应的最新执行人处理的CASE")
    @PostMapping("/pickRelevanceCaseId")
    public Result<List<CaseDto>> pickRelevanceCaseId(@RequestBody RepairFilter repairFilter,
                                                        @LoginUser UserInfo userInfo,
                                                        @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.pickRelevanceCaseId(repairFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "根据新平台工单状态查询报修信息")
    @PostMapping("/listRepairByWorkStatus")
    public Result<ListWrapper<RepairDto>> listRepairByWorkStatus(@RequestBody RepairFilter repairFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairCompoService.listRepairByWorkStatus(repairFilter, userInfo, reqParam));
    }

}
