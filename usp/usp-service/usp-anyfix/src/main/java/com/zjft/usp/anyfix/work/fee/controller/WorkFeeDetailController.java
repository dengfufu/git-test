package com.zjft.usp.anyfix.work.fee.controller;

import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDetailDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeDetailFilter;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeDetailService;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 工单费用明细 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
@RestController
@RequestMapping("/work-fee-detail")
public class WorkFeeDetailController {

    @Autowired
    private WorkFeeDetailService workFeeDetailService;

    @ApiOperation(value = "查询")
    @PostMapping(value = "/list")
    public Result<List<WorkFeeDetailDto>> list(@RequestBody WorkFeeDetailFilter workFeeDetailFilter) {
        return Result.succeed(this.workFeeDetailService.listByFilter(workFeeDetailFilter));
    }

}
