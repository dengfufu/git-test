package com.zjft.usp.anyfix.work.fee.controller;


import com.zjft.usp.anyfix.work.fee.dto.WorkPostDto;
import com.zjft.usp.anyfix.work.fee.service.WorkPostService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
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
 * 工单邮寄费 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
@Api(tags = "工单邮寄费")
@RestController
@RequestMapping("/work-post")
public class WorkPostController {

    @Autowired
    private WorkPostService workPostService;

    @ApiOperation("添加")
    @PostMapping(value = "/add")
    public Result add(@RequestBody WorkPostDto workPostDto, @LoginUser UserInfo userInfo) {
        this.workPostService.add(workPostDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("更新")
    @PostMapping(value = "/update")
    public Result update(@RequestBody WorkPostDto workPostDto, @LoginUser UserInfo userInfo) {
        this.workPostService.update(workPostDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("删除")
    @DeleteMapping(value = "/{postId}")
    public Result delete(@PathVariable("postId") Long postId) {
        this.workPostService.delete(postId);
        return Result.succeed();
    }

    @ApiOperation("模糊匹配邮寄公司")
    @PostMapping(value = "/matchCorp")
    public Result<List<WorkPostDto>> matchPostCorp(@RequestBody WorkPostDto workPostDto, @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workPostDto.getCorpId())) {
            workPostDto.setCorpId(reqParam.getCorpId());
        }
        return Result.succeed(this.workPostService.matchPostCorp(workPostDto));
    }

}
