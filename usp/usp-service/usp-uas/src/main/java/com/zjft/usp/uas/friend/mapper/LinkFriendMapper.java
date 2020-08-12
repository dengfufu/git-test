package com.zjft.usp.uas.friend.mapper;

import com.zjft.usp.uas.friend.model.LinkFriend;
import com.zjft.usp.uas.friend.model.LinkFriendKey;

public interface LinkFriendMapper {
    int deleteByPrimaryKey(LinkFriendKey key);

    int insert(LinkFriend record);

    int insertSelective(LinkFriend record);

    LinkFriend selectByPrimaryKey(LinkFriendKey key);

    int updateByPrimaryKeySelective(LinkFriend record);

    int updateByPrimaryKey(LinkFriend record);
}