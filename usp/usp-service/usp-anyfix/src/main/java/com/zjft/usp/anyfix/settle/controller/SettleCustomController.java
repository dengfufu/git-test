package com.zjft.usp.anyfix.settle.controller;

import com.zjft.usp.anyfix.settle.dto.SettleCustomDto;
import com.zjft.usp.anyfix.settle.filter.SettleCustomFilter;
import com.zjft.usp.anyfix.settle.service.SettleCustomService;
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
 * 客户结算单 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Api(tags = "客户结算")
@RestController
@RequestMapping("/settle-custom")
public class SettleCustomController {

    @Autowired
    private SettleCustomService settleCustomService;

    @ApiOperation(value = "添加客户结算单")
    @PostMapping(value = "/add")
    public Result addRecord(@RequestBody SettleCustomDto settleCustomDto, @LoginUser UserInfo userInfo){
        return this.settleCustomService.addRecord(settleCustomDto, userInfo.getUserId());
    }

    @ApiOperation(value = "查询客户结算单")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SettleCustomDto>> pageByFilter(@RequestBody SettleCustomFilter settleCustomFilter){
        ListWrapper<SettleCustomDto> list = this.settleCustomService.pageByFilter(settleCustomFilter);
        return Result.succeed(list);
    }

    @ApiOperation(value = "删除客户结算单")
    @DeleteMapping(value = "/{settleId}")
    public Result delete(@PathVariable("settleId") Long settleId){
        Assert.notNull(settleId, "主键不能为空！");
        this.settleCustomService.deleteById(settleId);
        return Result.succeed("删除成功！");
    }

}
