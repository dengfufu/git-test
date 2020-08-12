package com.zjft.usp.anyfix.settle.controller;


import com.zjft.usp.anyfix.settle.dto.SettleStaffDto;
import com.zjft.usp.anyfix.settle.filter.SettleStaffFilter;
import com.zjft.usp.anyfix.settle.service.SettleStaffService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 员工结算单 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Api(tags = "员工结算单")
@RestController
@RequestMapping("/settle-staff")
public class SettleStaffController {

    @Autowired
    private SettleStaffService settleStaffService;

    @ApiOperation(value = "分页查询结算单列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SettleStaffDto>> pageByFilter(@RequestBody SettleStaffFilter settleStaffFilter, @CommonReqParam ReqParam reqParam){
        return Result.succeed(this.settleStaffService.pageByFilter(settleStaffFilter, reqParam));
    }

    @ApiOperation(value = "删除结算记录")
    @DeleteMapping(value = "/{settleId}")
    public Result delete(@PathVariable("settleId") Long settleId){
        Assert.notNull(settleId, "主键不能为空！");
        this.settleStaffService.deleteById(settleId);
        return Result.succeed("删除成功！");
    }

}
