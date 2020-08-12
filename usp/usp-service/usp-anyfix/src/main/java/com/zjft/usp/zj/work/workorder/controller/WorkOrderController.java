package com.zjft.usp.zj.work.workorder.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.workorder.composite.WorkOrderCompoService;
import com.zjft.usp.zj.work.workorder.dto.WorkOrderDto;
import com.zjft.usp.zj.work.workorder.filter.WorkOrderFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 派工单 前面控制器
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 17:29
 **/
@Api(tags = "派工单请求")
@RestController
@RequestMapping("/zj/workorder")
public class WorkOrderController {
    @Autowired
    private WorkOrderCompoService workOrderCompoService;

    @ApiOperation("查询我的工单列表")
    @PostMapping("/listMyWork")
    public Result<ListWrapper<WorkOrderDto>> listMyWork(@RequestBody WorkOrderFilter workOrderFilter,
                                                        @LoginUser UserInfo userInfo,
                                                        @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workOrderCompoService.listMyWork(workOrderFilter, userInfo, reqParam));
    }

    @ApiOperation("查看派工单详情")
    @PostMapping("/findWorkOrderDetail")
    public Result<Map> findWorkOrderDetail(@RequestBody WorkOrderFilter workOrderFilter,
                                                    @LoginUser UserInfo userInfo,
                                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workOrderCompoService.findWorkOrderDetail(workOrderFilter, userInfo, reqParam));
    }

    @ApiOperation("进入新建派工单页面")
    @PostMapping("/addWorkOrder")
    public Result<Map> addWorkOrder(@RequestBody WorkOrderFilter workOrderFilter, @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workOrderCompoService.addWorkOrder(workOrderFilter, userInfo, reqParam));
    }

    @ApiOperation("新建派工单提交")
    @PostMapping("/addWorkOrderSubmit")
    public Result addWorkOrderSubmit(@RequestBody WorkOrderDto workOrderDto, @LoginUser UserInfo userInfo,
                                     @CommonReqParam ReqParam reqParam) {
        workOrderCompoService.addWorkOrderSubmit(workOrderDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation("进入接收派工单页面")
    @PostMapping("/acceptWorkOrder")
    public Result<Map> acceptWorkOrder(@RequestBody WorkOrderFilter workOrderFilter, @LoginUser UserInfo userInfo,
                                       @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workOrderCompoService.acceptWorkOrder(workOrderFilter, userInfo, reqParam));
    }

    @ApiOperation("接收派工单提交")
    @PostMapping("/acceptWorkOrderSubmit")
    public Result acceptWorkOrderSubmit(@RequestBody WorkOrderDto workOrderDto, @LoginUser UserInfo userInfo,
                                        @CommonReqParam ReqParam reqParam) {
        workOrderCompoService.acceptWorkOrderSubmit(workOrderDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation("进入拒绝派工单页面")
    @PostMapping("/refuseWorkOrder")
    public Result<Map> refuseWorkOrder(@RequestBody WorkOrderFilter workOrderFilter, @LoginUser UserInfo userInfo,
                                       @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workOrderCompoService.refuseWorkOrder(workOrderFilter, userInfo, reqParam));
    }

    @ApiOperation("拒绝派工单提交")
    @PostMapping("/refuseWorkOrderSubmit")
    public Result refuseWorkOrderSubmit(@RequestBody WorkOrderDto workOrderDto, @LoginUser UserInfo userInfo,
                                        @CommonReqParam ReqParam reqParam) {
        workOrderCompoService.refuseWorkOrderSubmit(workOrderDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation("进入预约页面")
    @PostMapping("/preBook")
    public Result<Map> preBook(@RequestBody WorkOrderFilter workOrderFilter, @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workOrderCompoService.preBook(workOrderFilter, userInfo, reqParam));
    }

    @ApiOperation("预约提交")
    @PostMapping("/preBookSubmit")
    public Result preBookSubmit(@RequestBody WorkOrderDto workOrderDto, @LoginUser UserInfo userInfo,
                                @CommonReqParam ReqParam reqParam) {
        workOrderCompoService.preBookSubmit(workOrderDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation("进入重新预约页面")
    @PostMapping("/renewPreBook")
    public Result<Map> renewPreBook(@RequestBody WorkOrderFilter workOrderFilter, @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workOrderCompoService.renewPreBook(workOrderFilter, userInfo, reqParam));
    }

    @ApiOperation("重新预约提交")
    @PostMapping("/renewPreBookSubmit")
    public Result renewPreBookSubmit(@RequestBody WorkOrderDto workOrderDto, @LoginUser UserInfo userInfo,
                                     @CommonReqParam ReqParam reqParam) {
        workOrderCompoService.renewPreBookSubmit(workOrderDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation("进入退回主管页面")
    @PostMapping("/returnManager")
    public Result<Map> returnManager(@RequestBody WorkOrderFilter workOrderFilter, @LoginUser UserInfo userInfo,
                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workOrderCompoService.returnManager(workOrderFilter, userInfo, reqParam));
    }

    @ApiOperation("退回主管提交")
    @PostMapping("/returnManagerSubmit")
    public Result returnManagerSubmit(@RequestBody WorkOrderDto workOrderDto, @LoginUser UserInfo userInfo,
                                      @CommonReqParam ReqParam reqParam) {
        workOrderCompoService.returnManagerSubmit(workOrderDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation("进入主动结束派工页面")
    @PostMapping("/activeEndWorkOrder")
    public Result<Map> activeEndWorkOrder(@RequestBody WorkOrderFilter workOrderFilter, @LoginUser UserInfo userInfo,
                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workOrderCompoService.activeEndWorkOrder(workOrderFilter, userInfo, reqParam));
    }

    @ApiOperation("主动结束派工提交")
    @PostMapping("/activeEndWorkOrderSubmit")
    public Result activeEndWorkOrderSubmit(@RequestBody WorkOrderDto workOrderDto, @LoginUser UserInfo userInfo,
                                           @CommonReqParam ReqParam reqParam) {
        workOrderCompoService.activeEndWorkOrderSubmit(workOrderDto, userInfo, reqParam);
        return Result.succeed();
    }

}
