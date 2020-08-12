package com.zjft.usp.anyfix.work.support.controller;


import com.zjft.usp.anyfix.work.support.dto.WorkSupportRecordDto;
import com.zjft.usp.anyfix.work.support.model.WorkSupportRecord;
import com.zjft.usp.anyfix.work.support.service.WorkSupportRecordService;
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
 * 技术支持跟踪记录表 前端控制器
 * </p>
 *
 * @author cxd
 * @since 2020-04-28
 */
@RestController
@RequestMapping("/work-support-record")
public class WorkSupportRecordController {
    @Autowired
    private WorkSupportRecordService workSupportRecordService;

    @ApiOperation(value = "添加跟踪记录")
    @PostMapping(value = "/add")
    public Result addWorkSupportRecord(@RequestBody WorkSupportRecordDto workSupportRecordDto,
                                       @LoginUser UserInfo userInfo,
                                       @CommonReqParam ReqParam reqParam) {
        workSupportRecordService.addWorkSupportRecord(workSupportRecordDto, userInfo, reqParam);
        return Result.succeed();
    }
}
