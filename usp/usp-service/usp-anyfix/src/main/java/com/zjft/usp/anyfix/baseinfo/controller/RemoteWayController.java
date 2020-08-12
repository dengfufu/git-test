package com.zjft.usp.anyfix.baseinfo.controller;


import com.zjft.usp.anyfix.baseinfo.filter.RemoteWayFilter;
import com.zjft.usp.anyfix.baseinfo.model.RemoteWay;
import com.zjft.usp.anyfix.baseinfo.service.RemoteWayService;
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
 * 远程处理方式表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Api(tags = "远程处理方式")
@RestController
@RequestMapping("/remote-way")
public class RemoteWayController {

    @Autowired
    private RemoteWayService remoteWayService;

    @ApiOperation(value = "添加远程处理方式")
    @PostMapping(value = "/add")
    public Result addRemoteWay(@RequestBody RemoteWay remoteWay,
                               @LoginUser UserInfo userInfo,
                               @CommonReqParam ReqParam reqParam) {
        remoteWayService.save(remoteWay,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "修改远程处理方式")
    @PostMapping(value = "/update")
    public Result updateRemoteWay(@RequestBody RemoteWay remoteWay,
                                  @LoginUser UserInfo userInfo) {
        remoteWayService.update(remoteWay,userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "删除远程处理方式")
    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        remoteWayService.removeById(id);
        return Result.succeed();
    }

    @ApiOperation(value = "获得单个远程处理方式")
    @GetMapping(value = "/{id}")
    public Result selectById(@PathVariable("id") Long id) {
        return Result.succeed(remoteWayService.getById(id));
    }

    @ApiOperation(value = "获得远程处理方式列表")
    @PostMapping(value = "/list")
    public Result<List<RemoteWay>> list(@RequestBody RemoteWay remoteWay,
                                        @CommonReqParam ReqParam reqParam) {
        Long corpId = reqParam.getCorpId();
        if (LongUtil.isNotZero(remoteWay.getServiceCorp())) {
            corpId = remoteWay.getServiceCorp();
        }
        return Result.succeed(remoteWayService.selectByServiceCrop(corpId));
    }

    @ApiOperation(value = "分页查询远程处理方式列表")
    @PostMapping(value = "/query")
    public Result<ListWrapper<RemoteWay>> query(@RequestBody RemoteWayFilter remoteWayFilter,
                                                @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(remoteWayFilter.getServiceCorp())) {
            remoteWayFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(remoteWayService.query(remoteWayFilter));
    }
}
