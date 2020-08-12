package com.zjft.usp.wms.baseinfo.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WareSupplierFilter;
import com.zjft.usp.wms.baseinfo.model.WareSupplier;
import com.zjft.usp.wms.baseinfo.service.WareSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 供应商表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/ware-supplier")
public class WareSupplierController {

    @Autowired
    WareSupplierService wareSupplierService;

    @GetMapping("/list/{corpId}")
    public Result<List<WareSupplier>> list(@PathVariable Long corpId){
        return Result.succeed(wareSupplierService.list(new QueryWrapper<WareSupplier>().eq("corp_id",corpId)));
    }

    @PostMapping("/query")
    public Result<ListWrapper<WareSupplier>> query(@RequestBody WareSupplierFilter wareSupplierFilter, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareSupplierFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(wareSupplierService.queryWareSupplier(wareSupplierFilter));
    }

    @GetMapping("/{supplierId}")
    public Result<WareSupplier> find(@PathVariable Long supplierId){
        return Result.succeed(wareSupplierService.getById(supplierId));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody WareSupplier wareSupplier, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareSupplier.setCorpId(reqParam.getCorpId());
        wareSupplier.setCreateBy(user.getUserId());
        wareSupplier.setCreateTime(DateUtil.date());
        wareSupplierService.insertWareSupplier(wareSupplier);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody WareSupplier wareSupplier, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareSupplier.setUpdateBy(user.getUserId());
        wareSupplier.setUpdateTime(DateUtil.date());
        wareSupplierService.updateWareSupplier(wareSupplier);
        return Result.succeed();
    }

    @DeleteMapping("/{supplierId}")
    public Result delete(@PathVariable Long supplierId){
        wareSupplierService.deleteWareSupplier(supplierId);
        return Result.succeed();
    }
}
