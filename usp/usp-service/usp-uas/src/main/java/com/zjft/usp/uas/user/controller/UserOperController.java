package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.dto.UserOperDto;
import com.zjft.usp.uas.user.filter.UserOperFilter;
import com.zjft.usp.uas.user.service.UserOperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户操作 前端控制器
 * </p>
 *
 * @author cxd
 * @since 2020-05-25
 */
@Api(tags = "用户操作")
@RestController
@RequestMapping("/user-oper")
public class UserOperController {

    @Autowired
    private UserOperService userOperService;

    @ApiOperation(value = "根据条件分页查询用户操作")
    @PostMapping(value = "/query")
    public Result<ListWrapper<UserOperDto>> queryUserOper(@RequestBody UserOperFilter userOperFilter,
                                                          @LoginUser UserInfo userInfo,
                                                          @CommonReqParam ReqParam reqParam) {
        return Result.succeed(userOperService.queryUserOper(userOperFilter, userInfo, reqParam));
    }

}
