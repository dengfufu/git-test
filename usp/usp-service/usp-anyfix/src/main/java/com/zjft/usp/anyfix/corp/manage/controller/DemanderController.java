package com.zjft.usp.anyfix.corp.manage.controller;

import com.zjft.usp.anyfix.corp.manage.dto.DemanderDto;
import com.zjft.usp.anyfix.corp.manage.service.DemanderService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 供应商前端控制器
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/1 10:51
 */
@Api(tags = "委托商")
@RestController
@RequestMapping("/demander")
public class DemanderController {

    @Autowired
    private DemanderService demanderService;

    @ApiOperation("根据当前企业编号获得关联的委托商列表")
    @GetMapping(value = "/list")
    public Result<List<DemanderDto>> listDemanderDto(@CommonReqParam ReqParam reqParam) {
        return Result.succeed(demanderService.listDemander(reqParam.getCorpId()));
    }

    @ApiOperation("根据企业编号获得关联的委托商编号名称map")
    @GetMapping(value = "/feign/{corpId}")
    public Result<Map<Long,String>> getDemanderIdNameMap(@PathVariable("corpId") Long corpId) {
        return Result.succeed(demanderService.getDemanderIdNameMap(corpId));
    }

}
