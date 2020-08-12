package com.zjft.usp.wms.baseinfo.controller;

import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.dto.WareBrandDto;
import com.zjft.usp.wms.baseinfo.filter.WareBrandFilter;
import com.zjft.usp.wms.baseinfo.model.WareBrand;
import com.zjft.usp.wms.baseinfo.service.WareBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 物料品牌表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Api(tags = "物料品牌")
@RestController
@RequestMapping("/ware-brand")
public class WareBrandController {

    @Autowired
    private WareBrandService wareBrandService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WareBrandDto>> query(@RequestBody WareBrandFilter wareBrandFilter, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        wareBrandFilter.setCorpId(reqParam.getCorpId());
        ListWrapper<WareBrandDto> listWrapper = this.wareBrandService.queryWareBrand(wareBrandFilter);
        return Result.succeed(listWrapper);
    }

    @GetMapping("/list")
    public Result<List<WareBrand>> list(@CommonReqParam ReqParam reqParam){
        return Result.succeed(this.wareBrandService.listWareBrand(reqParam.getCorpId()));
    }

    @ApiOperation(value = "模糊匹配")
    @PostMapping(value = "/match")
    public Result<List<WareBrand>> match(@RequestBody WareBrandFilter wareBrandFilter, @CommonReqParam ReqParam reqParam) {
        wareBrandFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(wareBrandService.match(wareBrandFilter));
    }

    @ApiOperation(value = "获得单个物料品牌")
    @GetMapping(value = "/{id}")
    public Result<WareBrand> findById(@PathVariable("id") Long id) {
        WareBrand wareBrand = wareBrandService.findWareBrandBy(id);
        return Result.succeed(wareBrand);
    }

    @ApiOperation(value = "添加物料品牌")
    @PostMapping(value = "/add")
    public Result<String> insert(@RequestBody WareBrand wareBrand,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        wareBrand.setCorpId(reqParam.getCorpId());
        wareBrand.setCreateBy(userInfo.getUserId());
        wareBrand.setCreateTime(DateUtil.date().toTimestamp());
        wareBrandService.insertWareBrand(wareBrand);
        return Result.succeed();
    }

    @ApiOperation(value = "修改物料品牌")
    @PostMapping(value = "/update")
    public Result<String> update(@RequestBody WareBrand wareBrand,
                                 @LoginUser UserInfo userInfo) {
        wareBrand.setUpdateBy(userInfo.getUserId());
        wareBrand.setUpdateTime(DateUtil.date().toTimestamp());
        wareBrandService.updateWareBrand(wareBrand);
        return Result.succeed();
    }

    @ApiOperation(value = "删除物料品牌")
    @DeleteMapping(value = "/{id}")
    public Result<String> delete(@PathVariable("id") Long id) {
        wareBrandService.removeById(id);
        return Result.succeed();
    }
}
