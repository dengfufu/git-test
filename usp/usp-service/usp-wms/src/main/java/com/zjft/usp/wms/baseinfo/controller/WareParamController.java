package com.zjft.usp.wms.baseinfo.controller;


import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WareParamFilter;
import com.zjft.usp.wms.baseinfo.model.WareParam;
import com.zjft.usp.wms.baseinfo.service.WareParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统参数表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/ware-param")
public class WareParamController {

    @Autowired
    WareParamService wareParamService;

    @GetMapping("/list")
    public Result<List<WareParam>> list(){
        return Result.succeed(wareParamService.listWareParam());
    }

    @GetMapping("/{wareParamId}")
    public Result<WareParam> find(@PathVariable Long wareParamId){
        return Result.succeed(wareParamService.findWareParamBy(wareParamId));
    }

    @PostMapping("/query")
    public Result<ListWrapper<WareParam>> query(@RequestBody WareParamFilter wareParamFilter, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return  Result.failed("参数解析失败！");
        }
        wareParamFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(wareParamService.queryWareParam(wareParamFilter));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody WareParam wareParam, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareParam.setCorpId(reqParam.getCorpId());
        wareParam.setCreateBy(user.getUserId());
        wareParam.setCreateTime(DateUtil.date());
        wareParamService.insertWareParam(wareParam);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody WareParam wareParam, @LoginUser UserInfo user){
        wareParam.setUpdateBy(user.getUserId());
        wareParam.setUpdateTime(DateUtil.date());
        wareParamService.updateWareParam(wareParam);
        return Result.succeed();
    }

    @DeleteMapping("/{wareParamId}")
    public Result delete(@PathVariable Long wareParamId){
        wareParamService.deleteWareParam(wareParamId);
        return Result.succeed();
    }
}
