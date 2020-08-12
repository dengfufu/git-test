package com.zjft.usp.wms.business.trans.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.trans.composite.TransCompoService;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.enums.TransStatusEnum;
import com.zjft.usp.wms.business.trans.filter.TransFilter;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 调拨信息共用表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
@RestController
@RequestMapping("/trans-ware-common")
public class TransWareCommonController {
    @Resource
    private TransCompoService transCompoService;

    @ApiOperation(value = "保存申请单")
    @PostMapping(value = "/save")
    public Result save(@RequestBody TransWareCommonDto transWareCommonDto, @LoginUser UserInfo userInfo
            , @CommonReqParam ReqParam reqParam) {
        transCompoService.save(transWareCommonDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "添加申请单")
    @PostMapping(value = "/add")
    public Result add(@RequestBody TransWareCommonDto transWareCommonDto,
                      @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        transWareCommonDto.setCreateBy(userInfo.getUserId());
        transCompoService.add(transWareCommonDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "审批申请单")
    @PostMapping(value = "/audit")
    public Result audit(@RequestBody TransWareCommonDto transWareCommonDto, @LoginUser UserInfo userInfo,
                        @CommonReqParam ReqParam reqParam) {
        transCompoService.audit(transWareCommonDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "分页查询已提交调拨单")
    @PostMapping(value = "/queryTrans")
    public Result<ListWrapper<TransWareCommonDto>> query(@RequestBody TransFilter transFilter, @CommonReqParam ReqParam reqParam) {
        transFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(transCompoService.queryTrans(transFilter));
    }

    @ApiOperation(value = "查询调拨单详情")
    @GetMapping(value = "/detail/{transId}")
    public Result<TransWareCommonDto> queryDetail(@PathVariable("transId") Long transId,@CommonReqParam ReqParam reqParam) {
        return Result.succeed(transCompoService.queryDetail(transId,reqParam));
    }

    @ApiOperation(value = "分页查询已保存未提交的调拨单")
    @PostMapping(value = "/querySavedTrans")
    public Result<ListWrapper<TransWareCommonDto>> querySavedTrans(@RequestBody TransFilter transFilter,
                                                                   @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        transFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(transCompoService.querySavedTrans(transFilter, userInfo, reqParam));
    }

    @ApiOperation(value = "获得单个暂存单")
    @GetMapping(value = "/{id}")
    public Result<TransWareCommonDto> selectById(@PathVariable("id") Long id, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(transCompoService.viewTrans(id, reqParam));
    }

    @ApiOperation(value = "批量审批申请单")
    @PostMapping(value = "/batchAudit")
    public Result batchAudit(@RequestBody TransWareCommonDto transWareCommonDto, @LoginUser UserInfo userInfo,
                        @CommonReqParam ReqParam reqParam) {
        transCompoService.batchAudit(transWareCommonDto, userInfo, reqParam, TransStatusEnum.FOR_ALLOCATION.getCode());
        return Result.succeed();
    }


    @ApiOperation(value = "统计调拨各状态总数")
    @PostMapping(value = "/countByWareStatus")
    public Result countByWareStatus(@RequestBody TransFilter transFilter, @LoginUser UserInfo userInfo,
                             @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.transCompoService.countByWareStatus(transFilter,userInfo,reqParam));
    }


}
