package com.zjft.usp.anyfix.work.follow.controller;
import com.zjft.usp.anyfix.work.follow.dto.WorkFollowDto;
import com.zjft.usp.anyfix.work.follow.service.WorkFollowService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 跟进记录表 前端控制器
 * </p>
 *
 * @author cxd
 * @since 2020-05-11
 */
@RestController
@RequestMapping("/work-follow")
public class WorkFollowController {
    @Autowired
    private WorkFollowService workFollowService;

    @ApiOperation(value = "添加跟进记录")
    @PostMapping(value = "/add")
    public Result addWorkAttention(@RequestBody WorkFollowDto workFollowDto,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        workFollowService.addWorkFollow(workFollowDto, userInfo.getUserId(),reqParam.getCorpId());
        return Result.succeed();
    }
}
