package com.zjft.usp.uas.baseinfo.controller;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.baseinfo.model.CfgIndustry;
import com.zjft.usp.uas.baseinfo.service.CfgIndustryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 基础数据控制器
 *
 * @author canlei
 * @version 1.0
 * @date 2019-08-19 15:21
 **/
@Api(tags = "行业管理")
@RestController
@RequestMapping(value = "/industry")
public class CfgIndustryController {

    @Autowired
    private CfgIndustryService baseInfoService;

    /**
     * 获取行业列表
     * @return
     */
    @GetMapping(value = "/list")
    public Result<List<CfgIndustry>> listIndustry(){
        return Result.succeed(baseInfoService.listIndustry());
    }
}
