package com.zjft.usp.wms.baseinfo.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WareClassFilter;
import com.zjft.usp.wms.baseinfo.model.WareClass;
import com.zjft.usp.wms.baseinfo.service.WareClassService;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.baseinfo.dto.WareClassDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 物品分类表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@Api(tags = "物品信息")
@RestController
@RequestMapping("/ware-class")
public class WareClassController {

    @Autowired
    private WareClassService wareClassService;

    @ApiOperation(value = "获得物品信息列表")
    @PostMapping(value = "/list")
    public Result listWareClass(@RequestBody WareClassFilter wareClassFilter, @CommonReqParam ReqParam reqParam) {
        if(wareClassFilter != null && LongUtil.isZero(wareClassFilter.getCorpId())){
            wareClassFilter.setCorpId(reqParam.getCorpId());
        }
        List<WareClassDto> partClassList = wareClassService.listWareClass(wareClassFilter);
        return Result.succeed(partClassList);
    }

    @ApiOperation(value = "获得单个物品信息")
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable("id") Long id) {
        WareClass wareClass = wareClassService.getById(id);
        return Result.succeed(wareClass);
    }

    @ApiOperation(value = "添加物品信息")
    @PostMapping(value = "/add")
    public Result insert(@RequestBody WareClassDto wareClassDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.wareClassService.addWareClass(wareClassDto, userInfo.getUserId(), reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改物品信息")
    @PostMapping(value = "/update")
    public Result update(@RequestBody WareClassDto wareClassDto, @LoginUser UserInfo userInfo) {
        this.wareClassService.updateWareClass(wareClassDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "删除物品信息")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        this.wareClassService.deleteById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得某个企业某种品牌某个设备小类对应的物品列表")
    @GetMapping(value = "/list/{corpId}/{brandId}/{smallClassId}")
    public Result listWareClassBy(@PathVariable("corpId") Long corpId,
                                  @PathVariable("brandId") Long brandId,
                                  @PathVariable("smallClassId") Long smallClassId) {
        List<WareClass> list = wareClassService.listWareClassBy(corpId, brandId, smallClassId);
        return Result.succeed(list);
    }
}
