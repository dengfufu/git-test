package com.zjft.usp.anyfix.work.fee.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailExcelDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeVerifyDetailFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.ExcelUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 委托商对账单明细表 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/work-fee-verify-detail")
public class WorkFeeVerifyDetailController {

    @Autowired
    private WorkFeeVerifyDetailService workFeeVerifyDetailService;
    @Autowired
    private WorkFeeVerifyService workFeeVerifyService;

    @ApiOperation(value = "分页查询，一般用于根据对账单编号获取明细列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WorkFeeVerifyDetailDto>> query(@RequestBody WorkFeeVerifyDetailFilter workFeeVerifyDetailFilter) {
        return Result.succeed(this.workFeeVerifyDetailService.query(workFeeVerifyDetailFilter));
    }

    @ApiOperation(value = "导出对账单明细")
    @GetMapping(value = "/export")
    public void exportVerifyDetail(HttpServletResponse response,
                                   @RequestParam("verifyId") Long verifyId,
                                   @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(verifyId)) {
            throw new AppException("对账单编号不能为空");
        }
        WorkFeeVerify workFeeVerify = this.workFeeVerifyService.getById(verifyId);
        if (workFeeVerify == null) {
            throw new AppException("对账单不存在");
        }
        String fileName = workFeeVerify.getVerifyName();
        String sheetName = "工单记录";
        List<WorkFeeVerifyDetailExcelDto> detailExcelDtos = this.workFeeVerifyDetailService.listExcelDto(verifyId);
        try {
            ExcelUtil.writeExcel(response, detailExcelDtos, fileName, sheetName, WorkFeeVerifyDetailExcelDto.class, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
