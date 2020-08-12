package com.zjft.usp.wms.form.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.form.dto.FormTemplateDto;
import com.zjft.usp.wms.form.filter.FormTemplateFilter;
import com.zjft.usp.wms.form.model.FormTemplate;
import com.zjft.usp.wms.form.service.FormTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 表单模板主表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Api(tags = "表单模板信息")
@RestController
@RequestMapping("/form-template")
@Data
public class FormTemplateController {

    @Autowired
    private FormTemplateService formTemplateService;

    @ApiOperation(value = "查询表单模板")
    @PostMapping(value = "/listBy")
    public Result<List<FormTemplateDto>> listBy(@RequestBody FormTemplateDto formTemplateDto, @CommonReqParam ReqParam reqParam) {
        formTemplateDto.setCorpId(reqParam.getCorpId());
        return Result.succeed(formTemplateService.listBy(formTemplateDto));
    }

    @ApiOperation(value = "分页查询表单模板")
    @PostMapping(value = "/pageBy")
    public Result<ListWrapper<FormTemplateDto>> pageBy(@RequestBody FormTemplateFilter formTemplateFilter, @CommonReqParam ReqParam reqParam) {
        formTemplateFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(formTemplateService.pageBy(formTemplateFilter));
    }

    @ApiOperation(value = "查询表单模板，用于下拉选择")
    @PostMapping(value = "/listForSelectBy")
    public Result<List<FormTemplate>> listForSelectBy(@RequestBody FormTemplateDto formTemplateDto, @CommonReqParam ReqParam reqParam) {
        formTemplateDto.setCorpId(reqParam.getCorpId());
        return Result.succeed(formTemplateService.listForSelectBy(formTemplateDto));
    }

    @ApiOperation(value = "通过复制现有模板达到快速添加表单模板的目的")
    @PostMapping(value = "/addByCopy")
    public Result addByCopy(@RequestBody FormTemplateDto formTemplateDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        formTemplateDto.setCorpId(reqParam.getCorpId());
        formTemplateService.addByCopy(formTemplateDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "修改表单模板基本信息")
    @PostMapping(value = "/modBaseInfo")
    public Result modBaseInfo(@RequestBody FormTemplateDto formTemplateDto, @LoginUser UserInfo userInfo) {
        formTemplateService.modBaseInfo(formTemplateDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "修改表单模板字段配置")
    @PostMapping(value = "/modFieldList")
    public Result modFieldList(@RequestBody FormTemplateDto formTemplateDto, @LoginUser UserInfo userInfo) {
        formTemplateService.modFieldList(formTemplateDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除表单模板")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        formTemplateService.deleteById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个表单模板")
    @GetMapping(value = "/{id}")
    public Result<FormTemplate> selectById(@PathVariable("id") Long id) {
        return Result.succeed(formTemplateService.getById(id));
    }
}
