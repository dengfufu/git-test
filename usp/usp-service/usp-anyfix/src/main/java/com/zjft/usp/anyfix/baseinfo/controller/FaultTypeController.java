package com.zjft.usp.anyfix.baseinfo.controller;


import com.zjft.usp.anyfix.baseinfo.dto.FaultTypeDto;
import com.zjft.usp.anyfix.baseinfo.filter.FaultTypeFilter;
import com.zjft.usp.anyfix.baseinfo.model.FaultType;
import com.zjft.usp.anyfix.baseinfo.service.FaultTypeService;
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
 * 故障现象表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "故障现象")
@RestController
@RequestMapping("/fault-type")
public class FaultTypeController {

    @Autowired
    private FaultTypeService faultTypeService;

    @ApiOperation(value = "获得可用的故障现象列表")
    @PostMapping(value = "/list")
    public Result<List<FaultType>> listEnableFaultType(@RequestBody FaultType faultType,
                                                       @CommonReqParam ReqParam reqParam) {
        Long corpId = reqParam.getCorpId();
        if(LongUtil.isNotZero(faultType.getDemanderCorp())){
            corpId = faultType.getDemanderCorp();
        }
        return Result.succeed(faultTypeService.listEnableFaultTypeByCorp(corpId));
    }

    @ApiOperation(value = "分页查询故障现象")
    @PostMapping(value = "/query")
    public Result<ListWrapper<FaultTypeDto>> query(@RequestBody FaultTypeFilter faultTypeFilter,
                                                   @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(faultTypeFilter.getCorpId())) {
            faultTypeFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.faultTypeService.query(faultTypeFilter));
    }

    @ApiOperation(value = "添加故障现象")
    @PostMapping(value = "/add")
    public Result addFaultType(@RequestBody FaultType faultType,
                               @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam) {
        faultTypeService.save(faultType,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改故障现象")
    @PostMapping(value = "/update")
    public Result updateFaultType(@RequestBody FaultType faultType,
                                  @LoginUser UserInfo userInfo) {
        faultTypeService.update(faultType,userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除故障现象")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        faultTypeService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个故障现象")
    @GetMapping(value = "/{id}")
    public Result selectById(@PathVariable("id") Long id) {
        return Result.succeed(faultTypeService.getById(id));
    }

}
