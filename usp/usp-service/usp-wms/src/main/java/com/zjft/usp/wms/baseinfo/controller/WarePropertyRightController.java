package com.zjft.usp.wms.baseinfo.controller;


import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WarePropertyRightFilter;
import com.zjft.usp.wms.baseinfo.model.WarePropertyRight;
import com.zjft.usp.wms.baseinfo.service.WarePropertyRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 产权表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/ware-property-right")
public class WarePropertyRightController {

    @Autowired
    WarePropertyRightService warePropertyRightService;

    @GetMapping("/list")
    public Result<List<WarePropertyRight>> list(){
        return Result.succeed(warePropertyRightService.list());
    }

    @PostMapping("/query")
    public Result<ListWrapper<WarePropertyRight>> query(@RequestBody WarePropertyRightFilter warePropertyRightFilter, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        warePropertyRightFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(warePropertyRightService.queryWarePropertyRight(warePropertyRightFilter));
    }

    @GetMapping("/{rightId}")
    public Result<WarePropertyRight> find(@PathVariable Long rightId){
        return Result.succeed(warePropertyRightService.getById(rightId));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody WarePropertyRight warePropertyRight, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        warePropertyRight.setCorpId(reqParam.getCorpId());
        warePropertyRight.setCreateBy(user.getUserId());
        warePropertyRight.setCreateTime(DateUtil.date());
        warePropertyRightService.insertWarePropertyRight(warePropertyRight);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody WarePropertyRight warePropertyRight, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        warePropertyRight.setUpdateBy(user.getUserId());
        warePropertyRight.setUpdateTime(DateUtil.date());
        warePropertyRightService.updateWarePropertyRight(warePropertyRight);
        return Result.succeed();
    }

    @DeleteMapping("/{rightId}")
    public Result delete(@PathVariable Long rightId){
        warePropertyRightService.deleteWarePropertyRight(rightId);
        return Result.succeed();
    }
}
