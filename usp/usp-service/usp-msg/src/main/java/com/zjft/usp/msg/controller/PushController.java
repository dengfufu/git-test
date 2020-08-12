package com.zjft.usp.msg.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.msg.dto.MsgFilter;
import com.zjft.usp.msg.dto.MsgJobDto;
import com.zjft.usp.msg.model.PushConstants;
import com.zjft.usp.msg.model.PushMsg;
import com.zjft.usp.msg.service.MessageService;
import com.zjft.usp.msg.service.PushMessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author : Zrlin
 * @Date : 2019/9/11
 * @Desc : 推送控制器
 * @Version 1.0.0
 */
@RestController
public class PushController {

    @Resource
    private PushMessageService pushMessageService;

    @Resource
    private MessageService messageService;

    @PostMapping(value = "/alias/setAlias")
    public Result setAlias(@RequestParam("registrationId") String registrationId,
                           @LoginUser UserInfo info) {
        String userId = Long.toString(info.getUserId());
        pushMessageService.setAlias(registrationId, userId);
        return Result.succeed();
    }

    @PostMapping(value = "/message/signPushMsg")
    public Result signPushMsg(@RequestBody PushMsg pushMsg) {
        pushMsg.setReceiveStatus(PushConstants.RECEIVE);
        messageService.signPushMsg(pushMsg);
        return Result.succeed();
    }

    @PostMapping("/notice/query")
    public Result<ListWrapper<PushMsg>> queryDeviceBranch(@RequestBody MsgFilter msgFilter) {
        try {
            ListWrapper<PushMsg> pushMsgList = this.pushMessageService.query(msgFilter);
            return Result.succeed(pushMsgList);
        } catch (Exception exception) {
            // 表不存在就返回空数据
            ListWrapper<PushMsg> list = new ListWrapper<>();
            return Result.succeedWith(list, 0, "表不存在!");
        }
    }

    @GetMapping("/message/unSend/list")
    public Result<List<PushMsg>> listUnSend(@LoginUser UserInfo userInfo) {
        try {
            List<PushMsg> pushMsgList = pushMessageService.listUnSend(userInfo.getUserId());
            return Result.succeed(pushMsgList);
        } catch (Exception exception) {
            // 表不存在就返回空数据
            return Result.failed( "表不存在!");
        }
    }

    /**
     * 定时创建单点消息记录表
     * @date 2020/1/20
     * @param msgJobDto dto
     * @return com.zjft.usp.common.model.Result<java.util.List<com.zjft.usp.pos.model.PosTrack>>
     */
    @RequestMapping(value = "/createPushMsg", method = RequestMethod.POST)
    public Result createMai(@RequestBody MsgJobDto msgJobDto) {
        pushMessageService.createPushMsgTable();
        return Result.succeed();
    }

    /**
     * 定时创建公告表
     * @date 2020/1/20
     * @param msgJobDto dto
     * @return com.zjft.usp.common.model.Result<java.util.List<com.zjft.usp.pos.model.PosTrack>>
     */
    @RequestMapping(value = "/createBulletin", method = RequestMethod.POST)
    public Result createPushMsg(@RequestBody MsgJobDto msgJobDto) {
        return Result.succeed();
    }

    /**
     * 定时创建群发通知表
     * @date 2020/1/20
     * @param msgJobDto dto
     * @return com.zjft.usp.common.model.Result<java.util.List<com.zjft.usp.pos.model.PosTrack>>
     */
    @RequestMapping(value = "/createPushNotice", method = RequestMethod.POST)
    public Result createPushNotice(@RequestBody MsgJobDto msgJobDto) {
        return Result.succeed();
    }
}
