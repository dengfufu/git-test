package com.zjft.usp.anyfix.corp.manage.controller;


import com.zjft.usp.anyfix.corp.manage.dto.DemanderContDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderContFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCont;
import com.zjft.usp.anyfix.corp.manage.service.DemanderContService;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zrlin
 * @since 2020-06-29
 */
@RestController
@RequestMapping("/demander-cont")
public class DemanderContController {

    @Resource
    private DemanderContService demanderContService;

    @ApiOperation("添加委托协议")
    @PostMapping(value = "/add")
    public Result add(@RequestBody DemanderContDto demanderContDto,
                                  @LoginUser UserInfo userInfo) {
        demanderContService.addDemanderCont(demanderContDto, userInfo,true);
        return Result.succeed();
    }

    @ApiOperation("更新委托协议")
    @PostMapping(value = "/update")
    public Result udpate(@RequestBody DemanderContDto demanderContDto,
                                  @LoginUser UserInfo userInfo) {
        demanderContService.addDemanderCont(demanderContDto, userInfo, false);
        return Result.succeed();
    }

    @ApiOperation("删除委托协议")
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id") Long id) {
        demanderContService.delete(id);
        return Result.succeed();
    }

    @ApiOperation("根据id获取委托协议")
    @GetMapping(value = "/{id}")
    public Result<DemanderContDto> findDetail(@PathVariable("id") Long id) {
        return Result.succeed(demanderContService.findDetail(id));
    }

    @ApiOperation("分页查询委托协议")
    @PostMapping(value = "/query")
    public Result query(@RequestBody DemanderContFilter filter) {
        return Result.succeed(demanderContService.query(filter));
    }

    @ApiOperation("查询服务标准信息")
    @PostMapping(value = "/service")
    public Result queryCont(@RequestBody DemanderContFilter filter) {
        return Result.succeed(demanderContService.queryCont(filter));
    }

    @ApiOperation("查询收费标准信息")
    @PostMapping(value = "/fee")
    public Result queryFee(@RequestBody DemanderContFilter filter) {
        return Result.succeed(demanderContService.queryCont(filter));
    }
}
