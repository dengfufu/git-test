package com.zjft.usp.anyfix.work.booking.controller;

import com.zjft.usp.anyfix.work.booking.dto.WorkBookingDto;
import com.zjft.usp.anyfix.work.booking.service.WorkBookingService;
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

/**
 * 工单预约 前端控制器
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/5 08:56
 */
@Api(tags = "工单预约")
@RestController
@RequestMapping("/work-booking")
public class WorkBookingController {

    @Autowired
    private WorkBookingService workBookingService;

    @ApiOperation(value = "工程师修改预约时间")
    @PostMapping(value = "/engineer/change")
    public Result changeBookingTime(@RequestBody WorkBookingDto workBookingDto,
                                    @LoginUser UserInfo userInfo,
                                    @CommonReqParam ReqParam reqParam) {
        workBookingService.changeBookingTime(workBookingDto, userInfo, reqParam);
        return Result.succeed();
    }
}
