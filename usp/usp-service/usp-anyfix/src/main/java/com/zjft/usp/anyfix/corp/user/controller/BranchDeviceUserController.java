package com.zjft.usp.anyfix.corp.user.controller;

import com.zjft.usp.anyfix.corp.user.composite.BranchUserCompoService;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "网点人员")
@RestController
@RequestMapping("/branch-user")
public class BranchDeviceUserController {

    @Autowired
    private BranchUserCompoService branchUserCompoService;

    @ApiOperation("远程调用：根据用户ID和企业ID删除服务网点以及设备网点人员")
    @PostMapping(value = "/feign/deleteBranchUser")
    public Result deleteBranchUser(@RequestParam("userId") Long userId,
                                   @RequestParam("corpId") Long corpId,
                                   @RequestParam(value = "currentUserId", defaultValue = "0") Long currentUserId,
                                   @RequestParam("clientId") String clientId) {
        branchUserCompoService.delServiceDeviceBranchUser(userId, corpId, currentUserId, clientId);
        return Result.succeed();
    }
}
