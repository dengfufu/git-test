package com.zjft.usp.anyfix.work.fee.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailExcelDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeVerifyFilter;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyService;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托商对账单表 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
@Api(tags = "对账单")
@RestController
@RequestMapping("/work-fee-verify")
public class WorkFeeVerifyController {

    @Autowired
    private WorkFeeVerifyService workFeeVerifyService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private RedisRepository redisRepository;
    // 一分钟毫秒数
    private static Long ONE_MINUTE = 60000L;
    // 定时生成对账单的key
    private static String AUTO_CREATE_VERIFY_JOB_KEY = "auto-create-verify-job-executing";

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WorkFeeVerifyDto>> query(@RequestBody WorkFeeVerifyFilter workFeeVerifyFilter,
                                                       @LoginUser UserInfo userInfo,
                                                       @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workFeeVerifyFilter.getCurrentCorp())) {
            workFeeVerifyFilter.setCurrentCorp(reqParam.getCorpId());
        }
        return Result.succeed(workFeeVerifyService.query(workFeeVerifyFilter, userInfo.getUserId(), reqParam.getCorpId()));
    }

    @ApiOperation(value = "添加对账单")
    @PostMapping(value = "/add")
    public Result add(@RequestBody WorkFeeVerifyDto workFeeVerifyDto,
                    @LoginUser UserInfo userInfo,
                    @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workFeeVerifyDto.getServiceCorp())) {
            workFeeVerifyDto.setServiceCorp(reqParam.getCorpId());
        }
        this.workFeeVerifyService.add(workFeeVerifyDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "修改对账单")
    @PostMapping(value = "/update")
    public Result update(@RequestBody WorkFeeVerifyDto workFeeVerifyDto,
                         @LoginUser UserInfo userInfo) {
        this.workFeeVerifyService.update(workFeeVerifyDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "提交委托商对账")
    @PostMapping(value = "/submit")
    public Result submit(@RequestBody WorkFeeVerifyDto workFeeVerifyDto,
                         @LoginUser UserInfo userInfo) {
        this.workFeeVerifyService.submit(workFeeVerifyDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "对账")
    @PostMapping(value = "/verify")
    public Result verify(@RequestBody WorkFeeVerifyDto workFeeVerifyDto,
                         @LoginUser UserInfo userInfo,
                         @CommonReqParam ReqParam reqParam) {
        this.workFeeVerifyService.verify(workFeeVerifyDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "导入对账")
    @PostMapping(value = "/verify/import")
    public Result importVerify(@RequestBody MultipartFile file,
                               @CommonReqParam ReqParam reqParam,
                               @LoginUser UserInfo userInfo,
                               @RequestParam("verifyId") Long verifyId) {
        List<WorkFeeVerifyDetailExcelDto> excelDtoList = null;
        String msg = "";
        try {
            excelDtoList = EasyExcel.read(new BufferedInputStream(file.getInputStream())).head(
                    WorkFeeVerifyDetailExcelDto.class).sheet().doReadSync();
            msg = this.workFeeVerifyService.importVerify(excelDtoList, verifyId, userInfo.getUserId(), reqParam.getCorpId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.succeed(msg);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/{verifyId}")
    public Result delete(@PathVariable("verifyId") Long verifyId,
                         @LoginUser UserInfo userInfo,
                         @CommonReqParam ReqParam reqParam) {
        this.workFeeVerifyService.delete(verifyId, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "查询可对账工单")
    @PostMapping(value = "/listCanVerifyWork")
    public Result<WorkFeeVerifyDto> listCanVerifyWork(@RequestBody WorkFilter workFilter,
                                                      @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workFilter.getServiceCorp())) {
            workFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.workFeeVerifyService.listCanVerifyWork(workFilter));
    }

    @ApiOperation(value = "查询对账单详情")
    @GetMapping(value = "/{verifyId}")
    public Result<WorkFeeVerifyDto> findDetail(@PathVariable("verifyId") Long verifyId) {
        return Result.succeed(this.workFeeVerifyService.findDetail(verifyId));
    }

    @ApiOperation(value = "确认")
    @PostMapping(value = "/confirm")
    public Result confirm(@RequestBody WorkFeeVerifyDto workFeeVerifyDto,
                          @LoginUser UserInfo userInfo,
                          @CommonReqParam ReqParam reqParam) {
        this.workFeeVerifyService.confirm(workFeeVerifyDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "分页查询可结算对账单")
    @PostMapping(value = "/queryCanSettleVerify")
    public Result<ListWrapper<WorkFeeVerifyDto>> queryCanSettleVerify(@RequestBody WorkFeeVerifyFilter workFeeVerifyFilter,
                                                         @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workFeeVerifyFilter.getServiceCorp())) {
            workFeeVerifyFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.workFeeVerifyService.queryCanSettleVerify(workFeeVerifyFilter));
    }

    @ApiOperation(value = "定时生成对账单")
    @PostMapping(value = "/createJob")
    public Result autoCreateVerify() {
        // 进入任务，如果任务正在执行，则停止，避免因时钟差异导致的重复执行
        if (this.redisRepository.exists(AUTO_CREATE_VERIFY_JOB_KEY)) {
            return Result.failed("任务正在执行，无需重复执行");
        }
        this.redisRepository.setExpire(AUTO_CREATE_VERIFY_JOB_KEY, ONE_MINUTE);
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
                    msg.append("服务商【" + corpDto.getCorpName() + "】：");
                    msg.append(workFeeVerifyService.autoCreateVerify(corpDto.getCorpId())).append("<br>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 执行完成， 删除key
            this.redisRepository.del(AUTO_CREATE_VERIFY_JOB_KEY);
        }
        return Result.succeedStr(msg.toString());
    }

}
