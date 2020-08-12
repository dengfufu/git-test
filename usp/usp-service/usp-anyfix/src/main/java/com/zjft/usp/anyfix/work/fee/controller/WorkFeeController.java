package com.zjft.usp.anyfix.work.fee.controller;


import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
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

/**
 * <p>
 * 工单费用表 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-01-06
 */
@Api(tags = "工单费用")
@RestController
@RequestMapping("/work-fee")
public class WorkFeeController {

    @Autowired
    private WorkFeeService workFeeService;

    @ApiOperation("分页查询")
    @PostMapping("/query")
    public Result<ListWrapper<WorkFeeDto>> query(@RequestBody WorkFilter workFilter, @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workFilter.getCorpId())) {
            workFilter.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.workFeeService.queryByWorkFilter(workFilter));
    }

    @ApiOperation("根据工单id查询")
    @GetMapping("/{workId}")
    public Result<WorkFeeDto> findById(@PathVariable("workId") Long workId) {
        return Result.succeed(workFeeService.getDtoById(workId));
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public Result update(@RequestBody WorkFeeDto workFeeDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.workFeeService.update(workFeeDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

}
