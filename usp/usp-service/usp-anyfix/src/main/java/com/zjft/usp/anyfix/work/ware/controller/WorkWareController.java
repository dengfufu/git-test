package com.zjft.usp.anyfix.work.ware.controller;

import com.zjft.usp.anyfix.work.ware.WareFilter;
import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import com.zjft.usp.anyfix.work.ware.service.WorkWareRecycleService;
import com.zjft.usp.anyfix.work.ware.service.WorkWareUsedService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.exception.AppException;
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
 * 更换备件控制器
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-10 08:45
 **/
@Api("更换备件")
@RestController
@RequestMapping("work-ware")
public class WorkWareController {

    @Autowired
    private WorkWareRecycleService workWareRecycleService;
    @Autowired
    private WorkWareUsedService workWareUsedService;

    @ApiOperation("添加回收部件")
    @PostMapping(value = "/recycle/add")
    public Result addRecycle(@RequestBody WareDto wareDto, @LoginUser UserInfo userInfo) {
        this.workWareRecycleService.add(wareDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("更新回收部件")
    @PostMapping(value = "/recycle/update")
    public Result updateRecycle(@RequestBody WareDto wareDto, @LoginUser UserInfo userInfo) {
        this.workWareRecycleService.update(wareDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("删除回收部件")
    @DeleteMapping(value = "/recycle/{recycleId}")
    public Result deleteRecycle(@PathVariable("recycleId") Long recycleId) {
        if (LongUtil.isZero(recycleId)) {
            throw new AppException("主键不能为空");
        }
        this.workWareRecycleService.removeById(recycleId);
        return Result.succeed();
    }

    @ApiOperation("添加使用部件")
    @PostMapping(value = "/used/add")
    public Result addUsed(@RequestBody WareDto wareDto, @LoginUser UserInfo userInfo) {
        this.workWareUsedService.add(wareDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("更新使用部件")
    @PostMapping(value = "/used/update")
    public Result updateUsed(@RequestBody WareDto wareDto, @LoginUser UserInfo userInfo) {
        this.workWareUsedService.update(wareDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("删除使用部件")
    @DeleteMapping(value = "/used/{usedId}")
    public Result deleteUsed(@PathVariable("usedId") Long usedId) {
        this.workWareUsedService.delete(usedId);
        return Result.succeed();
    }

    @ApiOperation("模糊匹配部件分类")
    @PostMapping(value = "/catalog/match")
    public Result<List<WareDto>> listWareCatalog(@RequestBody WareFilter wareFilter, @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(wareFilter.getCorpId())) {
            wareFilter.setCorpId(reqParam.getCorpId());
        }
        List<WareDto> list = this.workWareRecycleService.listCatalog(wareFilter);
        return Result.succeed(list);
    }

    @ApiOperation("模糊匹配部件品牌")
    @PostMapping(value = "/brand/match")
    public Result<List<WareDto>> listBrand(@RequestBody WareFilter wareFilter, @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(wareFilter.getCorpId())) {
            wareFilter.setCorpId(reqParam.getCorpId());
        }
        List<WareDto> list = this.workWareRecycleService.listBrand(wareFilter);
        return Result.succeed(list);
    }

    @ApiOperation("模糊匹配部件型号")
    @PostMapping(value = "/model/match")
    public Result<List<WareDto>> listModel(@RequestBody WareFilter wareFilter, @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(wareFilter.getCorpId())) {
            wareFilter.setCorpId(reqParam.getCorpId());
        }
        List<WareDto> list = this.workWareRecycleService.listModel(wareFilter);
        return Result.succeed(list);
    }

}
