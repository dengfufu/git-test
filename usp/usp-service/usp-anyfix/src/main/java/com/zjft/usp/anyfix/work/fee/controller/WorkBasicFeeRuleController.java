package com.zjft.usp.anyfix.work.fee.controller;


import com.zjft.usp.anyfix.work.fee.dto.WorkBasicFeeRuleDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkBasicFeeRuleFilter;
import com.zjft.usp.anyfix.work.fee.service.WorkBasicFeeRuleService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 工单基础服务费规则 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
@Api(tags = "工单基础服务费规则")
@RestController
@RequestMapping("/work-basic-fee-rule")
public class WorkBasicFeeRuleController {

    @Autowired
    private WorkBasicFeeRuleService workBasicFeeRuleService;

    @ApiOperation("分页查询")
    @PostMapping("/query")
    public Result<ListWrapper<WorkBasicFeeRuleDto>> query(@RequestBody WorkBasicFeeRuleFilter workBasicFeeRuleFilter,
                                                          @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workBasicFeeRuleFilter.getServiceCorp())) {
            workBasicFeeRuleFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(workBasicFeeRuleService.query(workBasicFeeRuleFilter));
    }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Result add(@RequestBody WorkBasicFeeRuleDto workBasicFeeRuleDto, @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workBasicFeeRuleDto.getServiceCorp())) {
            workBasicFeeRuleDto.setServiceCorp(reqParam.getCorpId());
        }
        workBasicFeeRuleService.add(workBasicFeeRuleDto);
        return Result.succeed();
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public Result update(@RequestBody WorkBasicFeeRuleDto workBasicFeeRuleDto, @CommonReqParam ReqParam reqParam) {
        workBasicFeeRuleService.update(workBasicFeeRuleDto);
        return Result.succeed();
    }

    @ApiOperation("删除")
    @DeleteMapping("/{ruleId}")
    public Result delete(@PathVariable("ruleId") Long ruleId) {
        this.workBasicFeeRuleService.deleteById(ruleId);
        return Result.succeed();
    }

}
