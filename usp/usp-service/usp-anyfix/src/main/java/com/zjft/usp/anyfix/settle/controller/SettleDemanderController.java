package com.zjft.usp.anyfix.settle.controller;


import com.zjft.usp.anyfix.settle.dto.BankAccountDto;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderDto;
import com.zjft.usp.anyfix.settle.filter.SettleDemanderFilter;
import com.zjft.usp.anyfix.settle.service.SettleDemanderService;
import com.zjft.usp.anyfix.utils.PdfUtil;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 委托商结算单 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
@Api(tags = "委托商结算单")
@RestController
@RequestMapping("/settle-demander")
public class SettleDemanderController {

    @Autowired
    private SettleDemanderService settleDemanderService;

    @ApiOperation("分页查询")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SettleDemanderDto>> query(@RequestBody SettleDemanderFilter settleDemanderFilter,
                                                        @LoginUser UserInfo userInfo,
                                                        @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(settleDemanderFilter.getCurrentCorp())) {
            settleDemanderFilter.setCurrentCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.settleDemanderService.query(settleDemanderFilter, userInfo.getUserId(), reqParam.getCorpId()));
    }

    @ApiOperation("添加")
    @PostMapping(value = "/add")
    public Result add(@RequestBody SettleDemanderDto settleDemanderDto, @CommonReqParam ReqParam reqParam, @LoginUser UserInfo userInfo) {
        if (LongUtil.isZero(settleDemanderDto.getServiceCorp())) {
            settleDemanderDto.setServiceCorp(reqParam.getCorpId());
        }
        this.settleDemanderService.add(settleDemanderDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "生成合同号")
    @PostMapping(value = "/generate/contNo")
    public Result<String> generateSettleCode(@RequestBody SettleDemanderDto settleDemanderDto, @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(settleDemanderDto.getServiceCorp())) {
            settleDemanderDto.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeedStr(this.settleDemanderService.generateSettleCode(settleDemanderDto));
    }

    @ApiOperation(value = "修改结算单")
    @PostMapping(value = "/update")
    public Result update(@RequestBody SettleDemanderDto settleDemanderDto,
                         @LoginUser UserInfo userInfo,
                         @CommonReqParam ReqParam reqParam) {
        this.settleDemanderService.update(settleDemanderDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation("根据id查询结算单")
    @GetMapping(value = "/{settleId}")
    public Result<SettleDemanderDto> findById(@PathVariable("settleId") Long settleId) {
        return Result.succeed(settleDemanderService.findById(settleId));
    }

    @ApiOperation("确认结算单")
    @PostMapping(value = "/check")
    public Result check(@RequestBody SettleDemanderDto settleDemanderDto, @LoginUser UserInfo userInfo) {
        this.settleDemanderService.check(settleDemanderDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("删除")
    @DeleteMapping(value = "/{settleId}")
    public Result delete(@PathVariable("settleId") Long settleId) {
        this.settleDemanderService.delete(settleId);
        return Result.succeed();
    }

    @ApiOperation(value = "获取收款账户历史数据")
    @PostMapping(value = "/list/account")
    public Result<ListWrapper<BankAccountDto>> listBankAccount(
            @RequestBody SettleDemanderFilter settleDemanderFilter,
            @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(settleDemanderFilter.getServiceCorp())) {
            settleDemanderFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(this.settleDemanderService.listBankAccount(settleDemanderFilter));
    }

    @ApiOperation(value = "导出PDF")
    @GetMapping(value = "/exportPDF/{settleId}")
    public void exportPdf(HttpServletResponse response,
                          @PathVariable("settleId") Long settleId,
                          @LoginUser UserInfo userInfo,
                          @CommonReqParam ReqParam reqParam) {
        SettleDemanderDto settleDemanderDto = settleDemanderService.findById(settleId);
        try {
            PdfUtil p = new PdfUtil();
            p.createSettleDemanderPdf(response, settleDemanderDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
