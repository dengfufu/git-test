package com.zjft.usp.anyfix.baseinfo.controller;


import com.zjft.usp.anyfix.baseinfo.dto.ServiceItemDto;
import com.zjft.usp.anyfix.baseinfo.filter.ServiceItemFilter;
import com.zjft.usp.anyfix.baseinfo.model.ServiceItem;
import com.zjft.usp.anyfix.baseinfo.service.ServiceItemService;
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
 * 服务项目表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "服务项目")
@RestController
@RequestMapping("/service-item")
public class ServiceItemController {

    @Autowired
    private ServiceItemService serviceItemService;

    @ApiOperation(value = "添加服务项目")
    @PostMapping(value = "/add")
    public Result addServiceItem(@RequestBody ServiceItem serviceItem,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
       serviceItemService.save(serviceItem,userInfo,reqParam);
       return Result.succeed();
    }

    @ApiOperation(value = "修改服务项目")
    @PostMapping(value = "/update")
    public Result updateServiceItem(@RequestBody ServiceItem serviceItem,
                                    @LoginUser UserInfo userInfo) {
        serviceItemService.update(serviceItem,userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除服务项目")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        serviceItemService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个服务项目")
    @GetMapping(value = "/{id}")
    public Result selectById(@PathVariable("id") Long id) {
        return Result.succeed(serviceItemService.getById(id));
    }

    @ApiOperation(value = "获得某个服务商的服务项目列表")
    @PostMapping(value = "/list")
    public Result<List<ServiceItem>> listServiceItem(@RequestBody ServiceItemFilter serviceItemFilter,
                                                           @CommonReqParam ReqParam reqParam) {
        if(LongUtil.isZero(serviceItemFilter.getServiceCorp())){
            serviceItemFilter.setServiceCorp(reqParam.getCorpId());
        }
        List<ServiceItem> list = serviceItemService.listServiceItemByFilter(serviceItemFilter, reqParam.getCorpId());
        return Result.succeed(list);
    }

    @ApiOperation(value = "分页查询服务项目列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<ServiceItemDto>> query(@RequestBody ServiceItemFilter serviceItemFilter,
                                                     @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceItemFilter.getServiceCorp())) {
            serviceItemFilter.setServiceCorp(reqParam.getCorpId());
        }
        ListWrapper<ServiceItemDto> list = serviceItemService.query(serviceItemFilter);
        return Result.succeed(list);
    }
}
