package com.zjft.usp.anyfix.baseinfo.controller;

import com.zjft.usp.anyfix.baseinfo.filter.ServiceEvaluateFilter;
import com.zjft.usp.anyfix.baseinfo.model.ServiceEvaluate;
import com.zjft.usp.anyfix.baseinfo.service.ServiceEvaluateService;
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

/**
 * <p>
 * 服务评价指标表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "服务评价指标")
@RestController
@RequestMapping("/service-evaluate")
public class ServiceEvaluateController {

    @Autowired
    private ServiceEvaluateService evaluateService;

    @ApiOperation(value = "添加服务评价指标")
    @PostMapping(value = "/add")
    public Result addServiceEvaluate(@RequestBody ServiceEvaluate serviceEvaluate,
                                     @LoginUser UserInfo userInfo,
                                     @CommonReqParam ReqParam reqParam) {
        evaluateService.save(serviceEvaluate,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改服务评价指标")
    @PostMapping(value = "/update")
    public Result updateServiceEvaluate(@RequestBody ServiceEvaluate serviceEvaluate,
                                        @LoginUser UserInfo userInfo) {
        evaluateService.update(serviceEvaluate,userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除服务评价指标")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        evaluateService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个服务评价指标")
    @GetMapping(value = "/{id}")
    public Result selectById(@PathVariable("id") Long id) {
        return Result.succeed(evaluateService.getById(id));
    }

    @ApiOperation(value = "获得服务评价指标列表")
    @PostMapping(value = "/list")
    public Result<List<ServiceEvaluate>> selectByServiceCrop(@RequestBody ServiceEvaluate serviceEvaluate,
                                                             @CommonReqParam ReqParam reqParam) {
        Long corpId = reqParam.getCorpId();
        if(LongUtil.isNotZero(serviceEvaluate.getServiceCorp())){
            corpId = serviceEvaluate.getServiceCorp();
        }
        return Result.succeed(evaluateService.listServiceEvaluate(corpId));
    }

    @ApiOperation(value = "分页查询服务评价指标列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<ServiceEvaluate>> selectByServiceCrop(@RequestBody ServiceEvaluateFilter serviceEvaluateFilter,
                                                                    @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceEvaluateFilter.getServiceCorp())) {
            serviceEvaluateFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(evaluateService.query(serviceEvaluateFilter));
    }
}
