package com.zjft.usp.wms.business.income.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.income.composite.IncomeCompoService;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 入库基本信息共用表 前端控制器
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-11 09:39
 **/
@RestController
@RequestMapping("/income-common")
public class IncomeWareCommonController {
    @Autowired
    IncomeCompoService incomeCompoService;

    /**
     * 保存入库申请
     *
     * @param incomeWareCommonDto
     * @return com.zjft.usp.common.model.Result<java.lang.Object>
     * @author Qiugm
     * @date 2019-11-11
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<Object> save(@RequestBody IncomeWareCommonDto incomeWareCommonDto,
                               @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        incomeCompoService.save(incomeWareCommonDto, userInfo, reqParam);
        return Result.succeed();
    }

    /**
     * 修改入库申请
     *
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.Result<java.lang.Object>
     * @author Qiugm
     * @date 2019-11-11
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<Object> update(@RequestBody IncomeWareCommonDto incomeWareCommonDto,
                                 @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        incomeCompoService.update(incomeWareCommonDto, userInfo, reqParam);
        return Result.succeed();
    }

    /**
     * 删除入库申请
     *
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.Result<java.lang.Object>
     * @author Qiugm
     * @date 2019-11-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result<Object> delete(@RequestBody IncomeWareCommonDto incomeWareCommonDto,
                                 @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        incomeCompoService.delete(incomeWareCommonDto, userInfo, reqParam);
        return Result.succeed();
    }

    /**
     * 入库申请提交
     *
     * @param incomeWareCommonDto
     * @return com.zjft.usp.common.model.Result<java.lang.Object>
     * @author Qiugm
     * @date 2019-11-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<Object> add(@RequestBody IncomeWareCommonDto incomeWareCommonDto,
                              @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        incomeCompoService.add(incomeWareCommonDto, userInfo, reqParam);
        return Result.succeed();
    }

    /**
     * 审核入库申请
     *
     * @param incomeWareCommonDto
     * @return com.zjft.usp.common.model.Result<java.lang.Object>
     * @author Qiugm
     * @date 2019-11-11
     */
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public Result<Object> audit(@RequestBody IncomeWareCommonDto incomeWareCommonDto,
                                @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        incomeCompoService.audit(incomeWareCommonDto, userInfo, reqParam);
        return Result.succeed();
    }

    /**
     * 审核入库申请
     *
     * @param incomeWareCommonDto
     * @return com.zjft.usp.common.model.Result<java.lang.Object>
     * @author Qiugm
     * @date 2019-11-11
     */
    @RequestMapping(value = "/batchAudit", method = RequestMethod.POST)
    public Result<Object> batchAudit(@RequestBody IncomeWareCommonDto incomeWareCommonDto,
                                     @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        incomeCompoService.batchAudit(incomeWareCommonDto, userInfo, reqParam);
        return Result.succeed();
    }

    /**
     * 查看申请详情
     *
     * @param applyId
     * @return com.zjft.usp.common.model.Result<com.zjft.usp.wms.business.income.dto.IncomeMainCommonDto>
     * @author Qiugm
     * @date 2019-11-11
     */
    @RequestMapping(value = "/detail/{applyId}", method = RequestMethod.POST)
    public Result<IncomeWareCommonDto> viewIncome(@PathVariable("applyId") Long applyId, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(incomeCompoService.viewIncome(applyId, reqParam));
    }

    /**
     * 查询入库申请记录
     *
     * @param incomeFilter
     * @return com.zjft.usp.common.model.Result<com.zjft.usp.common.model.ListWrapper < com.zjft.usp.wms.business.income.dto.IncomeMainCommonDto>>
     * @author Qiugm
     * @date 2019-11-11
     */
    @RequestMapping(value = "/listIncome", method = RequestMethod.POST)
    public Result<ListWrapper<IncomeWareCommonDto>> listIncome(@RequestBody IncomeFilter incomeFilter) {
        return Result.succeed(incomeCompoService.listIncome(incomeFilter));
    }

    /**
     * 查询入库申请记录
     *
     * @param incomeFilter
     * @return com.zjft.usp.common.model.Result<com.zjft.usp.common.model.ListWrapper < com.zjft.usp.wms.business.income.dto.IncomeMainCommonDto>>
     * @author Qiugm
     * @date 2019-11-11
     */
    @RequestMapping(value = "/listSaveIncome", method = RequestMethod.POST)
    public Result<ListWrapper<IncomeWareCommonDto>> listSaveIncome(@RequestBody IncomeFilter incomeFilter) {
        return Result.succeed(incomeCompoService.listSaveIncome(incomeFilter));
    }

    @ApiOperation(value = "统计入库单数量")
    @PostMapping(value = "/incomeStatus/count")
    public Result<Map<Integer, Long>> countByFilter(@RequestBody IncomeFilter incomeFilter,
                                                    @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.incomeCompoService.countByIncomeStatus(incomeFilter, userInfo, reqParam));
    }

}