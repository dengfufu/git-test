package com.zjft.usp.anyfix.baseinfo.controller;

import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDataDto;
import com.zjft.usp.anyfix.baseinfo.filter.CustomFieldDataFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldDataService;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自定义字段数据表 前端控制器
 * </p>
 *
 * @author cxd
 * @since 2020-01-08
 */
@Api(tags = "自定义字段数据")
@RestController
@RequestMapping("/custom-field-data")
public class CustomFieldDataController {
    @Autowired
    private CustomFieldDataService customFieldDataService;

    @ApiOperation(value = "添加自定义配置字段数据")
    @PostMapping(value = "/feign/add")
    public Result addCustomField(@RequestBody List<CustomFieldData> customFieldDataList) {
        customFieldDataService.addCustomFieldDataList(customFieldDataList);
        return Result.succeed();
    }

    @ApiOperation(value = "根据条件查询自定义字段数据")
    @PostMapping(value = "/feign/query")
    public Result<List<CustomFieldDataDto>> queryCustomFieldData(@RequestBody CustomFieldDataFilter customFieldDataFilter) {
        List<CustomFieldData> customFieldDataList = customFieldDataService.queryCustomFieldData(customFieldDataFilter);
        List<CustomFieldDataDto> customFieldDtoList = new ArrayList<>();
        CustomFieldDataDto customFieldDataDto;
        for (CustomFieldData customFieldData : customFieldDataList) {
            customFieldDataDto = new CustomFieldDataDto();
            BeanUtils.copyProperties(customFieldData, customFieldDataDto);
            customFieldDtoList.add(customFieldDataDto);
        }
        return Result.succeed(customFieldDtoList);
    }


    @ApiOperation(value = "删除自定义字段数据")
    @DeleteMapping(value = "/feign/{deviceId}")
    public Result deleteByDeviceId(@PathVariable("deviceId") Long deviceId) {
        customFieldDataService.deleteCustomFieldData(deviceId);
        return Result.succeed();
    }
}
