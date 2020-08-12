package com.zjft.usp.device.baseinfo.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceBrandDto;
import com.zjft.usp.device.baseinfo.filter.DeviceBrandFilter;
import com.zjft.usp.device.baseinfo.model.DeviceBrand;
import com.zjft.usp.device.baseinfo.service.DeviceBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备品牌表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "设备品牌")
@RestController
@RequestMapping("/device-brand")
public class DeviceBrandController {

    @Autowired
    private DeviceBrandService deviceBrandService;

    @ApiOperation(value = "根据条件分页查询")
    @PostMapping(value = "/query")
    public Result<ListWrapper<DeviceBrandDto>> query(@RequestBody DeviceBrandFilter deviceBrandFilter, @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceBrandFilter.getCorp())) {
            deviceBrandFilter.setCorpIdForDemander(reqParam.getCorpId());
        }
        ListWrapper<DeviceBrandDto> listWrapper = this.deviceBrandService.query(deviceBrandFilter);
        return Result.succeed(listWrapper);
    }

    @ApiOperation(value = "设备品牌列表")
    @PostMapping(value = "/list")
    public Result<List<DeviceBrandDto>> list(@RequestBody DeviceBrandFilter deviceBrandFilter,
                                             @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceBrandFilter.getCorp())) {
            deviceBrandFilter.setCorp(reqParam.getCorpId());
        }
        List<DeviceBrandDto> deviceBrandList = deviceBrandService.listDeviceBrand(deviceBrandFilter);
        return Result.succeed(deviceBrandList);
    }

    @ApiOperation(value = "品牌型号列表")
    @PostMapping(value = "/brand/model/list")
    public Result<List<DeviceBrandDto>> listBrandModel(@RequestBody DeviceBrandDto brandDto,
                                                       @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(brandDto.getCorp())) {
            brandDto.setCorp(reqParam.getCorpId());
        }
        List<DeviceBrandDto> deviceBrandList = deviceBrandService.listBrandModel(brandDto.getCorp(),
                brandDto.getSmallClassId());
        return Result.succeed(deviceBrandList);
    }

    @ApiOperation(value = "获得单个设备品牌")
    @GetMapping(value = "/{id}")
    public Result<DeviceBrand> findById(@PathVariable("id") Long id) {
        DeviceBrand deviceBrand = deviceBrandService.getById(id);
        return Result.succeed(deviceBrand);
    }

    @ApiOperation(value = "添加设备品牌")
    @PostMapping(value = "/add")
    public Result insert(@RequestBody DeviceBrand deviceBrand,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        deviceBrandService.save(deviceBrand,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改设备品牌")
    @PostMapping(value = "/update")
    public Result update(@RequestBody DeviceBrand deviceBrand,
                                 @LoginUser UserInfo userInfo) {
        deviceBrandService.update(deviceBrand,userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除设备品牌")
    @DeleteMapping(value = "/{id}")
    public Result<String> delete(@PathVariable("id") Long id) {
        deviceBrandService.delete(id);
        return Result.succeed();
    }

    @ApiOperation(value = "模糊查询设备品牌")
    @PostMapping(value = "/match")
    public Result<List<DeviceBrand>> matchDeviceBrand(@RequestBody DeviceBrandFilter deviceBrandFilter,
                                                      @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceBrandFilter.getCorp())) {
            deviceBrandFilter.setCorp(reqParam.getCorpId());
        }
        return Result.succeed(deviceBrandService.matchDeviceBrand(deviceBrandFilter));
    }

    @ApiOperation(value = "远程调用获得单个设备品牌")
    @GetMapping(value = "/feign/{id}")
    public Result<DeviceBrand> findByIdFeign(@PathVariable("id") Long id) {
        DeviceBrand deviceBrand = deviceBrandService.getById(id);
        return Result.succeed(deviceBrand);
    }

    @ApiOperation(value = "远程调用获取品牌编号和名称的映射")
    @GetMapping(value = "/feign/map/{corpId}")
    public Result<Map<Long, String>> mapIdAndNameByCorp(@PathVariable("corpId") Long corpId) {
        Map<Long, String> map = this.deviceBrandService.mapIdAndNameByCorpId(corpId);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用根据客户编号list获取设备品牌映射")
    @PostMapping(value = "/feign/mapByCorpIdList")
    public Result<Map<Long, String>> mapIdAndNameByCorpIdList(@RequestParam("corpIdList") List<Long> corpIdList) {
        Map<Long, String> map = this.deviceBrandService.mapIdAndNameByCorpIdList(corpIdList);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用根据客户编号jsonList获取设备品牌映射")
    @PostMapping(value = "/feign/mapByJsonCorpIds")
    public Result<Map<Long, String>> mapDeviceBrandByJsonCorpIds(@RequestBody List<Long> corpIdList) {
        Map<Long, String> map = this.deviceBrandService.mapIdAndNameByCorpIdList(corpIdList);
        return Result.succeed(map);
    }
}
