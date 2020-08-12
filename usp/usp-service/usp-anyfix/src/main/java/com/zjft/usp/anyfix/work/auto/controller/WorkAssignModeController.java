package com.zjft.usp.anyfix.work.auto.controller;


import com.zjft.usp.anyfix.work.auto.dto.WorkAssignModeDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkAssignModeFilter;
import com.zjft.usp.anyfix.work.auto.service.WorkAssignModeService;
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
 * 派单模式配置 前端控制器
 * </p>
 *
 * @author zphu
 * @since 2019-09-26
 */
@Api(tags = "自动派单模式")
@RestController
@RequestMapping("/work-assign-mode")
public class WorkAssignModeController {

    @Autowired
    private WorkAssignModeService workAssignModeService;

    @ApiOperation(value = "分页查询自动派单模式列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WorkAssignModeDto>> queryWorkAssignMode(@RequestBody WorkAssignModeFilter filter,
                                                                      @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(filter.getServiceCorp())) {
            filter.setServiceCorp(reqParam.getCorpId());
        }
        ListWrapper<WorkAssignModeDto> list = this.workAssignModeService.query(filter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "添加自动派单模式")
    @PostMapping(value = "/add")
    public Result addWorkAssignMode(@RequestBody WorkAssignModeDto workAssignModeDto, @LoginUser UserInfo userInfo) {
        this.workAssignModeService.add(workAssignModeDto);
        return Result.succeed();
    }

    @ApiOperation(value = "修改自动派单模式")
    @PostMapping(value = "/update")
    public Result modWorkAssignMode(@RequestBody WorkAssignModeDto workAssignModeDto, @LoginUser UserInfo userInfo) {
        this.workAssignModeService.mod(workAssignModeDto);
        return Result.succeed();
    }

    @ApiOperation(value = "删除自动派单模式")
    @DeleteMapping(value = "/{id}")
    public Result delWorkAssignMode(@PathVariable("id") Long id, @LoginUser UserInfo userInfo) {
        this.workAssignModeService.delById(id);
        return Result.succeed();
    }
}
