package com.zjft.usp.uas.user.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.filter.UserInfoFilter;

import java.util.List;
import java.util.Map;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/19 10:51
 */
public interface UserCompoService {

    /**
     * 根据条件分页查询用户基本信息
     *
     * @param userInfoFilter
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.uas.user.dto.UserLogonLogDto>
     * @date 2020/5/26
     */
    ListWrapper<UserInfoDto> queryUserInfo(UserInfoFilter userInfoFilter, ReqParam reqParam);

    /**
     * 模糊查询平台有效用户
     *
     * @param userInfoFilter
     * @param reqParam
     * @return
     * @date 2020/6/29
     */
    List<UserInfoDto> matchUser(UserInfoFilter userInfoFilter, ReqParam reqParam);

    /**
     * 获得单个用户基本信息
     *
     * @param userId
     * @param reqParam
     * @return com.zjft.usp.uas.user.dto.UserInfoDto
     * @date 2020/6/4
     */
    UserInfoDto findUserDetail(Long userId, ReqParam reqParam);

    /**
     * 用户编号与手机号映射
     *
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2019/12/19 11:01
     **/
    Map<Long, String> mapUserIdAndMobile(List<Long> userIdList);
}
