package com.zjft.usp.anyfix.baseinfo.controller;

import com.zjft.usp.anyfix.baseinfo.dto.CustomFieldDto;
import com.zjft.usp.anyfix.baseinfo.filter.CustomFieldFilter;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldDataSourceService;
import com.zjft.usp.anyfix.baseinfo.service.CustomFieldService;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 自定义字段配置表 前端控制器
 * </p>
 *
 * @author chenxiaod
 * @since 2019-10-08
 */
@Api(tags = "自定义字段配置")
@RestController
@RequestMapping("/custom-field")
public class CustomFieldController {

    @Autowired
    private CustomFieldService customFieldService;
    @Autowired
    CustomFieldDataSourceService customFieldDataSourceService;

    @ApiOperation(value = "分页查询自定义配置字段列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<CustomFieldDto>> query(@RequestBody CustomFieldFilter customFieldFilter) {
        return Result.succeed(customFieldService.query(customFieldFilter));
    }

    @ApiOperation(value = "根据条件查询自定义配置字段列表(包含数据源)")
    @PostMapping(value = "/list")
    public Result<List<CustomFieldDto>> customFieldList(@RequestBody CustomFieldFilter customFieldFilter) {
        return Result.succeed(customFieldService.customFieldList(customFieldFilter));
    }

    @ApiOperation(value = "查找自定义配置字段")
    @GetMapping(value = "/{fieldId}")
    public Result<CustomFieldDto> findCustomFieldById(@PathVariable("fieldId") Long fieldId) {
        return Result.succeed(customFieldService.findCustomFieldById(fieldId));
    }

    @ApiOperation(value = "添加自定义配置字段")
    @PostMapping(value = "/add")
    public Result addCustomField(@RequestBody CustomFieldDto customFieldDto,
                                 @LoginUser UserInfo userInfo) {
        customFieldService.save(customFieldDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "修改自定义配置字段")
    @PostMapping(value = "/update")
    public Result updateCustomField(@RequestBody CustomFieldDto customFieldDto, @LoginUser UserInfo userInfo) {
        customFieldService.update(customFieldDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除自定义配置字段")
    @DeleteMapping(value = "/{fieldId}")
    public Result deleteById(@PathVariable("fieldId") Long fieldId) {
        customFieldService.deleteCustomField(fieldId);
        return Result.succeed();
    }
}
