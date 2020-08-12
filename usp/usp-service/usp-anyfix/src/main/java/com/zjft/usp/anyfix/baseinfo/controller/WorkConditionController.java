package com.zjft.usp.anyfix.baseinfo.controller;

import com.zjft.usp.anyfix.baseinfo.dto.WorkConditionDto;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;
import com.zjft.usp.anyfix.baseinfo.service.WorkConditionService;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.controller
 * @date 2019-09-26 18:17
 * @note
 */
@Api(tags = "工单流转条件")
@RestController
@RequestMapping("/work-condition")
public class WorkConditionController {

    @Autowired
    private WorkConditionService workConditionService;

    @ApiOperation(value = "工单流转条件列表")
    @PostMapping(value = "/list")
    public Result listWorkCondition(@RequestBody WorkConditionDto workConditionDto){
        List<WorkCondition> workConditionList =  this.workConditionService.list(workConditionDto);
        return Result.succeed(workConditionList);

    }

    @ApiOperation(value = "添加工单流转条件")
    @PostMapping(value = "/add")
    public Result addWorkCondition(@RequestBody WorkConditionDto workConditionDto, @LoginUser UserInfo userInfo){
        workConditionDto.setId(KeyUtil.getId());
        this.workConditionService.add(workConditionDto);
        return Result.succeed();
    }


    @PostMapping(value = "/{id}")
    public Result findById (@PathVariable("id") Long id) {
        WorkCondition workCondition = workConditionService.getById(id);
        return Result.succeed(workCondition);
    }

    @ApiOperation(value = "修改工单流转条件")
    @PostMapping(value = "/mod")
    public Result update (@RequestBody WorkConditionDto workConditionDto) {
        workConditionService.mod(workConditionDto);
        return Result.succeed();
    }


}
