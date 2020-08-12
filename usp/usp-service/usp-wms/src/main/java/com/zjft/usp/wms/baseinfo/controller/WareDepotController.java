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
import com.zjft.usp.wms.baseinfo.dto.WareDepotDto;
import com.zjft.usp.wms.baseinfo.filter.WareDepotFilter;
import com.zjft.usp.wms.baseinfo.model.WareDepot;
import com.zjft.usp.wms.baseinfo.service.WareDepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 库房表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/ware-depot")
public class WareDepotController {

    @Autowired
    WareDepotService wareDepotService;

    @GetMapping("/list")
    public Result<List<WareDepot>> list(){
        return Result.succeed(wareDepotService.listWareDepot());
    }

    @GetMapping("/tree")
    public Result<List<WareDepotDto>> tree(@CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        return Result.succeed(wareDepotService.treeWareDepot(reqParam.getCorpId()));
    }

    @PostMapping("/query")
    public Result<ListWrapper<WareDepotDto>> query(@RequestBody WareDepotFilter wareDepotFilter, @CommonReqParam ReqParam reqParam){
        wareDepotFilter.setCorpId(reqParam.getCorpId());

        return Result.succeed(wareDepotService.queryWareDepot(wareDepotFilter));
    }

    @GetMapping("/{wareDepotId}")
    public Result<WareDepot> find(@PathVariable Long wareDepotId){
        return Result.succeed(wareDepotService.findWareDepotBy(wareDepotId));
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody WareDepotDto wareDepotDto, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        wareDepotDto.setId(KeyUtil.getId());
        wareDepotDto.setCorpId(reqParam.getCorpId());
        wareDepotDto.setCreateBy(user.getUserId());
        wareDepotDto.setCreateTime(DateUtil.date());
        wareDepotService.insertWareDepot(wareDepotDto);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody WareDepotDto wareDepotDto, @LoginUser UserInfo user){
        wareDepotDto.setUpdateBy(user.getUserId());
        wareDepotDto.setUpdateTime(DateUtil.date());
        wareDepotService.updateWareDepot(wareDepotDto);
        return Result.succeed();
    }

    @DeleteMapping("/{wareDepotId}")
    public Result delete(@PathVariable Long wareDepotId){
        wareDepotService.deleteWareDepot(wareDepotId);
        return Result.succeed();
    }
}
