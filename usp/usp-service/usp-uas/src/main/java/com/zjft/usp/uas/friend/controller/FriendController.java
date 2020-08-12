package com.zjft.usp.uas.friend.controller;


import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.uas.corp.dto.CorpInfoDto;
import com.zjft.usp.uas.friend.dto.*;
import com.zjft.usp.uas.friend.model.FriendApply;
import com.zjft.usp.uas.friend.model.FriendConstants;
import com.zjft.usp.uas.friend.model.LinkFriend;
import com.zjft.usp.uas.friend.service.FriendService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * @Author : zrlin
 * @Date : 2019年8月12日10:25:26
 * @Desc : 好友相关请求类
 */
@Api(tags = "好友")
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @RequestMapping(value="/friendApply" , method = RequestMethod.POST)
    public Result friendApply(@RequestBody FriendApply friendApply,
                              @LoginUser UserInfo userInfo){
        // TODO 校验申请时间
        if(friendApply.getFriendId() == 0){
            return Result.failed("添加好友出错");
        }
        friendApply.setUserId(userInfo.getUserId());
        int status = friendService.addFriendApply(friendApply,userInfo.getUserId());
        if( status == FriendConstants.IS_YOURSELF){
            return  Result.succeed("不能添加自己");
        }
        if(status == FriendConstants.BEEN_FRIEND){
            return  Result.succeed("对方已是您的好友，无需重复添加");
        } else {
            return Result.succeed("已发送好友请求");
        }

    }

    /**
     * 处理好友请求
     * @param friendApply
     * @return
     */
    @RequestMapping(value="/handleFriendApply" , method = RequestMethod.POST)
    public Result handleFriendApply(@RequestBody FriendApply friendApply,@LoginUser UserInfo userInfo){
        // TODO 校验审核时间
        if(friendApply.getUserId()==null){
            return Result.failed("朋友编号不能为空");
        }
        if(friendApply.getStatus() == 0 ){
            return Result.failed("操作异常");
        }
        friendApply.setFriendId(userInfo.getUserId());
        friendService.handleFriendApply(friendApply);
        if(friendApply.getStatus() == FriendConstants.PASS){
            return Result.succeed("OK");
        }
        return Result.succeed();
    }

    /**
     * 查看我的新的好友
     * @param userInfo
     * @return
     */
    @RequestMapping(value="/listNewFriend" , method = RequestMethod.POST)
    public Result<List<NewFriendDto>> listNewFriend(@LoginUser UserInfo userInfo){
        long userId = userInfo.getUserId();
        // TODO 校验审核时间
        List<NewFriendDto> list = friendService.listNewFriend(userId);
        return Result.succeed(list);
    }

    /**
     * 查询好友列表
     * @param userInfo
     * @return
     */
    @RequestMapping(value="/listFriend" , method = RequestMethod.POST)
    public Result<List<FriendDto>> listFriend(@LoginUser UserInfo userInfo) {
        long  userId = userInfo.getUserId();
        // TODO 校验审核时间
        List<FriendDto> list = friendService.listFriend(userId);
        return Result.succeed(list);
    }



    /**
     * 设置好友信息，备注、标签
     * @param linkFriend
     * @return
     */
    @RequestMapping(value="/setFriendInfo" , method = RequestMethod.POST)
    public String setFriendInfo(@RequestBody LinkFriend linkFriend){

        friendService.setFriendInfo(linkFriend);
        return  JsonUtil.toJson("设置成功");
    }

    /**
     * 名片详情
     * @param linkFriend
     * @return
     */
    @RequestMapping(value="/getUserDetail" , method = RequestMethod.POST)
    public Result<FriendDetailDto> getUserDetail(@RequestBody LinkFriend linkFriend,@LoginUser UserInfo userInfo){
        linkFriend.setUserId(userInfo.getUserId());
        FriendDetailDto friendDetailDto = friendService.getUserDetail(linkFriend);
        return  Result.succeed(friendDetailDto);
    }



    /**
     * 获取同事
     * @param map
     * @return
     */
    @RequestMapping(value="/listCrop" , method = RequestMethod.POST)
    public Result<List<CorpInfoDto>> listCrop(@RequestBody Map<String,String> map){
        List<CorpInfoDto> list = friendService.selectCorpList(map.get("condition"));
        return  Result.succeed(list);
    }

    /**
     * 删除好友
     * @param linkFriend
     * @return
     */
    @RequestMapping(value="/deleteFriend" , method = RequestMethod.POST)
    public String deleteFriend(@RequestBody LinkFriend linkFriend){
        friendService.deleteFriend(linkFriend);
        return  JsonUtil.toJson("删除好友成功");
    }

    /**
     * 查询用户
     * @param map
     * @return
     */
    @RequestMapping(value="/searchUser" , method = RequestMethod.POST)
    public Result<SearchFriendDto> searchUser(@RequestBody Map<String,String> map, @LoginUser UserInfo userInfo){
        String mobile = map.get("mobile");
        SearchFriendDto searchFriendDto = friendService.searchFriendDto(mobile);
        if(searchFriendDto != null){
            return Result.succeed(searchFriendDto);
        } else {
            return Result.failed("查找不到该用户");
        }
    }

    /**
     * 查询同事列表
     * @param userInfo
     * @return
     */
    @RequestMapping(value="/listColleague" , method = RequestMethod.POST)
    public Result listColleague(@LoginUser UserInfo userInfo ){
        Long userLong = userInfo.getUserId();
        Map<String,List<ColleagueDto>> listMap =  friendService.selectColleagueDtoList(userLong);
        return Result.succeed(listMap);
    }



}
