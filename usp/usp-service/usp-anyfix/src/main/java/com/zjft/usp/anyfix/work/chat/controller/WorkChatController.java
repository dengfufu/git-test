package com.zjft.usp.anyfix.work.chat.controller;


import com.zjft.usp.anyfix.work.chat.enums.OperateTypeEnum;
import com.zjft.usp.anyfix.work.chat.filter.WorkChatFilter;
import com.zjft.usp.anyfix.work.chat.model.WorkChat;
import com.zjft.usp.anyfix.work.chat.service.WorkChatService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linzerun
 * @since 2020-03-09
 */
@RestController
@RequestMapping("/work-chat")
public class WorkChatController {

    @Autowired
    private WorkChatService workChatService;

    @PostMapping("send")
    public Result sendMsg(@RequestBody WorkChat workChat, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        workChat.setUserId(userInfo.getUserId());
        workChat.setCorpId(reqParam.getCorpId());
        workChatService.sendMsg(workChat);
        workChatService.notify(workChat.getWorkId(), userInfo.getUserId());
        return Result.succeed();
    }

    @PostMapping("query")
    public Result queryMsg(@RequestBody WorkChatFilter workChatFilter, @CommonReqParam ReqParam reqParam) {
        return Result.succeed(workChatService.selectWorkChatList(workChatFilter ,reqParam));
    }


    @PostMapping("recall")
    public Result recallMsg(@RequestBody WorkChatFilter workChatFilter) {
        workChatService.operateMsg(workChatFilter.getId(), OperateTypeEnum.RECALL.getCode());
        return Result.succeed();
    }

    @PostMapping("delete")
    public Result deleteMsg(@RequestBody WorkChatFilter workChatFilter) {
        workChatService.operateMsg(workChatFilter.getId(), OperateTypeEnum.DELETE.getCode());
        return Result.succeed();
    }

}
