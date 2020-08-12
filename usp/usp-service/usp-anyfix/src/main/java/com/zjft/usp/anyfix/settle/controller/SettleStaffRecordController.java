package com.zjft.usp.anyfix.settle.controller;

import com.zjft.usp.anyfix.settle.dto.SettleStaffRecordDto;
import com.zjft.usp.anyfix.settle.filter.SettleStaffRecordFilter;
import com.zjft.usp.anyfix.settle.service.SettleStaffRecordService;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 员工结算记录 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Api(tags = "员工结算记录")
@RestController
@RequestMapping("/settle-staff-record")
public class SettleStaffRecordController {

    @Autowired
    private SettleStaffRecordService settleStaffRecordService;

    @ApiOperation(value = "分页查询人员结算记录")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SettleStaffRecordDto>> pageByFilter(@RequestBody SettleStaffRecordFilter settleStaffRecordFilter){
        return Result.succeed(this.settleStaffRecordService.pageByFilter(settleStaffRecordFilter));
    }

    @ApiOperation(value = "添加人员结算记录")
    @PostMapping(value = "/add")
    public Result add(@RequestBody SettleStaffRecordDto settleStaffRecordDto, @LoginUser UserInfo userInfo){
        this.settleStaffRecordService.add(settleStaffRecordDto, userInfo.getUserId());
        return Result.succeed("添加成功！");
    }

    @ApiOperation(value = "根据结算记录编号删除")
    @DeleteMapping(value = "/{recordId}")
    public Result delete(@PathVariable("recordId") Long recordId){
        Assert.notNull(recordId, "参数解析错误！");
        this.settleStaffRecordService.deleteById(recordId);
        return Result.succeed("删除成功！");
    }

}
