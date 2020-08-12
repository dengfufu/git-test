package com.zjft.usp.zj.work.repair.controller;

import com.zjft.usp.anyfix.work.request.dto.WorkStatDto;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.repair.composite.RepairStatCompoService;
import com.zjft.usp.zj.work.repair.filter.RepairFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 报修单统计
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-01 16:09
 **/
@Api(tags = "报修单统计")
@RestController
@RequestMapping("/zj/repair-stat")
public class RepairStatController {
    @Autowired
    private RepairStatCompoService repairStatCompoService;

    @ApiOperation("统计各个状态的工单数量")
    @PostMapping(value = "/status/count")
    public Result<List<WorkStatDto>> countCaseStatus(@RequestBody RepairFilter repairFilter,
            @LoginUser UserInfo userInfo,
            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(repairStatCompoService.countRepairStatus(repairFilter, userInfo, reqParam));
    }

}
