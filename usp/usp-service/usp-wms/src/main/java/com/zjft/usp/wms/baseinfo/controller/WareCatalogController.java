package com.zjft.usp.wms.baseinfo.controller;


import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.dto.WareCatalogDto;
import com.zjft.usp.wms.baseinfo.filter.WareCatalogFilter;
import com.zjft.usp.wms.baseinfo.model.WareCatalog;
import com.zjft.usp.wms.baseinfo.service.WareCatalogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 物料分类表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/ware-catalog")
public class WareCatalogController {

    @Autowired
    WareCatalogService wareCatalogService;

    @GetMapping("/list")
    public Result<List<WareCatalogDto>> list(@CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        return Result.succeed(wareCatalogService.listWareCatalog(reqParam.getCorpId()));
    }

    @GetMapping("/tree")
    public Result<List<WareCatalogDto>> tree(@CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        return Result.succeed(wareCatalogService.treeWareCatalog(reqParam.getCorpId()));
    }

    @ApiOperation(value = "模糊匹配")
    @PostMapping("/match")
    public Result<List<WareCatalog>> match(@RequestBody WareCatalogFilter wareCatalogFilter, @CommonReqParam ReqParam reqParam) {
        wareCatalogFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(wareCatalogService.match(wareCatalogFilter));
    }

    @PostMapping("/query")
    public Result<ListWrapper<WareCatalogDto>> query(@RequestBody WareCatalogFilter wareCatalogFilter, @CommonReqParam ReqParam reqParam) {
        wareCatalogFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(wareCatalogService.queryWareCatalog(wareCatalogFilter));
    }

    @GetMapping("/{wareCatalogId}")
    public Result<WareCatalogDto> find(@PathVariable Long wareCatalogId){
        return Result.succeed(wareCatalogService.findWareCatalogBy(wareCatalogId));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody WareCatalogDto wareCatalogDto, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareCatalogDto.setCorpId(reqParam.getCorpId());
        wareCatalogDto.setCreateBy(user.getUserId());
        wareCatalogDto.setCreateTime(DateUtil.date());
        wareCatalogService.insertWareCatalog(wareCatalogDto);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody WareCatalogDto wareCatalogDto, @LoginUser UserInfo user){
        wareCatalogDto.setUpdateBy(user.getUserId());
        wareCatalogDto.setUpdateTime(DateUtil.date());
        wareCatalogService.updateWareCatalog(wareCatalogDto);
        return Result.succeed();
    }

    @DeleteMapping("/{wareCatalogId}")
    public Result delete(@PathVariable Long wareCatalogId){
        wareCatalogService.deleteWareCatalog(wareCatalogId);
        return Result.succeed();
    }
}
