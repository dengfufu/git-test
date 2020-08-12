package com.zjft.usp.anyfix.work.transfer.controller;


import com.zjft.usp.anyfix.work.listener.WorkMqTopic;
import com.zjft.usp.anyfix.work.transfer.composite.WorkTransferCompoService;
import com.zjft.usp.anyfix.work.transfer.dto.WorkTransferDto;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 工单流转表 前端控制器
 * </p>
 *
 * @author zphu
 * @since 2019-09-25
 */
@Api(tags = "工单流转")
@RestController
@RequestMapping("/work-transfer")
public class WorkTransferController {

    @Autowired
    private WorkTransferCompoService workTransferCompoService;

    @ApiOperation(value = "客户撤单")
    @PostMapping(value = "/custom/recall")
    public Result recallWorkByCustom(@RequestBody WorkTransferDto workTransferDto,
                                     @LoginUser UserInfo userInfo,
                                     @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.recallWorkByCustom(workTransferDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "客户分配工单")
    @PostMapping(value = "/custom/dispatch")
    public Result dispatchWorkByCustom(@RequestBody WorkTransferDto workTransferDto,
                                       @LoginUser UserInfo userInfo,
                                       @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.dispatchWorkByCustom(workTransferDto, userInfo, reqParam);
        workTransferCompoService.addMessageQueue(WorkMqTopic.CUSTOM_DISPATCH_WORK, workTransferDto.getWorkId());
        return Result.succeed();
    }

    @ApiOperation(value = "服务商客服退单")
    @PostMapping(value = "/service/return")
    public Result returnWorkByService(@RequestBody WorkTransferDto workTransferDto,
                                      @LoginUser UserInfo userInfo,
                                      @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.returnWorkByService(workTransferDto, userInfo, reqParam);
        workTransferCompoService.addMessageQueue(WorkMqTopic.RETURN_WORK, workTransferDto.getWorkId());
        return Result.succeed();
    }

    @ApiOperation(value = "服务商客服受理工单")
    @PostMapping(value = "/service/handle")
    public Result handleWorkByManual(@RequestBody WorkTransferDto workTransferDto,
                                     @LoginUser UserInfo userInfo,
                                     @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.handleWorkByManual(workTransferDto, userInfo, reqParam);
        workTransferCompoService.addMessageQueue(WorkMqTopic.SERVICE_HANDLE_WORK, workTransferDto.getWorkId());
        return Result.succeed();
    }

    @ApiOperation(value = "服务商客服转处理")
    @PostMapping(value = "/service/turn/handle")
    public Result turnHandleWork(@RequestBody WorkTransferDto workTransferDto,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.turnHandleWork(workTransferDto, userInfo, reqParam);
        workTransferCompoService.addMessageQueue(WorkMqTopic.SERVICE_TURN_HANDLE_WORK, workTransferDto.getWorkId());
        return Result.succeed();
    }

    @ApiOperation(value = "服务商客服撤回派单")
    @PostMapping(value = "/service/assign/recall")
    public Result recallAssign(@RequestBody WorkTransferDto workTransferDto,
                               @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.recallAssign(workTransferDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "工程师认领工单")
    @PostMapping(value = "/engineer/claim")
    public Result claimWork(@RequestBody WorkTransferDto workTransferDto,
                            @LoginUser UserInfo userInfo,
                            @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.claimWork(workTransferDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "工程师拒绝派单")
    @PostMapping(value = "/engineer/assign/refuse")
    public Result refuseAssign(@RequestBody WorkTransferDto workTransferDto,
                               @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.refuseAssign(workTransferDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "工程师退回派单")
    @PostMapping(value = "/engineer/assign/return")
    public Result returnAssign(@RequestBody WorkTransferDto workTransferDto,
                               @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.returnAssign(workTransferDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "检查工单")
    @PostMapping(value = "/work/check")
    public Result checkWork(@RequestBody WorkTransferDto workTransferDto,
                            @LoginUser UserInfo userInfo,
                            @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.checkWork(workTransferDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "服务网点退回工单")
    @PostMapping(value = "/service/assign/return")
    public Result returnAssignByService(@RequestBody WorkTransferDto workTransferDto,
                               @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam) {
        workTransferCompoService.returnAssignByService(workTransferDto, userInfo, reqParam);
        return Result.succeed();
    }
}
