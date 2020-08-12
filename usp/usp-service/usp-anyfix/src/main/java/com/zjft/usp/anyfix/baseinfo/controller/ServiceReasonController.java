package com.zjft.usp.anyfix.baseinfo.controller;

import com.zjft.usp.anyfix.baseinfo.filter.ServiceReasonFilter;
import com.zjft.usp.anyfix.baseinfo.model.ServiceReason;
import com.zjft.usp.anyfix.baseinfo.service.ServiceReasonService;
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
 * 服务商原因表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "服务商原因")
@RestController
@RequestMapping("/service-reason")
public class ServiceReasonController {

    @Autowired
    private ServiceReasonService serviceReasonService;

    @ApiOperation(value = "获得服务商可用的原因列表")
    @GetMapping(value = "/list/{reasonType}")
    public Result<List<ServiceReason>> addServiceReason(@PathVariable(value = "reasonType") Integer reasonType,
                                                        @CommonReqParam ReqParam reqParam) {
        List<ServiceReason> list = serviceReasonService.listEnableServiceReason(reqParam.getCorpId(), reasonType);
        return Result.succeed(list);
    }

    @ApiOperation(value = "添加服务商原因")
    @PostMapping(value = "/add")
    public Result addServiceReason(@RequestBody ServiceReason serviceReason,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        serviceReasonService.save(serviceReason,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改服务商原因")
    @PostMapping(value = "/update")
    public Result updateServiceReason(@RequestBody ServiceReason serviceReason,
                                      @LoginUser UserInfo userInfo) {
        serviceReasonService.update(serviceReason,userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除服务商原因")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        serviceReasonService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个服务商原因")
    @GetMapping(value = "/{id}")
    public Result selectById(@PathVariable("id") Long id) {
        return Result.succeed(serviceReasonService.getById(id));
    }

    @ApiOperation(value = "根据客户企业编号和原因类型获取列表")
    @GetMapping(value = "/list/urlType/{urlType}")
    public Result<List<ServiceReason>> selectByCorpAndType(@PathVariable("urlType") String type,
                                                           @CommonReqParam ReqParam reqParam) {
        return Result.succeed(serviceReasonService.selectByCorpAndType(reqParam.getCorpId(), type));
    }

    @ApiOperation(value = "分页查询服务商原因列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<ServiceReason>> query(@RequestBody ServiceReasonFilter serviceReasonFilter,
                                                    @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceReasonFilter.getServiceCorp())) {
            serviceReasonFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(serviceReasonService.query(serviceReasonFilter));
    }
}
