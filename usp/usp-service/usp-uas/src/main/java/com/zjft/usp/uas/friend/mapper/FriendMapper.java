package com.zjft.usp.uas.friend.mapper;

import com.zjft.usp.uas.corp.dto.CorpInfoDto;
import com.zjft.usp.uas.friend.dto.*;
import com.zjft.usp.uas.friend.model.LinkFriend;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FriendMapper   {

    List<NewFriendDto> selectNewFriendList(long userId);

    List<FriendDto> selectFriendList(long userId);


    FriendDetailDto selectFriendDetail(LinkFriend linkFriend);

    List<CorpInfoDto> selectCorpList(@Param("condition") String condtion);

    SearchFriendDto selectUserShortDetail(String mobile);

    List<Map<String,Object>> selectColleagueList(long userId);


}
