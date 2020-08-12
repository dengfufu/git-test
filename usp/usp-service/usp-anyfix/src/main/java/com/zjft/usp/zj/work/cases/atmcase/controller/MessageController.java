package com.zjft.usp.zj.work.cases.atmcase.controller;

import com.zjft.usp.common.annotation.LoginClient;
import com.zjft.usp.common.model.OauthClient;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.zj.work.cases.atmcase.composite.MessageCompoService;
import com.zjft.usp.zj.work.cases.atmcase.dto.message.MessageDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息推送 前端控制器
 *
 * @author zgpi
 * @date 2020-4-7 20:34
 **/
@Api(tags = "消息推送")
@RestController
@RequestMapping("/zj/message")
public class MessageController {

    @Autowired
    private MessageCompoService messageCompoService;

    @ApiOperation(value = "发送普通消息")
    @PostMapping("/send")
    public Result sendOrdinaryMessage(@RequestBody MessageDto messageDto,
                                      @LoginClient(isFull = true) OauthClient oauthClient) {
        messageCompoService.sendOrdinaryMessage(messageDto, oauthClient);
        return Result.succeed();
    }
}
