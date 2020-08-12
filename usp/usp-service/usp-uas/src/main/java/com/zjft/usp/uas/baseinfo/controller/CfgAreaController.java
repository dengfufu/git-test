package com.zjft.usp.uas.baseinfo.controller;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.baseinfo.dto.AreaDto;
import com.zjft.usp.uas.baseinfo.dto.CfgAreaDto;
import com.zjft.usp.uas.baseinfo.model.CfgArea;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地区控制层
 *
 * @author zgpi
 * @version 1.0
 * @date 2019-08-19 11:18
 **/
@Api(tags = "地区")
@RestController
@RequestMapping("/area")
public class CfgAreaController {

    @Autowired
    private CfgAreaService cfgAreaService;

    @ApiOperation(value = "省市区列表")
    @GetMapping(value = "/list")
    public Result<List<CfgAreaDto>> listAreaDto() {
        List<CfgAreaDto> list = cfgAreaService.listAreaDto();
        return Result.succeed(list);
    }

    @ApiOperation(value = "省市列表")
    @GetMapping(value = "/province-city")
    public Result<List<CfgAreaDto>> listProvinceCity() {
        List<CfgAreaDto> list = cfgAreaService.listProvinceAndCity();
        return Result.succeed(list);
    }


    @ApiOperation(value = "省份列表")
    @GetMapping(value = "/province/list")
    public Result<List<CfgAreaDto>> listProvince() {
        return Result.succeed(cfgAreaService.listProvince(false,true));
    }

    @ApiOperation(value = "省份列表")
    @GetMapping(value = "/province-city/list")
    public Result<List<CfgAreaDto>> listProvinceAndCity() {
        return Result.succeed(cfgAreaService.listProvince(true,false));
    }

    @ApiOperation(value = "城市列表")
    @GetMapping(value = "/city/list/{province}")
    public Result<List<CfgAreaDto>> listCity(@PathVariable("province") String province) {
        return Result.succeed(cfgAreaService.listCity(province,false));
    }

    @ApiOperation(value = "区县列表")
    @GetMapping(value = "/district/list/{city}")
    public Result<List<CfgAreaDto>> listDistrict(@PathVariable("city") String city) {
        return Result.succeed(cfgAreaService.listDistrict(city));
    }

    @ApiOperation(value = "根据名称获得对象")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public Result<AreaDto> getArea(@RequestBody AreaDto areaDto) {
        areaDto = cfgAreaService.getAreaDto(areaDto);
        return Result.succeed(areaDto);
    }

    @ApiOperation(value = "查看地区详情")
    @GetMapping(value = "/{code}")
    public Result<AreaDto> getAreaByCode(@PathVariable("code") String code) {
        AreaDto areaDto = cfgAreaService.findAreaByCode(code);
        return Result.succeed(areaDto);
    }

    @ApiOperation(value = "远程调用：根据多个编码获得地区编码与名称映射")
    @PostMapping(value = "/feign/mapByCodeList")
    public Result<Map<String, String>> mapCodeAndNameByCodeList(@RequestBody List<Long> codeList) {
        // 重庆市特殊处理
        codeList.add(5001L);
        List<CfgArea> list = (List<CfgArea>) cfgAreaService.listByIds(codeList);
        Map<String, String> map = new HashMap<>();
        for (CfgArea cfgArea : list) {
            map.put(cfgArea.getCode(), cfgArea.getName());
        }
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：获得地区编码与名称映射")
    @GetMapping(value = "/feign/map")
    public Result<Map<String, String>> mapCodeAndName() {
        List<CfgArea> list = cfgAreaService.list();
        Map<String, String> map = new HashMap<>();
        for (CfgArea cfgArea : list) {
            map.put(cfgArea.getCode(), cfgArea.getName());
        }
        return Result.succeed(map);
    }

    @ApiOperation(value = "远程调用：根据编码获得地区信息")
    @GetMapping(value = "/feign/{code}")
    public Result<AreaDto> findAreaByCode(@PathVariable(name = "code") String code) {
        AreaDto areaDto = cfgAreaService.findAreaByCode(code);
        return Result.succeed(areaDto);
    }
}
