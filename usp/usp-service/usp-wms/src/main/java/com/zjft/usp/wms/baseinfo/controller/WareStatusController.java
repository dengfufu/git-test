package com.zjft.usp.wms.baseinfo.controller;


import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WareStatusFilter;
import com.zjft.usp.wms.baseinfo.model.WareStatus;
import com.zjft.usp.wms.baseinfo.service.WareStatusService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 物料状态表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/ware-status")
public class WareStatusController {

    @Autowired
    WareStatusService wareStatusService;

    @GetMapping("/list")
    public Result<List<WareStatus>> list(@CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        return Result.succeed(wareStatusService.listWareStatus(reqParam.getCorpId()));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WareStatus>> query(@RequestBody WareStatusFilter wareStatusFilter, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareStatusFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(wareStatusService.queryWareStatus(wareStatusFilter));
    }

    @GetMapping("/{wareStatusId}")
    public Result<WareStatus> find(@PathVariable Long wareStatusId){
        return Result.succeed(wareStatusService.getById(wareStatusId));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody WareStatus wareStatus, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareStatus.setCorpId(reqParam.getCorpId());
        wareStatus.setCreateBy(user.getUserId());
        wareStatus.setCreateTime(DateUtil.date());
        wareStatusService.insertWareStatus(wareStatus);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody WareStatus wareStatus, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareStatus.setCorpId(reqParam.getCorpId());
        wareStatus.setUpdateBy(user.getUserId());
        wareStatus.setUpdateTime(DateUtil.date());
        wareStatusService.updateWareStatus(wareStatus);
        return Result.succeed();
    }

    @DeleteMapping("/{wareStatusId}")
    public Result delete(@PathVariable Long wareStatusId){
        wareStatusService.deleteWareStatus(wareStatusId);
        return Result.succeed();
    }
}
