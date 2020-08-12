package com.zjft.usp.anyfix.work.review.controller;

import com.zjft.usp.anyfix.work.review.dto.WorkReviewDto;
import com.zjft.usp.anyfix.work.review.service.WorkReviewService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工单客户回访 前端控制器
 *
 * @author ljzhu
 * @version 1.0
 */
@Api(tags = "工单客户回访")
@RestController
@RequestMapping("/work-review")
public class WorkReviewController {

    @Autowired
    private WorkReviewService workReviewService;

    @ApiOperation(value = "添加客户回访")
    @PostMapping(value = "/add")
    public Result addWorkReview(@RequestBody WorkReviewDto workReviewDto,
                                    @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        workReviewService.addWorkReview(workReviewDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "查询某个工单的客户回访")
    @PostMapping(value = "/list")
    public Result<List<WorkReviewDto>> listWorkReviewsByWorkId(@RequestBody WorkReviewDto workReviewDto,
                                                              @LoginUser UserInfo userInfo,
                                                              @CommonReqParam ReqParam reqParam) {

        return Result.succeed(workReviewService.listWorkReviewsByWorkId(workReviewDto, userInfo, reqParam));
    }

    @ApiOperation(value = "删除一条客户回访记录")
    @PostMapping(value = "/del")
    public Result delWorkReview(@RequestBody WorkReviewDto workReviewDto) {
        workReviewService.delWorkReview(workReviewDto);
        return Result.succeed();
    }

    @ApiOperation(value = "删除某个工单的客户回访记录")
    @PostMapping(value = "/delByWorkId")
    public Result delWorkReviewByWorkId(@RequestBody WorkReviewDto workReviewDto) {
        workReviewService.delWorkReviewByWorkId(workReviewDto);
        return Result.succeed();
    }
}
