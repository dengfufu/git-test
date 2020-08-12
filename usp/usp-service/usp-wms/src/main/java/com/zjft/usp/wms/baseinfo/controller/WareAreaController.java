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
import com.zjft.usp.wms.baseinfo.dto.WareAreaDto;
import com.zjft.usp.wms.baseinfo.filter.WareAreaFilter;
import com.zjft.usp.wms.baseinfo.model.WareArea;
import com.zjft.usp.wms.baseinfo.service.WareAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 供应商表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-07
 */
@RestController
@RequestMapping("/ware-area")
public class WareAreaController {

    @Autowired
    WareAreaService wareAreaService;

    @GetMapping("/list")
    public Result<List<WareArea>> list() {
        return Result.succeed(wareAreaService.listWareArea());
    }

    @PostMapping("/query")
    public Result<ListWrapper<WareAreaDto>> query(@RequestBody WareAreaFilter wareAreaFilter, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareAreaFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(wareAreaService.queryWareArea(wareAreaFilter));
    }

    @GetMapping("/{wareAreaId}")
    public Result<WareArea> find(@PathVariable Long wareAreaId) {
        return Result.succeed(wareAreaService.findWareAreaBy(wareAreaId));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody WareAreaDto wareAreaDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam){
        wareAreaDto.setId(KeyUtil.getId());
        wareAreaDto.setCorpId(reqParam.getCorpId());
        wareAreaDto.setCreateBy(userInfo.getUserId());
        wareAreaDto.setCreateTime(DateUtil.date());
        wareAreaService.insertWareArea(wareAreaDto);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody WareAreaDto wareAreaDto, @LoginUser UserInfo userInfo){
        wareAreaDto.setUpdateBy(userInfo.getUserId());
        wareAreaDto.setUpdateTime(DateUtil.date());
        wareAreaService.updateWareArea(wareAreaDto);
        return Result.succeed();
    }

    @DeleteMapping("/{wareAreaId}")
    public Result delete(@PathVariable Long wareAreaId){
        wareAreaService.deleteWareArea(wareAreaId);
        return Result.succeed();
    }

}
