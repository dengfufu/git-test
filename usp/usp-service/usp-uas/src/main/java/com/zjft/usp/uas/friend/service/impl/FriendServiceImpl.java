package com.zjft.usp.uas.friend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.uas.corp.dto.CorpInfoDto;
import com.zjft.usp.uas.friend.dto.*;
import com.zjft.usp.uas.friend.mapper.FriendApplyMapper;
import com.zjft.usp.uas.friend.mapper.FriendMapper;
import com.zjft.usp.uas.friend.mapper.LinkFriendMapper;
import com.zjft.usp.uas.friend.model.FriendConstants;
import com.zjft.usp.uas.friend.model.FriendApply;
import com.zjft.usp.uas.friend.model.LinkFriend;
import com.zjft.usp.uas.friend.service.FriendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class FriendServiceImpl implements FriendService {

    @Resource
    private FriendApplyMapper friendApplyMapper;
    @Resource
    private LinkFriendMapper linkFriendMapper;
    @Resource
    private FriendMapper friendMapper;

    @Override
    public int addFriendApply(FriendApply friendApply, long userId){
        if( friendApply.getFriendId() == userId){
           throw new AppException("不能添加自己！");
        }
        List<FriendApply> friendApplyRel = friendApplyMapper.selectFriendApplyRel(friendApply);
        if(friendApplyRel.size()>0){
            for(FriendApply apply: friendApplyRel){
                if(apply.getStatus() == FriendConstants.PASS){
                    throw new AppException("已是您的好友！");
                }
            }
            for(FriendApply apply: friendApplyRel ){
                if(apply.getUserId() == userId){
                    return FriendConstants.SEND;
                }
            }
        }
        friendApply.setStatus(FriendConstants.APPLY);
        // 从前台获取
        friendApply.setTxId(3L);
        friendApplyMapper.insertSelective(friendApply);
        return FriendConstants.SEND;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleFriendApply(FriendApply friendApply) {
        friendApplyMapper.updateFriendApplyRel(friendApply);
        if(friendApply.getStatus() == FriendConstants.PASS) {
            LinkFriend linkFriend = new LinkFriend();
            linkFriend.setUserId(friendApply.getUserId());
            linkFriend.setFriendId(friendApply.getFriendId());

            LinkFriend linkFriend2 = new LinkFriend();
            linkFriend2.setUserId(friendApply.getFriendId());
            linkFriend2.setFriendId(friendApply.getUserId());
            linkFriendMapper.insertSelective(linkFriend2);
            linkFriendMapper.insertSelective(linkFriend);
        }
    }

    @Override
    public List<NewFriendDto> listNewFriend(long userId) {
        return friendMapper.selectNewFriendList(userId);
    }

    @Override
    public List<FriendDto> listFriend(long userId) {
        return friendMapper.selectFriendList(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(LinkFriend linkFriend) {
        //删除好友申请表
        FriendApply friendApply = new FriendApply();
        friendApply.setFriendId(linkFriend.getFriendId());
        friendApply.setUserId(linkFriend.getUserId());
        friendApplyMapper.deleteFriendApply(friendApply);

        LinkFriend linkFriend2 = new LinkFriend();
        linkFriend2.setUserId(linkFriend.getFriendId());
        linkFriend2.setFriendId(linkFriend.getUserId());
        linkFriendMapper.deleteByPrimaryKey(linkFriend);
        linkFriendMapper.deleteByPrimaryKey(linkFriend2);
    }

    @Override
    public void setFriendInfo(LinkFriend linkFriend) {
        linkFriendMapper.updateByPrimaryKeySelective(linkFriend);
    }


    @Override
    public FriendDetailDto getUserDetail(LinkFriend linkFriend) {

        return friendMapper.selectFriendDetail(linkFriend);
    }

    public void updateLinkFriend(LinkFriend linkFriend){
        linkFriendMapper.updateByPrimaryKeySelective(linkFriend);
    }
    @Override
    public List<CorpInfoDto> selectCorpList(String condition){
        if(StrUtil.isNotBlank(condition)){
            String str = condition.replaceAll(" ","");
            String str2 = str.replaceAll("" , "%");
            condition = str2;
        }
        return friendMapper.selectCorpList(condition);
    }

    @Override
    public SearchFriendDto searchFriendDto(String mobile){
        return friendMapper.selectUserShortDetail(mobile);
    }

    @Override
    public  Map<String,List<ColleagueDto>>  selectColleagueDtoList(long userId) {
        List<Map<String,Object>> listColleague = friendMapper.selectColleagueList(userId);
        Map<String,List<ColleagueDto>> corpNameMap = new HashMap<>();
        for(Map<String,Object>  map: listColleague){
            corpNameMap.put((String) map.get("corpName"),null);
        }
        Iterator<String> it = corpNameMap.keySet().iterator();

        while (it.hasNext()) {
            corpNameMap.put(it.next(),new ArrayList<ColleagueDto>());
        }
        for(Map<String,Object> map :listColleague){
            ColleagueDto colleagueDto = new ColleagueDto();
            colleagueDto.setNickname((String) map.get("nickname"));
            colleagueDto.setMobile((String) map.get("mobile"));
            long faceImg = (long) map.get("faceImg");
            colleagueDto.setFaceImg(Long.toString(faceImg));
            corpNameMap.get(map.get("corpName")).add(colleagueDto);
        }
        return corpNameMap;

    }


}


