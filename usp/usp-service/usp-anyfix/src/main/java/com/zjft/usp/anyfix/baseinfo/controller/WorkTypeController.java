package com.zjft.usp.anyfix.baseinfo.controller;

import com.zjft.usp.anyfix.baseinfo.dto.WorkTypeDto;
import com.zjft.usp.anyfix.baseinfo.filter.WorkTypeFilter;
import com.zjft.usp.anyfix.baseinfo.model.WorkType;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
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
 * 工单类型表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "工单类型")
@RestController
@RequestMapping("/work-type")
public class WorkTypeController {

    @Autowired
    private WorkTypeService workTypeService;

    @ApiOperation(value = "添加工单类型")
    @PostMapping(value = "/add")
    public Result addWorkType(@RequestBody WorkType workType,
                              @LoginUser UserInfo userInfo,
                              @CommonReqParam ReqParam reqParam) {
        workTypeService.save(workType,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改工单类型")
    @PostMapping(value = "/update")
    public Result updateWorkType(@RequestBody WorkType workType,
                                 @LoginUser UserInfo userInfo,
                                 @CommonReqParam ReqParam reqParam) {
        workTypeService.update(workType,userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除工单类型")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        workTypeService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个工单类型")
    @GetMapping(value = "/{id}")
    public Result selectById(@PathVariable("id") Long id) {
        return Result.succeed(workTypeService.getById(id));
    }

    @ApiOperation(value = "获得工单类型列表")
    @PostMapping(value = "/list")
    public Result<List<WorkType>> listWorkType(@RequestBody WorkTypeFilter workTypeFilter,
                                               @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workTypeFilter.getDemanderCorp())) {
            workTypeFilter.setDemanderCorp(reqParam.getCorpId());
        }
        List<WorkType> list = workTypeService.listWorkType(workTypeFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "分页查询工单类型列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WorkTypeDto>> query(@RequestBody WorkTypeFilter workTypeFilter,
                                                  @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workTypeFilter.getCorpId())) {
            workTypeFilter.setCorpId(reqParam.getCorpId());
        }
        ListWrapper<WorkTypeDto> listWrapper = this.workTypeService.query(workTypeFilter);
        return Result.succeed(listWrapper);
    }

}
