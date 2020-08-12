package com.zjft.usp.msg.controller;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.msg.dto.MsgJobDto;
import com.zjft.usp.msg.model.Sms;
import com.zjft.usp.msg.service.SmsSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author : dcyu
 * @Date : 2019/8/27 9:26
 * @Desc : 短信发送控制类
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/sms")
public class SmsSendController {

    @Autowired
    private SmsSendService smsSendService;

    @PostMapping(value = "sendSms")
    public Result sendSms(@RequestBody Sms sms) {
        smsSendService.productSmsToQueue(sms);
        return Result.succeed();
    }

    @PostMapping(value = "sendVerifySms")
    public Result sendVerifySms(@RequestParam("appId") int appId, @RequestParam("phoneNumbers") String phoneNumbers, @RequestParam(value = "verifyCode") String verifyCode) {
        smsSendService.productVerifySmsToQueue(appId, phoneNumbers, verifyCode);
        return Result.succeed();
    }

    /**
     * 定时创建短信发送记录表
     * @date 2020/1/20
     * @param msgJobDto dto
     * @return com.zjft.usp.common.model.Result<java.util.List<com.zjft.usp.pos.model.PosTrack>>
     */
    @RequestMapping(value = "/createSms", method = RequestMethod.POST)
    public Result createSms(@RequestBody MsgJobDto msgJobDto) {
        smsSendService.createSmsTable();
        return Result.succeed();
    }

    /**
     * 定时创建邮件发送记录表
     * @date 2020/1/20
     * @param msgJobDto dto
     * @return com.zjft.usp.common.model.Result<java.util.List<com.zjft.usp.pos.model.PosTrack>>
     */
    @RequestMapping(value = "/createMai", method = RequestMethod.POST)
    public Result createMai(@RequestBody MsgJobDto msgJobDto) {
        smsSendService.createMailTable();
        return Result.succeed();
    }
}
