package com.zjft.usp.anyfix.corp.user.controller;


import cn.hutool.core.lang.Assert;
import com.zjft.usp.anyfix.corp.user.dto.StaffSkillDto;
import com.zjft.usp.anyfix.corp.user.filter.StaffSkillFilter;
import com.zjft.usp.anyfix.corp.user.service.StaffSkillService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 工程师技能表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Api(tags = "工程师技能")
@RestController
@RequestMapping("/staff-skill")
public class StaffSkillController {

    @Autowired
    private StaffSkillService staffSkillService;

    @ApiOperation("分页查询工程师技能")
    @PostMapping(value = "/query")
    public Result<ListWrapper<StaffSkillDto>> pageByFilter(@RequestBody StaffSkillFilter staffSkillFilter,
                                                           @CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.staffSkillService.pageByFilter(staffSkillFilter, reqParam));
    }

    @ApiModelProperty("添加工程师技能")
    @PostMapping(value = "/add")
    public Result addStaffSkill(@RequestBody StaffSkillDto staffSkillDto,
                                @CommonReqParam ReqParam reqParam) {
        this.staffSkillService.addStaffSkill(staffSkillDto, reqParam);
        return Result.succeed();
    }

    @ApiOperation("修改工程师技能")
    @PostMapping(value = "/update")
    public Result updateStaffSkill(@RequestBody StaffSkillDto staffSkillDto) {
        this.staffSkillService.updateStaffSkill(staffSkillDto);
        return Result.succeed();
    }

    @ApiOperation(value = "删除工程师技能")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        Assert.notNull(id, "主键不能为空！");
        this.staffSkillService.removeById(id);
        return Result.succeed();
    }

}
