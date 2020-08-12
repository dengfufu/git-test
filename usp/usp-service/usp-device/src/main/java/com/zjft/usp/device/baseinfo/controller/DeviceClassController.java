package com.zjft.usp.device.baseinfo.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.composite.DeviceClassCompoService;
import com.zjft.usp.device.baseinfo.dto.DeviceClassCompoDto;
import com.zjft.usp.device.baseinfo.dto.DeviceClassDto;
import com.zjft.usp.device.baseinfo.dto.DeviceLargeClassDto;
import com.zjft.usp.device.baseinfo.dto.DeviceSmallClassDto;
import com.zjft.usp.device.baseinfo.filter.DeviceLargeClassFilter;
import com.zjft.usp.device.baseinfo.filter.DeviceSmallClassFilter;
import com.zjft.usp.device.baseinfo.model.DeviceLargeClass;
import com.zjft.usp.device.baseinfo.model.DeviceSmallClass;
import com.zjft.usp.device.baseinfo.service.DeviceLargeClassService;
import com.zjft.usp.device.baseinfo.service.DeviceSmallClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备大类表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "设备分类")
@RestController
public class DeviceClassController {

    @Autowired
    private DeviceClassCompoService deviceClassCompoService;
    @Autowired
    private DeviceLargeClassService deviceLargeClassService;
    @Autowired
    private DeviceSmallClassService deviceSmallClassService;

    @ApiOperation(value = "获得设备大类列表")
    @PostMapping(value = "/device-large-class/list")
    public Result<List<DeviceLargeClassDto>> listLargeClass(@RequestBody DeviceLargeClassFilter deviceLargeClassFilter,
                                                            @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceLargeClassFilter.getCorp())) {
            deviceLargeClassFilter.setCorp(reqParam.getCorpId());
        }
        List<DeviceLargeClassDto> largeClassList = deviceLargeClassService.listDeviceLargeClass(deviceLargeClassFilter);
        return Result.succeed(largeClassList);
    }

    @ApiOperation(value = "分页查询设备大类")
    @PostMapping(value = "/device-large-class/query")
    public Result<ListWrapper<DeviceLargeClassDto>> query(@RequestBody DeviceLargeClassFilter deviceLargeClassFilter,
                                                          @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceLargeClassFilter.getCorp())) {
            deviceLargeClassFilter.setCorpIdForDemander(reqParam.getCorpId());
        }
        ListWrapper<DeviceLargeClassDto> listWrapper = this.deviceLargeClassService.query(deviceLargeClassFilter);
        return Result.succeed(listWrapper);
    }

    @ApiOperation(value = "获得单个设备大类")
    @GetMapping(value = "/device-large-class/{id}")
    public Result<DeviceLargeClass> findLargeClassById(@PathVariable("id") Long id) {
        DeviceLargeClass deviceLargeClass = deviceLargeClassService.getById(id);
        return Result.succeed(deviceLargeClass);
    }

    @ApiOperation(value = "获得设备大类最大顺序号")
    @GetMapping(value = "/device-large-class/max/sortNo/{corp}")
    public Result<Integer> findLargeClassMaxSortNo(@PathVariable("corp") Long corp,@CommonReqParam ReqParam reqParam) {
        Long corpId = LongUtil.isZero(corp) ? reqParam.getCorpId() : corp;
        Integer sortNo = deviceLargeClassService.findMaxSortNo(corpId);
        return Result.succeed(sortNo);
    }

    @ApiOperation(value = "添加设备大类")
    @PostMapping(value = "/device-large-class/add")
    public Result addLargeClass(@RequestBody DeviceLargeClass deviceLargeClass,
                                @LoginUser UserInfo userInfo,
                                @CommonReqParam ReqParam reqParam) {
        deviceLargeClassService.save(deviceLargeClass, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改设备大类")
    @PostMapping(value = "/device-large-class/update")
    public Result updateLargeClass(@RequestBody DeviceLargeClass deviceLargeClass,
                                   @LoginUser UserInfo userInfo) {
        deviceLargeClassService.update(deviceLargeClass, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除设备大类")
    @DeleteMapping(value = "/device-large-class/{id}")
    public Result deleteLargeClass(@PathVariable("id") Long id) {
        deviceLargeClassService.delete(id);
        return Result.succeed();
    }


    @PostMapping(value = "/device-small-class/list")
    @ApiOperation(value = "获得设备小类列表")
    public Result listSmallClass(@RequestBody DeviceSmallClassDto smallClassDto,
                                 @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(smallClassDto.getCorp()) && LongUtil.isZero(smallClassDto.getLargeClassId())) {
            smallClassDto.setCorp(reqParam.getCorpId());
        }
        List<DeviceSmallClassDto> smallClassList = deviceSmallClassService.listDeviceSmallClass(smallClassDto);
        return Result.succeed(smallClassList);
    }

    @ApiOperation(value = "分页查询设备小类")
    @PostMapping(value = "/device-small-class/query")
    public Result<ListWrapper<DeviceSmallClassDto>> query(@RequestBody DeviceSmallClassFilter deviceSmallClassFilter,
                                                          @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceSmallClassFilter.getCorp())) {
            deviceSmallClassFilter.setCorpIdForDemander(reqParam.getCorpId());
        }
        ListWrapper<DeviceSmallClassDto> listWrapper = deviceClassCompoService.queryDeviceSmallClass(deviceSmallClassFilter);
        return Result.succeed(listWrapper);
    }

    @ApiOperation(value = "获得单个设备小类")
    @GetMapping(value = "/device-small-class/{id}")
    public Result<DeviceSmallClassDto> findById(@PathVariable("id") Long id) {
        return Result.succeed(deviceClassCompoService.findDeviceSmallClass(id));
    }

    @ApiOperation(value = "获得设备小类最大顺序号")
    @GetMapping(value = "/device-small-class/max/sortNo/{corp}")
    public Result<Integer> findSmallClassMaxSortNo(@PathVariable("corp") Long corp,@CommonReqParam ReqParam reqParam) {
        Long corpId = LongUtil.isZero(corp) ? reqParam.getCorpId() : corp;
        Integer sortNo = deviceSmallClassService.findMaxSortNo(corpId);
        return Result.succeed(sortNo);
    }

    @ApiOperation(value = "添加设备小类")
    @PostMapping(value = "/device-small-class/add")
    public Result addSmallClass(@RequestBody DeviceSmallClassDto deviceSmallClassDto,
                                @LoginUser UserInfo userInfo,
                                @CommonReqParam ReqParam reqParam) {
        deviceClassCompoService.addDeviceSmallClass(deviceSmallClassDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改设备小类")
    @PostMapping(value = "/device-small-class/update")
    public Result updateSmallClass(@RequestBody DeviceSmallClassDto deviceSmallClassDto,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        deviceClassCompoService.updateDeviceSmallClass(deviceSmallClassDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "删除单个设备小类")
    @DeleteMapping(value = "/device-small-class/{id}")
    public Result deleteSmallClass(@PathVariable("id") Long id) {
        deviceClassCompoService.delDeviceSmallClass(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得设备分类列表")
    @PostMapping(value = "/device-class/list")
    public Result<List<DeviceLargeClassDto>> listDeviceClass(@RequestBody DeviceClassDto deviceClassDto,
                                                             @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceClassDto.getCorpId())) {
            deviceClassDto.setCorpId(reqParam.getCorpId());
        }
        List<DeviceLargeClassDto> list = deviceSmallClassService.listDeviceClass(deviceClassDto.getCorpId());
        return Result.succeed(list);
    }

    @ApiOperation(value = "模糊查询设备大类列表")
    @PostMapping(value = "/device-large-class/match")
    public Result<List<DeviceLargeClass>> matchDeviceSmallClass(@RequestBody DeviceLargeClassFilter deviceLargeClassFilter,
                                                                @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceLargeClassFilter.getCorp())) {
            deviceLargeClassFilter.setCorp(reqParam.getCorpId());
        }
        List<DeviceLargeClass> list = deviceLargeClassService.matchDeviceLargeClass(deviceLargeClassFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "模糊查询设备小类列表")
    @PostMapping(value = "/device-small-class/match")
    public Result<List<DeviceSmallClass>> matchDeviceSmallClass(@RequestBody DeviceSmallClassFilter deviceSmallClassFilter,
                                                                @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(deviceSmallClassFilter.getCorp()) && LongUtil.isZero(deviceSmallClassFilter.getLargeClassId())) {
            deviceSmallClassFilter.setCorp(reqParam.getCorpId());
        }
        List<DeviceSmallClass> list = deviceSmallClassService.matchDeviceSmallClass(deviceSmallClassFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "远程调用：获得单个设备小类详情")
    @GetMapping(value = "/device-small-class/feign/{id}")
    public Result<DeviceSmallClassDto> findByIdFeign(@PathVariable("id") Long id) {
        return Result.succeed(deviceSmallClassService.findDtoById(id));
    }

    @ApiOperation(value = "远程调用：获得某个客户的设备小类映射")
    @GetMapping(value = "/device-small-class/feign/map/{customCorp}")
    public Result<Map<Long, String>> mapSmallClass(@PathVariable("customCorp") Long customCorp) {
        Map<Long, String> map = this.deviceSmallClassService.mapIdAndNameByCorp(customCorp);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：获得某个客户的设备大类映射")
    @GetMapping(value = "/device-large-class/feign/map/{customCorp}")
    public Result<Map<Long, String>> mapLargeClass(@PathVariable("customCorp") Long customCorp) {
        Map<Long, String> map = this.deviceLargeClassService.mapClassIdAndNameByCorp(customCorp);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：根据客户list获取设备大类映射")
    @PostMapping(value = "/device-large-class/feign/mapByCorpIdList")
    public Result<Map<Long, String>> mapLargeClassByCorpList(@RequestParam("corpIdList") List<Long> corpIdList) {
        Map<Long, String> map = this.deviceLargeClassService.mapClassIdAndNameByCorpList(corpIdList);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：根据客户list获取设备小类映射")
    @PostMapping(value = "/device-small-class/feign/mapByCorpIdList")
    public Result<Map<Long, String>> mapSmallClassByCorpIdList(@RequestParam("corpIdList") List<Long> corpIdList) {
        Map<Long, String> map = this.deviceSmallClassService.mapIdAndNameByCorpIdList(corpIdList);
        return Result.succeed(map);
    }

    @ApiOperation(value = "获得设备分类小类id和大类名称-小类名称map")
    @GetMapping(value = "/device-class/feign/map/{customCorp}")
    public Result<Map<Long, String>> getDeviceClassMap(@PathVariable("customCorp") Long customCorp) {
        Map<Long, String> map = deviceSmallClassService.getDeviceClassMap(customCorp);
        return Result.succeed(map);
    }

    @ApiOperation(value = "根据企业编号list获取设备小类ID与设备大类、设备小类组合对象的映射")
    @PostMapping(value = "/device-class/feign/mapDeviceClassCompoByCorpIds")
    public Result<Map<Long, DeviceClassCompoDto>> mapDeviceClassCompoByCorpIds(@RequestBody List<Long> corpList) {
        Map<Long, DeviceClassCompoDto> map = deviceSmallClassService.mapDeviceClassCompoByCorpIds(corpList);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：根据设备大类编号获取设备小类编号list")
    @PostMapping(value = "/device-small-class/listSmallClassIdByLargeClassId")
    public Result<List<Long>> listSmallClassIdByLargeClassId(@RequestParam("largeClassId") Long largeClassId) {
        return Result.succeed(this.deviceSmallClassService.listSmallClassIdByLargeClassId(largeClassId));
    }
}
