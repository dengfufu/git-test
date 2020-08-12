package com.zjft.usp.anyfix.work.evaluate.controller;

import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto;
import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluateIndex;
import com.zjft.usp.anyfix.work.evaluate.service.WorkEvaluateIndexService;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 客户评价指标表 前端控制器
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@RestController
@RequestMapping("/work-evaluate-index")
public class WorkEvaluateIndexController {

    @Resource
    private WorkEvaluateIndexService workEvaluateIndexService;

    @PostMapping("/list")
    public Result<List<WorkEvaluateIndex>> selectByDate(@RequestBody WorkEvaluateDto workEvaluateDto,
                                                         @LoginUser UserInfo userInfo) {
        return Result.succeed(workEvaluateIndexService.selectByMonth(workEvaluateDto, userInfo));
    }
}
