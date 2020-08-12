package com.zjft.usp.wms.form.controller;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.form.model.FormTemplateField;
import com.zjft.usp.wms.form.service.FormTemplateFieldService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 表单模板字段表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/form-template-field")
public class FormTemplateFieldController {

    @Autowired
    private FormTemplateFieldService formTemplateFieldService;

    @ApiOperation(value = "查询表单字段列表")
    @GetMapping(value = "/listAllBy/{formTemplateId}")
    public Result<List<FormTemplateField>> listAllBy(@PathVariable("formTemplateId") Long formTemplateId) {
        return Result.succeed(formTemplateFieldService.listAllBy(formTemplateId));
    }

    @ApiOperation(value = "添加表单模板字段")
    @PostMapping(value = "/addField")
    public Result addField(@RequestBody FormTemplateField formTemplateField) {
        formTemplateFieldService.addField(formTemplateField);
        return Result.succeed();
    }

    @ApiOperation(value = "修改表单模板字段")
    @PostMapping(value = "/modField")
    public Result modField(@RequestBody FormTemplateField formTemplateField, UserInfo userInfo) {
        formTemplateFieldService.modField(formTemplateField, userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除表单模板单个字段")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        //TODO 有引用不能删除
        formTemplateFieldService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得表单模板单个字段")
    @GetMapping(value = "/{id}")
    public Result<FormTemplateField> selectById(@PathVariable("id") Long id) {
        return Result.succeed(formTemplateFieldService.getById(id));
    }
}
