package com.zjft.usp.wms.business.outcome.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;
import com.zjft.usp.wms.business.outcome.composite.OutcomeCompoService;
import com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 出库信息共用表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
@Api(tags = "出库申请信息请求")
@RestController
@RequestMapping("/outcome-common")
public class OutcomeWareCommonController {

    @Autowired
    private OutcomeCompoService outcomeCompoService;

    @ApiOperation(value = "出库申请提交")
    @PostMapping(value = "/add")
    public Result<Object> add(@RequestBody OutcomeWareCommonDto outcomeWareCommonDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.outcomeCompoService.add(outcomeWareCommonDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "出库申请删除")
    @PostMapping(value = "/delete")
    public Result<Object> delete(@RequestBody OutcomeWareCommonDto outcomeWareCommonDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.outcomeCompoService.delete(outcomeWareCommonDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "出库申请修改（单个记录）")
    @PostMapping(value = "/update")
    public Result<Object> update(@RequestBody OutcomeWareCommonDto outcomeWareCommonDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.outcomeCompoService.update(outcomeWareCommonDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "出库申请批量修改（申请关联的所有记录即申请基本信息修改）")
    @PostMapping(value = "/batchUpdate")
    public Result<Object> batchUpdate(@RequestBody OutcomeWareCommonDto outcomeWareCommonDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.outcomeCompoService.batchUpdate(outcomeWareCommonDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "分页查询出库列表")
    @PostMapping(value = "/list")
    public Result<ListWrapper<OutcomeWareCommonDto>> list(@RequestBody OutcomeFilter outcomeFilter, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        outcomeFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(this.outcomeCompoService.list(outcomeFilter,userInfo,reqParam));
    }

    @ApiOperation(value = "分页查询出库列表")
    @GetMapping(value = "/detail/{outcomeId}")
    public Result<OutcomeWareCommonDto> detail(@PathVariable("outcomeId") Long outcomeId, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.outcomeCompoService.detail(outcomeId,userInfo,reqParam));
    }

    @ApiOperation(value = "审核出库列表")
    @PostMapping(value = "/audit")
    public Result<Object> audit(@RequestBody  OutcomeWareCommonDto outcomeWareCommonDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.outcomeCompoService.audit(outcomeWareCommonDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "批量审核出库列表")
    @PostMapping(value = "/batchAudit")
    public Result<Object> batchAudit(@RequestBody OutcomeWareCommonDto outcomeWareCommonDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.outcomeCompoService.batchAudit(outcomeWareCommonDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "出库申请暂存")
    @PostMapping(value = "/save")
    public Result<Object> save(@RequestBody OutcomeWareCommonDto outcomeWareCommonDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.outcomeCompoService.save(outcomeWareCommonDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "分页查询出库申请保存列表")
    @PostMapping(value = "/save/list")
    public Result<ListWrapper<OutcomeMainCommonSaveDto>> listSave(@RequestBody OutcomeFilter outcomeFilter, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        outcomeFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(this.outcomeCompoService.listSave(outcomeFilter,userInfo,reqParam));
    }

    @ApiOperation(value = "分页查询出库列表")
    @GetMapping(value = "/save/detail/{outcomeId}")
    public Result<OutcomeMainCommonSaveDto> detailSave(@PathVariable("outcomeId") Long outcomeId, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.outcomeCompoService.detailSave(outcomeId,userInfo,reqParam));
    }

    @ApiOperation(value = "更新暂存的出库申请")
    @PostMapping(value = "/save/update")
    public Result<Object> updateSave(@RequestBody OutcomeWareCommonDto outcomeWareCommonDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.outcomeCompoService.updateSave(outcomeWareCommonDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "删除暂存的出库申请")
    @GetMapping(value = "/save/delete/{outcomeId}")
    public Result<Object> deleteSave(@PathVariable("outcomeId") Long outcomeId) {
        this.outcomeCompoService.deleteSave(outcomeId);
        return Result.succeed();
    }

    @ApiOperation(value = "统计入库单数量")
    @PostMapping(value = "/outcomeStatus/count")
    public Result<Map<Integer, Long>> countByFilter(@RequestBody OutcomeFilter outcomeFilter,
                                                    @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        outcomeFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(this.outcomeCompoService.countByOutcomeStatus(outcomeFilter, userInfo, reqParam));
    }
}
