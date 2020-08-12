package com.zjft.usp.wms.baseinfo.controller;


import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.dto.WareModelDto;
import com.zjft.usp.wms.baseinfo.filter.WareModelFilter;
import com.zjft.usp.wms.baseinfo.model.WareModel;
import com.zjft.usp.wms.baseinfo.service.WareModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 型号表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/ware-model")
public class WareModelController {

    @Autowired
    WareModelService wareModelService;

    @PostMapping("/list")
    public Result<List<WareModelDto>> list(@RequestBody WareModelFilter wareModelFilter, @CommonReqParam ReqParam reqParam){
        if (LongUtil.isZero(wareModelFilter.getCorpId())) {
            wareModelFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(wareModelService.listWareModel(wareModelFilter));
    }

    @PostMapping("/query")
    public Result<ListWrapper<WareModelDto>> query(@RequestBody WareModelFilter wareModelFilter, @CommonReqParam ReqParam reqParam) {
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareModelFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(wareModelService.queryWareModel(wareModelFilter));
    }

    @GetMapping("/{wareModelId}")
    public Result<WareModel> find(@PathVariable Long wareModelId){
        return Result.succeed(wareModelService.findWareModelBy(wareModelId));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody WareModelDto wareModelDto, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareModelDto.setCorpId(reqParam.getCorpId());
        wareModelDto.setCreateBy(user.getUserId());
        wareModelDto.setCreateTime(DateUtil.date());
        wareModelService.insertWareModel(wareModelDto);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody WareModelDto wareModelDto, @LoginUser UserInfo user){
        wareModelDto.setUpdateBy(user.getUserId());
        wareModelDto.setUpdateTime(DateUtil.date());
        wareModelService.updateWareModel(wareModelDto);
        return Result.succeed();
    }

    @DeleteMapping("/{wareModelId}")
    public Result delete(@PathVariable Long wareModelId){
        wareModelService.deleteWareModel(wareModelId);
        return Result.succeed();
    }
}
