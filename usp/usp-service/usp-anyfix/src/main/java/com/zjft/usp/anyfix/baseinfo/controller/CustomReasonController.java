package com.zjft.usp.anyfix.baseinfo.controller;


import com.zjft.usp.anyfix.baseinfo.dto.CustomReasonDto;
import com.zjft.usp.anyfix.baseinfo.filter.CustomReasonFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomReason;
import com.zjft.usp.anyfix.baseinfo.service.CustomReasonService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 客户原因表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "客户原因")
@RestController
@RequestMapping("/custom-reason")
public class CustomReasonController {

    @Autowired
    private CustomReasonService customReasonService;

    @ApiOperation(value = "添加客户原因")
    @PostMapping(value = "/add")
    public Result addCustomReason(@RequestBody CustomReason customReason,
                                  @LoginUser UserInfo userInfo,
                                  @CommonReqParam ReqParam reqParam) {
        customReasonService.save(customReason,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改客户原因")
    @PostMapping(value = "/update")
    public Result updateCustomReason(@RequestBody CustomReason customReason,
                                     @LoginUser UserInfo userInfo) {
        customReasonService.update(customReason,userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除客户原因")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        customReasonService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个客户原因")
    @GetMapping(value = "/{id}")
    public Result<CustomReason> selectById(@PathVariable("id") Long id) {
        return Result.succeed(customReasonService.getById(id));
    }

    @ApiOperation(value = "根据客户企业编号和原因类型获取列表")
    @GetMapping(value = "/list/{reasonType}")
    public Result<List<CustomReason>> listByType(@PathVariable("reasonType") Integer reasonType,
                                                 @CommonReqParam ReqParam reqParam) {
        return Result.succeed(customReasonService.selectByCorpAndType(reqParam.getCorpId(), reasonType));
    }

    @ApiOperation(value = "分页查询客户原因列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<CustomReasonDto>> query(@RequestBody CustomReasonFilter customReasonFilter,
                                                      @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(customReasonFilter.getCustomCorp())) {
            customReasonFilter.setCustomCorp(reqParam.getCorpId());
        }
        return Result.succeed(customReasonService.query(customReasonFilter));
    }

}
