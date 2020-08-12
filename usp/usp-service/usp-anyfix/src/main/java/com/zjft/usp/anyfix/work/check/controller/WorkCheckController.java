package com.zjft.usp.anyfix.work.check.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.anyfix.work.check.composite.WorkCheckCompoService;
import com.zjft.usp.anyfix.work.check.dto.WorkCheckDto;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 工单审核信息表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-05-11
 */
@RestController
@RequestMapping("/work-check")
public class WorkCheckController {

    @Autowired
    private WorkCheckCompoService workCheckCompoService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private RedisRepository redisRepository;

    private static String AUTO_CONFIRM_SERVICE_KEY = "auto-confirm-service-job-executing";
    private static String AUTO_CONFIRM_FEE_KEY = "auto-confirm-fee-job-executing";
    private static Long ONE_MINUTE = 60000L;

    @ApiOperation(value = "获得工单审核记录")
    @GetMapping(value = "/{workId}")
    public Result<WorkCheckDto> findWorkCheck(@PathVariable("workId") Long workId) {
        return Result.succeed(workCheckCompoService.findWorkCheck(workId));
    }

    @ApiOperation(value = "审核服务")
    @PostMapping(value = "/service/check")
    public Result checkService(@RequestBody WorkCheckDto workCheckDto,
                               @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam) {
        workCheckCompoService.checkService(workCheckDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "审核费用")
    @PostMapping(value = "/fee/check")
    public Result checkFee(@RequestBody WorkCheckDto workCheckDto,
                           @LoginUser UserInfo userInfo,
                           @CommonReqParam ReqParam reqParam) {
        workCheckCompoService.checkFee(workCheckDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "确认服务")
    @PostMapping(value = "/service/confirm")
    public Result confirmService(@RequestBody WorkCheckDto workCheckDto,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        workCheckCompoService.confirmService(workCheckDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "确认费用")
    @PostMapping(value = "/fee/confirm")
    public Result confirmFee(@RequestBody WorkCheckDto workCheckDto,
                             @LoginUser UserInfo userInfo,
                             @CommonReqParam ReqParam reqParam) {
        workCheckCompoService.confirmFee(workCheckDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "服务商客服批量审核服务")
    @PostMapping(value = "/service/batch-check")
    public Result batchCheckService(@RequestBody WorkCheckDto workCheckDto,
                                    @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        String msg = workCheckCompoService.batchCheckService(workCheckDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeedStr(msg);
    }

    @ApiOperation(value = "服务商客服批量审核费用")
    @PostMapping(value = "/fee/batch-check")
    public Result batchCheckFee(@RequestBody WorkCheckDto workCheckDto,
                                @LoginUser UserInfo userInfo,
                                @CommonReqParam ReqParam reqParam) {
        String msg = workCheckCompoService.batchCheckFee(workCheckDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeedStr(msg);
    }

    @ApiOperation(value = "委托商批量确认服务")
    @PostMapping(value = "/service/batch-confirm")
    public Result batchConfirmService(@RequestBody WorkCheckDto workCheckDto,
                                      @LoginUser UserInfo userInfo,
                                      @CommonReqParam ReqParam reqParam) {
        String msg = workCheckCompoService.batchConfirmService(workCheckDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeedStr(msg);
    }

    @ApiOperation(value = "委托商批量确认费用")
    @PostMapping(value = "/fee/batch-confirm")
    public Result batchConfirmFee(@RequestBody WorkCheckDto workCheckDto,
                                  @LoginUser UserInfo userInfo,
                                  @CommonReqParam ReqParam reqParam) {
        String msg = workCheckCompoService.batchConfirmFee(workCheckDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeedStr(msg);
    }

    @ApiOperation(value = "自动确认服务")
    @PostMapping(value = "/service/auto-confirm")
    public Result autoConfirmService() {
        if (this.redisRepository.exists(AUTO_CONFIRM_SERVICE_KEY)) {
            return Result.failed("任务正在执行，无需重复执行");
        }
        this.redisRepository.setExpire(AUTO_CONFIRM_SERVICE_KEY, ONE_MINUTE);
        StringBuilder msg = new StringBuilder();
        try {
            // 获取所有服务商
            Result tenantListResult = this.uasFeignService.listTenant(JsonUtil.toJsonString("serviceProvider", "Y"));
            List<CorpDto> corpDtoList = new ArrayList<>();
            if (Result.isSucceed(tenantListResult)) {
                corpDtoList = JsonUtil.parseArray(JsonUtil.toJson(tenantListResult.getData()), CorpDto.class);
            }
            // 分服务商执行定时任务
            if (CollectionUtil.isNotEmpty(corpDtoList)) {
                for (CorpDto corpDto : corpDtoList) {
                    msg.append("服务商" + corpDto.getCorpName() + "：");
                    msg.append(workCheckCompoService.autoConfirmService(corpDto.getCorpId())).append("<br>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.redisRepository.del(AUTO_CONFIRM_SERVICE_KEY);
        }
        return Result.succeedStr(msg.toString());
    }

    @ApiOperation(value = "自动确认费用")
    @PostMapping(value = "/fee/auto-confirm")
    public Result autoConfirmFee() {
        if (this.redisRepository.exists(AUTO_CONFIRM_FEE_KEY)) {
            return Result.failed("任务正在执行，无需重复执行");
        }
        this.redisRepository.setExpire(AUTO_CONFIRM_FEE_KEY, ONE_MINUTE);
        StringBuilder msg = new StringBuilder();
        try {
            // 获取所有服务商
            Result tenantListResult = this.uasFeignService.listTenant(JsonUtil.toJsonString("serviceProvider", "Y"));
            List<CorpDto> corpDtoList = new ArrayList<>();
            if (Result.isSucceed(tenantListResult)) {
                corpDtoList = JsonUtil.parseArray(JsonUtil.toJson(tenantListResult.getData()), CorpDto.class);
            }
            // 分服务商执行定时任务
            if (CollectionUtil.isNotEmpty(corpDtoList)) {
                for (CorpDto corpDto : corpDtoList) {
                    msg.append("服务商" + corpDto.getCorpName() + "：");
                    msg.append(workCheckCompoService.autoConfirmFee(corpDto.getCorpId())).append("<br>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.redisRepository.del(AUTO_CONFIRM_FEE_KEY);
        }
        return Result.succeedStr(msg.toString());
    }

}
