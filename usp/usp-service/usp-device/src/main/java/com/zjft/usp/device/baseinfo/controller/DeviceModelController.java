package com.zjft.usp.device.baseinfo.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceModelDto;
import com.zjft.usp.device.baseinfo.filter.DeviceModelFilter;
import com.zjft.usp.device.baseinfo.model.DeviceModel;
import com.zjft.usp.device.baseinfo.service.DeviceModelService;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备型号表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "设备型号")
@RestController
@RequestMapping("/device-model")
public class DeviceModelController {

    @Autowired
    private DeviceModelService deviceModelService;

    @ApiOperation(value = "分页查询设备型号")
    @PostMapping(value = "/query")
    public Result<ListWrapper<DeviceModelDto>> query(@RequestBody DeviceModelFilter deviceModelFilter,
                                                     @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceModelFilter.getCorp())) {
            deviceModelFilter.setCorpIdForDemander(reqParam.getCorpId());
        }
        ListWrapper<DeviceModelDto> listWrapper = this.deviceModelService.query(deviceModelFilter);
        return Result.succeed(listWrapper);
    }

    @PostMapping(value = "/list")
    @ApiOperation(value = "获得设备型号列表")
    public Result<List<DeviceModelDto>> listDeviceModel(@RequestBody DeviceModelFilter modelFilter,
                                                        @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(modelFilter.getCorp())){
            if (LongUtil.isZero(modelFilter.getBrandId()) && LongUtil.isZero(modelFilter.getSmallClassId())) {
                modelFilter.setCorp(reqParam.getCorpId());
            }
        }
        List<DeviceModelDto> deviceModelList = deviceModelService.listDeviceModel(modelFilter);
        return Result.succeed(deviceModelList);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获得单个设备型号")
    public Result<DeviceModel> findById(@PathVariable("id") Long id) {
        DeviceModel deviceModel = deviceModelService.getById(id);
        return Result.succeed(deviceModel);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加设备型号")
    public Result insert(@RequestBody DeviceModel deviceModel,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        deviceModelService.save(deviceModel,userInfo,reqParam);
        return Result.succeed();
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改设备型号")
    public Result updateDeviceModel(@RequestBody DeviceModel deviceModel,
                                            @LoginUser UserInfo userInfo) {
        deviceModelService.update(deviceModel,userInfo);
        return Result.succeed();
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备型号")
    public Result deleteDeviceModel(@PathVariable("id") Long id) {
        deviceModelService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "模糊查询设备型号")
    @PostMapping(value = "/match")
    public Result<List<DeviceModel>> matchDeviceModel(@RequestBody DeviceModelFilter deviceModelFilter,
                                                      @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceModelFilter.getCorp())) {
            if (LongUtil.isZero(deviceModelFilter.getCorp())) {
                if (LongUtil.isZero(deviceModelFilter.getBrandId()) &&
                        LongUtil.isZero(deviceModelFilter.getLargeClassId()) &&
                        LongUtil.isZero(deviceModelFilter.getSmallClassId())) {
                    deviceModelFilter.setCorp(reqParam.getCorpId());
                }
            }
        }
        return Result.succeed(deviceModelService.matchDeviceModel(deviceModelFilter));
    }

    @GetMapping(value = "/feign/{id}")
    @ApiOperation(value = "远程调用：获得单个设备型号")
    public Result<DeviceModel> findByIdFeign(@PathVariable("id") Long id) {
        DeviceModel deviceModel = deviceModelService.getById(id);
        return Result.succeed(deviceModel);
    }

    @ApiOperation(value = "远程调用：获取型号编号和名称的映射")
    @GetMapping(value = "/feign/map")
    public Result<Map<Long, String>> mapIdAndName() {
        Map<Long, String> map = this.deviceModelService.mapIdAndName();
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：根据企业ID列表获取型号编号和名称的映射")
    @PostMapping(value = "/feign/mapByCorpIdList")
    public Result<Map<Long, String>> mapIdAndNameByCorpIdList(@RequestParam("corpIdList") List<Long> corpIdList) {
        Map<Long, String> map = this.deviceModelService.mapIdAndNameByCorpIdList(corpIdList);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：根据企业ID列表获取型号编号和名称的映射")
    @PostMapping(value = "/feign/mapByJsonCorpIds")
    public Result<Map<Long, String>> mapDeviceModelByJsonCorpIds(@RequestBody List<Long> corpIdList) {
        Map<Long, String> map = this.deviceModelService.mapIdAndNameByCorpIdList(corpIdList);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：根据企业ID型号编号小类编号获取型号编号和名称的映射")
    @PostMapping(value = "/feign/mapByCorpIdBrandIdSmallClassId")
    public Result<Map<Long, String>> mapByCorpIdBrandIdSmallClassId(@RequestParam("corpId") Long corpId,
                                                                    @RequestParam("brandId") Long brandId,
                                                                    @RequestParam("smallClassId") Long smallClassId) {
        DeviceModelFilter deviceModelFilter = new DeviceModelFilter();
        deviceModelFilter.setCorp(corpId);
        deviceModelFilter.setBrandId(brandId);
        deviceModelFilter.setSmallClassId(smallClassId);
        Map<Long, String> map = deviceModelService.listDeviceModel(deviceModelFilter).stream()
                .collect(Collectors.toMap(item -> item.getId(), item -> item.getName(), (id, name) -> id, LinkedHashMap::new));
        return Result.succeed(map);
    }
}
