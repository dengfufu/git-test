package com.zjft.usp.wms.baseinfo.controller;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.wms.baseinfo.dto.LargeClassDto;
import com.zjft.usp.wms.baseinfo.service.LargeClassService;
import com.zjft.usp.wms.form.dto.FormTemplateDto;
import com.zjft.usp.wms.form.service.FormTemplateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 业务大类表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/large-class")
public class LargeClassController {

    @Autowired
    private LargeClassService largeClassService;

    @ApiOperation(value = "查询业务大类")
    @PostMapping(value = "/query")
    public Result<List<LargeClassDto>> query(@RequestBody LargeClassDto largeClassDto) {
        return Result.succeed(largeClassService.query(largeClassDto));
    }

}
