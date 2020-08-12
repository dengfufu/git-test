package com.zjft.usp.wms.baseinfo.controller;


import com.zjft.usp.common.model.Result;
import com.zjft.usp.wms.baseinfo.dto.WareCatalogSpecsDto;
import com.zjft.usp.wms.baseinfo.service.WareCatalogSpecsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 物料分类规格定义表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/ware-catalog-specs")
public class WareCatalogSpecsController {

    @Autowired
    private WareCatalogSpecsService wareCatalogSpecService;

    @ApiOperation(value = "根据分类查询规格")
    @GetMapping(value = "/list/{catalogId}")
    public Result<List<WareCatalogSpecsDto>> listByCatalogId(@PathVariable("catalogId") Long catalogId) {
        return Result.succeed(this.wareCatalogSpecService.listByCatalogId(catalogId));
    }

}
