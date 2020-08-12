package com.zjft.usp.uas.corp.controller.feign;

import com.zjft.usp.uas.corp.model.CorpRegistry;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 企业注册控制器
 *
 * @author CK
 * @version 1.0
 * @date 2020-06-24 09:06
 **/
@Api(tags = "feign-企业注册")
@RestController
@RequestMapping(value = "/feign/corp-registry")
public class CorpRegistryFeignController {

    @Autowired
    private CorpRegistryService corpRegistryService;

    @ApiOperation(value = "远程调用：根据多个ID查询企业信息")
    @PostMapping(value = "/corp-map")
    public Map<Long, CorpRegistry> listCorpByIdList(@RequestBody List<Long> corpIdList) {
        return corpRegistryService.mapCorpIdAndRegistry(corpIdList);
    }

}
