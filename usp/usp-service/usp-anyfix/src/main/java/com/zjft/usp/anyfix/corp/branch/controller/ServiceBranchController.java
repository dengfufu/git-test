package com.zjft.usp.anyfix.corp.branch.controller;


import cn.hutool.core.lang.Assert;
import com.zjft.usp.anyfix.corp.branch.composite.ServiceBranchCompoService;
import com.zjft.usp.anyfix.corp.branch.dto.ServiceBranchDto;
import com.zjft.usp.anyfix.corp.branch.dto.ServiceBranchTreeDto;
import com.zjft.usp.anyfix.corp.branch.filter.ServiceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
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
import java.util.Map;

/**
 * <p>
 * 服务网点表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Api(tags = "服务网点")
@RestController
@RequestMapping("/service-branch")
public class ServiceBranchController {

    @Autowired
    private ServiceBranchService serviceBranchService;
    @Autowired
    private ServiceBranchCompoService serviceBranchCompoService;

    @ApiOperation("分页查询服务网点")
    @PostMapping(value = "/query")
    public Result<ListWrapper<ServiceBranchDto>> query(@RequestBody ServiceBranchFilter serviceBranchFilter,
                                                       @LoginUser UserInfo userInfo,
                                                       @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchFilter.getServiceCorp())) {
            serviceBranchFilter.setServiceCorp(reqParam.getCorpId());
        }
        ListWrapper<ServiceBranchDto> listWrapper = this.serviceBranchService.query(serviceBranchFilter, userInfo);
        return Result.succeed(listWrapper);
    }

    @ApiOperation("查看服务网点信息")
    @GetMapping(value = "/{branchId}")
    public Result<ServiceBranchDto> findByBranchId(@PathVariable("branchId") Long branchId) {
        return Result.succeed(this.serviceBranchService.findByBranchId(branchId));
    }

    @ApiOperation("添加服务网点")
    @PostMapping(value = "/add")
    public Result add(@RequestBody ServiceBranchDto serviceBranchDto,
                      @LoginUser UserInfo userInfo,
                      @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchDto.getServiceCorp())) {
            serviceBranchDto.setServiceCorp(reqParam.getCorpId());
        }
        serviceBranchCompoService.addServiceBranch(serviceBranchDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation("修改服务网点")
    @PostMapping(value = "/update")
    public Result update(@RequestBody ServiceBranchDto serviceBranchDto,
                         @LoginUser UserInfo userInfo) {
        serviceBranchCompoService.updateServiceBranch(serviceBranchDto, userInfo);
        return Result.succeed();
    }

    @ApiOperation("删除服务网点")
    @DeleteMapping(value = "/{branchId}")
    public Result delete(@PathVariable("branchId") Long branchId) {
        serviceBranchCompoService.delServiceBranch(branchId);
        return Result.succeed();
    }

    @ApiOperation("服务网点列表")
    @PostMapping(value = "/list")
    public Result<List<ServiceBranch>> listServiceBranch(@RequestBody ServiceBranchFilter serviceBranchFilter,
                                                         @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchFilter.getServiceCorp())) {
            serviceBranchFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(serviceBranchService.listServiceBranch(serviceBranchFilter));
    }

    @ApiOperation("服务网点树")
    @PostMapping(value = "/tree")
    public Result<List<ServiceBranchTreeDto>> listServiceBranchTree(@RequestBody ServiceBranchFilter serviceBranchFilter,
                                                                    @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchFilter.getServiceCorp())) {
            serviceBranchFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(serviceBranchCompoService.listServiceBranchTree(serviceBranchFilter));
    }

    @ApiOperation("上级服务网点树")
    @PostMapping(value = "/upper/tree")
    public Result<List<ServiceBranchTreeDto>> listAllUpperServiceBranchTree(@RequestBody ServiceBranchFilter serviceBranchFilter) {
        return Result.succeed(serviceBranchCompoService.listAllUpperServiceBranchTree(serviceBranchFilter));
    }

    @ApiOperation("上级服务网点列表")
    @PostMapping(value = "/upper/list")
    public Result<List<ServiceBranch>> listAllUpperServiceBranch(@RequestBody ServiceBranchFilter serviceBranchFilter) {
        return Result.succeed(serviceBranchService.listAllUpperServiceBranch(serviceBranchFilter.getBranchId()));
    }

    @ApiOperation("分页查询下级服务网点")
    @PostMapping(value = "/lower/query")
    public Result<ListWrapper<ServiceBranchDto>> queryAllLowerServiceBranch(@RequestBody ServiceBranchFilter serviceBranchFilter) {
        return Result.succeed(serviceBranchService.queryAllLowerServiceBranch(serviceBranchFilter));
    }

    @ApiOperation("分页查询同级服务网点")
    @PostMapping(value = "/same/query")
    public Result<ListWrapper<ServiceBranchDto>> querySameServiceBranch(@RequestBody ServiceBranchFilter serviceBranchFilter,
                                                                        @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchFilter.getServiceCorp())) {
            serviceBranchFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(serviceBranchService.querySameServiceBranch(serviceBranchFilter));
    }

    @ApiOperation("根据服务商客服编号查询网点列表")
    @GetMapping(value = "/list/service/{userId}")
    public Result<List<ServiceBranch>> listBranchByServiceUserId(@PathVariable("userId") Long userId) {
        return Result.succeed(serviceBranchService.listServiceBranchByServiceUserId(userId));
    }

    @ApiOperation("获得当前用户的服务网点列表")
    @GetMapping(value = "/own-branch/list")
    public Result<List<ServiceBranch>> listServiceBranchByUserId(@LoginUser UserInfo userInfo,
                                                                 @CommonReqParam ReqParam reqParam) {
        return Result.succeed(serviceBranchCompoService.listOwnBranch(userInfo.getUserId(), reqParam.getCorpId()));
    }

    @ApiOperation("模糊查询服务网点")
    @PostMapping(value = "/match")
    public Result<List<ServiceBranch>> matchServiceBranch(@RequestBody ServiceBranchFilter serviceBranchFilter,
                                                          @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceBranchFilter.getServiceCorp())) {
            serviceBranchFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(serviceBranchService.matchServiceBranch(serviceBranchFilter));
    }

    @ApiOperation("远程调用：根据服务网点编号查询")
    @GetMapping(value = "/feign/{branchId}")
    public Result getById(@PathVariable("branchId") Long branchId) {
        Assert.notNull(branchId, "网点编号不能为空！");
        return Result.succeed(this.serviceBranchService.findByBranchId(branchId));
    }

    @ApiOperation("远程调用：获得人员所在服务网点编号列表")
    @GetMapping(value = "/feign/own/list/{userId}/{corpId}")
    public Result<List<Long>> listOwnBranchId(@PathVariable("userId") Long userId,
                                              @PathVariable("corpId") Long corpId) {
        return Result.succeed(serviceBranchCompoService.listOwnBranchId(userId, corpId));
    }

    @ApiOperation("远程调用：获得人员所在服务网点的下级网点编号列表")
    @GetMapping(value = "/feign/lower/list/{userId}/{corpId}")
    public Result<List<Long>> listOwnLowerBranchId(@PathVariable("userId") Long userId,
                                                   @PathVariable("corpId") Long corpId) {
        return Result.succeed(serviceBranchCompoService.listOwnLowerBranchId(userId, corpId));
    }

    @ApiOperation("远程调用：获得人员所在服务网点及下级网点编号列表")
    @GetMapping(value = "/feign/own-lower/list/{userId}/{corpId}")
    public Result<List<Long>> listOwnAndLowerBranchId(@PathVariable("userId") Long userId,
                                                      @PathVariable("corpId") Long corpId) {
        return Result.succeed(serviceBranchCompoService.listOwnAndLowerBranchId(userId, corpId));
    }

    @ApiOperation("远程调用：获得下级网点编号列表")
    @PostMapping(value = "/feign/lower/list")
    public Result<List<Long>> listLowerBranchId(@RequestBody List<Long> branchIdList) {
        return Result.succeed(serviceBranchCompoService.listLowerBranchId(branchIdList));
    }

    @ApiOperation(value = "远程调用：查询网点编号与网点名称")
    @PostMapping("/feign/mapIdAndName")
    public Result<Map<Long, String>> mapIdAndName(@RequestBody List<Long> branchIdList) {
        Map<Long, String> map = this.serviceBranchService.mapIdAndNameByBranchIdList(branchIdList);
        return Result.succeed(map);
    }

    @ApiOperation("查询某个人员的网点列表")
    @GetMapping(value = "/user/{userId}")
    public Result getBranchByUserId(@PathVariable("userId") Long userId) {
        return Result.succeed(serviceBranchService.getBranchByUserId(userId));
    }

}
