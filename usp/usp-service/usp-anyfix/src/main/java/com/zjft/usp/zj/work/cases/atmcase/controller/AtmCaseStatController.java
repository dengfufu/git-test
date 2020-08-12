package com.zjft.usp.zj.work.cases.atmcase.controller;

import com.zjft.usp.anyfix.work.request.dto.WorkStatDto;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.composite.AtmCaseStatCompoService;
import com.zjft.usp.zj.work.cases.atmcase.filter.AtmCaseFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ATM机CASE 前端控制层
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-31 14:41
 **/
@Api(tags = "ATM机CASE统计")
@RestController
@RequestMapping("/zj/atmcase-stat")
public class AtmCaseStatController {
    @Autowired
    private AtmCaseStatCompoService atmCaseStatCompoService;

    @ApiOperation("统计各个状态的工单数量")
    @PostMapping(value = "/status/count")
    public Result<List<WorkStatDto>> countCaseStatus(@RequestBody AtmCaseFilter atmCaseFilter,
                                                     @LoginUser UserInfo userInfo,
                                                     @CommonReqParam ReqParam reqParam) {
        return Result.succeed(atmCaseStatCompoService.countCaseStatus(atmCaseFilter, userInfo, reqParam));
    }

}
