package com.zjft.usp.anyfix.work.request.controller;

import com.zjft.usp.anyfix.utils.AddressResolutionUtil;
import com.zjft.usp.anyfix.work.request.composite.CaseRequestCompoService;
import com.zjft.usp.anyfix.work.request.composite.WorkRequestCompoService;
import com.zjft.usp.anyfix.work.request.dto.AddressDto;
import com.zjft.usp.anyfix.work.request.dto.CaseDto;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;


/**
 * <p>
 * 工单服务请求表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
@Api(tags = "工单服务请求")
@RestController
@RequestMapping("/work-request")
public class WorkRequestController {

    public static final String WX_SCAN_CREATE = "scan";
    public static final String WX_DEMANDER_CREATE = "demander";
    @Autowired
    private WorkRequestCompoService workRequestCompoService;
    @Autowired
    private CaseRequestCompoService caseRequestCompoService;


    @ApiOperation(value = "根据条件分页查询工单")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WorkDto>> queryWork(@RequestBody WorkFilter workFilter,
                                                  @LoginUser UserInfo userInfo,
                                                  @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workFilter.getCorpId())) {
            workFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(workRequestCompoService.queryWork(workFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "根据条件分页查询金融设备类工单")
    @PostMapping(value = "/query/atmcase")
    public Result<ListWrapper<CaseDto>> queryAtmCase(@RequestBody WorkFilter workFilter,
                                                     @LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(caseRequestCompoService.queryAtmCase(workFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获得单个金融设备类工单详情")
    @PostMapping(value = "/detail/atmcase")
    public Result<CaseDto> findAtmCaseDetail(@RequestBody WorkFilter workFilter, @LoginUser UserInfo userInfo) {
        return Result.succeed(caseRequestCompoService.findAtmCaseDetail(workFilter, userInfo));
    }

    @ApiOperation(value = "获取金融设备工单下拉数据源")
    @PostMapping(value = "/findAtmCaseOption")
    public Result<Map<String, Object>> findAtmCaseOption(@RequestBody WorkFilter workFilter, @LoginUser UserInfo userInfo) {
        return Result.succeed(caseRequestCompoService.findAtmCaseOption(workFilter, userInfo));
    }

    @ApiOperation(value = "添加工单服务请求")
    @PostMapping(value = "/add")
    public Result<Long> addWorkRequest(@RequestBody WorkRequestDto workRequestDto,
                                       @LoginUser UserInfo userInfo,
                                       @CommonReqParam ReqParam reqParam) {
        workRequestDto.setCreatorCorpId(reqParam.getCorpId());
        List<Long> workIdList = workRequestCompoService.addWorkRequest(workRequestDto, userInfo, reqParam);
        workRequestCompoService.addMessageQueueByCreate(workIdList);
        return Result.succeed(workIdList.get(0));
    }

    @ApiOperation(value = "微信添加工单服务请求")
    @PostMapping(value = "/addByWx")
    public Result<Long> addWXWorkRequest(@RequestBody WorkRequestDto workRequestDto,
                                       @LoginUser UserInfo userInfo,
                                       @CommonReqParam ReqParam reqParam) {

        workRequestDto.setCreatorCorpId(reqParam.getCorpId());
        List<Long> workIdList = workRequestCompoService.addWorkRequest(workRequestDto, userInfo, reqParam);
        if(workRequestDto.getWxCreateType().equalsIgnoreCase(WX_SCAN_CREATE)) {
            // 扫一扫建单
            workRequestCompoService.addMessageQueueByScanCreate(workIdList);
        } else if(workRequestDto.getWxCreateType().equalsIgnoreCase(WX_DEMANDER_CREATE)) {
            // 委托商建单
            workRequestCompoService.addMessageQueueByCreate(workIdList);
        }
        // TODO 需要挪到消息服务，通过
//        workRequestCompoService.buildWxMessage(workIdList);
        return Result.succeed(workIdList.get(0));

    }

    @ApiOperation(value = "修改工单服务请求")
    @PostMapping(value = "/update")
    public Result modWorkRequest(@RequestBody WorkRequestDto workRequestDto,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        Long workId = workRequestCompoService.modWorkRequest(workRequestDto, userInfo, reqParam);
        workRequestCompoService.addMessageQueueByMod(workId);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个工单详情")
    @GetMapping(value = "/detail/{workId}")
    public Result<WorkDto> findWorkDetail(@PathVariable("workId") Long workId,
                                          @LoginUser UserInfo userInfo,
                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workRequestCompoService.findWorkDetail(workId, userInfo, reqParam));
    }

    @ApiOperation(value = "分页查询当前用户的工单列表")
    @PostMapping(value = "/user/query")
    public Result<ListWrapper<WorkDto>> queryUserWork(@RequestBody WorkFilter workFilter,
                                                      @LoginUser UserInfo userInfo,
                                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workRequestCompoService.queryUserWork(workFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "分页查询当前用户待办的工单列表")
    @PostMapping(value = "/user/todo/query")
    public Result<ListWrapper<WorkDto>> queryUserTodoWork(@RequestBody WorkFilter workFilter,
                                                          @LoginUser UserInfo userInfo,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workRequestCompoService.queryUserTodoWork(workFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获得客户的工单记录")
    @PostMapping(value = "/custom/query")
    public Result<ListWrapper<WorkDto>> queryCustomWork(@RequestBody WorkFilter workFilter,
                                                        @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workFilter.getDemanderCorp())) {
            workFilter.setDemanderCorp(reqParam.getCorpId());
        }
        return Result.succeed(workRequestCompoService.queryDetailDto(workFilter));
    }

    @ApiOperation(value = "服务商审核服务查询")
    @PostMapping(value = "/service-check/query")
    public Result<ListWrapper<WorkDto>> queryServiceCheck(@RequestBody WorkFilter workFilter,
                                                          @LoginUser UserInfo userInfo,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.workRequestCompoService.queryServiceCheck(workFilter, userInfo.getUserId(), reqParam.getCorpId()));
    }

    @ApiOperation(value = "服务商审核费用查询")
    @PostMapping(value = "/fee-check/query")
    public Result<ListWrapper<WorkDto>> queryFeeCheck(@RequestBody WorkFilter workFilter,
                                                      @LoginUser UserInfo userInfo,
                                                      @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.workRequestCompoService.queryFeeCheck(workFilter, userInfo.getUserId(), reqParam.getCorpId()));
    }

    @ApiOperation(value = "委托商确认服务查询")
    @PostMapping(value = "/service-confirm/query")
    public Result<ListWrapper<WorkDto>> queryServiceConfirm(@RequestBody WorkFilter workFilter,
                                                            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.workRequestCompoService.queryServiceConfirm(workFilter, reqParam.getCorpId()));
    }

    @ApiOperation(value = "委托商确认费用查询")
    @PostMapping(value = "/fee-confirm/query")
    public Result<ListWrapper<WorkDto>> queryFeeConfirm(@RequestBody WorkFilter workFilter,
                                                        @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.workRequestCompoService.queryFeeConfirm(workFilter, reqParam.getCorpId()));
    }

    @ApiOperation(value = "行政区划地址匹配")
    @PostMapping(value = "/addressResolution")
    public Result<AddressDto> addressResolution(@RequestBody WorkFilter workFilter) {
        AddressDto addressDto = AddressResolutionUtil.addressFormat(workFilter.getAddressStr());
        return Result.succeed(addressDto);
    }

    @ApiOperation(value = "获取需要补传数量")
    @GetMapping(value = "/getReplenishCount")
    public Result getReplenishCount(@LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workRequestCompoService.getReplenishCount(userInfo, reqParam));
    }

    @ApiOperation(value = "根据条件分页查询工单预警")
    @PostMapping(value = "/queryRemindWork")
    public Result<ListWrapper<WorkDto>> queryRemindWork(@RequestBody WorkFilter workFilter,
                                                        @LoginUser UserInfo userInfo,
                                                        @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workRequestCompoService.queryRemindWork(workFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "根据条件分页查询工单")
    @PostMapping(value = "/queryWX")
    public Result<ListWrapper<WorkDto>> queryWXWork(@RequestBody WorkFilter workFilter,
                                                  @LoginUser UserInfo userInfo,
                                                  @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workRequestCompoService.queryWXWork(workFilter, userInfo, reqParam));
    }
}
