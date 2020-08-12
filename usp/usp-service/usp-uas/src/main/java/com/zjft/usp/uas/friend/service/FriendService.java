package com.zjft.usp.uas.friend.service;

import com.zjft.usp.uas.corp.dto.CorpInfoDto;
import com.zjft.usp.uas.friend.dto.*;
import com.zjft.usp.uas.friend.model.FriendApply;
import com.zjft.usp.uas.friend.model.LinkFriend;

import java.util.List;
import java.util.Map;

public interface FriendService {

    /**
     * 添加好友请求
     * @param friendApply
     */
    int addFriendApply(FriendApply friendApply,long userId);

    /**
     * 处理好友请求
     * @param friendApply
     */
    void handleFriendApply(FriendApply friendApply);

    /**
     * 查询新的好友列表
     * @param userId
     * @return
     */
    List<NewFriendDto> listNewFriend(long userId);

    /**
     * 查询好友列表
     * @param userId
     * @return
     */
    List<FriendDto> listFriend(long userId);

    /**
     * 删除好友
     * @param linkFriend
     */
    void deleteFriend(LinkFriend linkFriend);

    /**
     * 设置好友信息
     * @param linkFriend
     */
    void setFriendInfo(LinkFriend linkFriend);


    /**
     * 获取好友详情
     * @param linkFriend
     * @return
     */
    FriendDetailDto getUserDetail(LinkFriend linkFriend);

    /**
     *
     * @param condition
     * @return
     */
    List<CorpInfoDto>selectCorpList(String condition);

    /**
     * -
     * @param mobile
     * @return
     */
    SearchFriendDto searchFriendDto(String mobile);

    /**
     * 查询我的同事
     * @param userId
     * @return
     */
    Map<String,List<ColleagueDto>> selectColleagueDtoList(long userId);


}
