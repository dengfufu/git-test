package com.zjft.usp.uas.friend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.friend.model.FriendApply;
import com.zjft.usp.uas.friend.model.FriendApplyKey;

import java.util.List;

public interface FriendApplyMapper extends BaseMapper<FriendApply> {
    int deleteByPrimaryKey(FriendApplyKey key);

    int insert(FriendApply record);

    int insertSelective(FriendApply record);

    FriendApply selectByPrimaryKey(FriendApplyKey key);

    int updateByPrimaryKeySelective(FriendApply record);

    int updateByPrimaryKey(FriendApply record);

    void deleteFriendApply(FriendApply record);

    List<FriendApply> selectFriendApplyRel(FriendApply friendApply);

    void updateFriendApplyRel(FriendApply friendApply);
}