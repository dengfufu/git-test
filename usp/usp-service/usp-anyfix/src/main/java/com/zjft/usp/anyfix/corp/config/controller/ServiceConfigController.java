package com.zjft.usp.anyfix.corp.config.controller;


import com.zjft.usp.anyfix.corp.config.dto.ServiceConfigDto;
import com.zjft.usp.anyfix.corp.config.dto.ServiceConfigFilter;
import com.zjft.usp.anyfix.corp.config.model.ServiceConfig;
import com.zjft.usp.anyfix.corp.config.service.ServiceConfigService;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托商服务商关系配置表 前端控制器
 * </p>
 *
 * @author zrlin
 * @since 2020-04-15
 */
@Api(tags = "委托商服务商数据项配置")
@RestController
@RequestMapping("/service-config")
public class ServiceConfigController {

    @Resource
    private ServiceConfigService serviceConfigService;

    @PostMapping("/demander/add")
    public void addServiceConfig(@RequestBody ServiceConfig serviceConfig) {
        serviceConfigService.addServiceConfig(serviceConfig);
    }

    @PostMapping("/corp/add")
    public void addCorpServiceConfig(@RequestBody ServiceConfig serviceConfig) {
        serviceConfigService.addServiceConfig(serviceConfig);
    }

    @ApiOperation(value = "获得委托商数据项配置列表")
    @PostMapping("/demander/getConfig")
    public Result<List<ServiceConfigDto>> listServiceConfig(@RequestBody ServiceConfigFilter serviceConfigFilter) {
        return Result.succeed(serviceConfigService.listServiceConfig(serviceConfigFilter));
    }

    @PostMapping("/corp/getConfig")
    public Result<List<ServiceConfigDto>> getCorpConfig(@RequestBody Map<String, Object> paramsMap) {
        String corpId = (String) paramsMap.get("corpId");
        Long corpIdLong = Long.parseLong(corpId);
        List<Integer> itemIdList = (List<Integer>) paramsMap.get("itemIdList");
        return Result.succeed(serviceConfigService.listConfigByCorpId(corpIdLong, itemIdList));
    }

    @PostMapping("/corp/getConfigByWx")
    public Result<List<ServiceConfigDto>> getCorpConfigByWx(@RequestBody Map<String,Object> paramsMap){
       return this.getCorpConfig(paramsMap);
    }
}
