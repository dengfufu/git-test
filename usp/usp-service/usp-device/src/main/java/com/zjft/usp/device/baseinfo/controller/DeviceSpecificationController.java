package com.zjft.usp.device.baseinfo.controller;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.device.baseinfo.dto.DeviceSpecificationDto;
import com.zjft.usp.device.baseinfo.filter.DeviceSpecificationFilter;
import com.zjft.usp.device.baseinfo.model.DeviceSpecification;
import com.zjft.usp.device.baseinfo.service.DeviceSpecificationService;
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
 * 设备规格表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-1-20
 */
@Api(tags = "设备规格")
@RestController
@RequestMapping("/device-specification")
public class DeviceSpecificationController {

    @Autowired
    private DeviceSpecificationService deviceSpecificationService;

    @ApiOperation(value = "分页查询设备规格列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<DeviceSpecification>> query(@RequestBody DeviceSpecificationFilter deviceSpecificationFilter) {
        return Result.succeed(deviceSpecificationService.query(deviceSpecificationFilter));
    }

    @ApiOperation(value = "查询设备规格列表")
    @PostMapping(value = "/list")
    public Result<List<DeviceSpecificationDto>> list(@RequestBody DeviceSpecificationFilter deviceSpecificationFilter) {
        return Result.succeed(deviceSpecificationService.list(deviceSpecificationFilter));
    }

    @ApiOperation(value = "远程调用：获得设备规格详情")
    @GetMapping(value = "/feign/{id}")
    public Result<DeviceSpecification> findById(@PathVariable("id") Long id) {
        return Result.succeed(deviceSpecificationService.getById(id));
    }

    @ApiOperation(value = "远程调用：设备规格ID与名称映射")
    @PostMapping(value = "/feign/mapByIdList")
    public Result<Map<Long, String>> mapSpecificationAndNameByIdList(@RequestBody List<Long> IdList) {
        return Result.succeed(deviceSpecificationService.mapIdAndName(IdList));
    }

    @ApiOperation(value = "远程调用：获得设备规格map")
    @GetMapping(value = "/feign/map/{customCorp}")
    public Result<Map<Long, String>> mapSpecificationByCorp(@PathVariable("customCorp") Long customCorp) {
        Map<Long, String> map = this.deviceSpecificationService.mapSpecificationByCorp(customCorp);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：获得某个小类设备规格map")
    @GetMapping(value = "/feign/mapBySmallClassId/{smallClassId}")
    public Result<Map<Long, String>> mapSpecificationBySmallClassId(@PathVariable("smallClassId") Long smallClassId) {
        Map<Long, String> map = this.deviceSpecificationService.mapBySmallClassId(smallClassId);
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：根据规格编号列表获取规格编号->[小类名称]规格名称映射")
    @PostMapping(value = "/feign/mapSpecIdAndSmallClassSpecName")
    public Result<Map<Long, String>> mapSpecIdAndSmallClassSpecName(@RequestBody List<Long> idList) {
        return Result.succeed(this.deviceSpecificationService.mapIdAndSmallClassSpecName(idList));
    }
}
