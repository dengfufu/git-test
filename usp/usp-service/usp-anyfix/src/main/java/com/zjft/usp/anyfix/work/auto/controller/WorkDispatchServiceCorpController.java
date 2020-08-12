package com.zjft.usp.anyfix.work.auto.controller;

import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceCorpDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkDispatchServiceCorpFilter;
import com.zjft.usp.anyfix.work.auto.service.WorkDispatchServiceCorpService;
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
 * @author ljzhu
 * @date 2019-09-26 18:17
 * @note
 */
@Api(tags = "工单自动提交服务商")
@RestController
@RequestMapping("/work-dispatch-service-corp")
public class WorkDispatchServiceCorpController {

    @Autowired
    private WorkDispatchServiceCorpService workDispatchServiceCorpService;

    @ApiOperation(value = "分页查询工单自动提交服务商规则列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WorkDispatchServiceCorpDto>> listWorkDispatchServiceCorp(@RequestBody WorkDispatchServiceCorpFilter WorkDispatchServiceCorpFilter,
                                                                                       @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(WorkDispatchServiceCorpFilter.getDemanderCorp())) {
            WorkDispatchServiceCorpFilter.setDemanderCorp(reqParam.getCorpId());
        }
        ListWrapper<WorkDispatchServiceCorpDto> list = workDispatchServiceCorpService.query(WorkDispatchServiceCorpFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "添加工单自动提交服务商规则")
    @PostMapping(value = "/add")
    public Result addWorkDispatchServiceCorp(@RequestBody WorkDispatchServiceCorpDto workDispatchServiceCorpDto) {
        this.workDispatchServiceCorpService.add(workDispatchServiceCorpDto);
        return Result.succeed();
    }

    @ApiOperation(value = "修改工单自动提交服务商规则")
    @PostMapping(value = "/update")
    public Result modWorkDispatchServiceCorp(@RequestBody WorkDispatchServiceCorpDto workDispatchServiceCorpDto) {
        this.workDispatchServiceCorpService.mod(workDispatchServiceCorpDto);
        return Result.succeed();
    }

    @ApiOperation(value = "删除工单自动提交服务商规则")
    @DeleteMapping(value = "/{id}")
    public Result delWorkDispatchServiceCorp(@PathVariable("id") Long id) {
        this.workDispatchServiceCorpService.delById(id);
        return Result.succeed();
    }
}
