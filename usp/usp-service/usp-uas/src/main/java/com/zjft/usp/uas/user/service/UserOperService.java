package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.dto.UserOperDto;
import com.zjft.usp.uas.user.enums.UserOperEnum;
import com.zjft.usp.uas.user.filter.UserOperFilter;
import com.zjft.usp.uas.user.model.UserOper;

/**
 * @author zphu
 * @date 2019/8/15 14:44
 * @Version 1.0
 **/
public interface UserOperService extends IService<UserOper> {

    /**
     * 根据条件分页查询用户操作
     * @date 2020/5/26
     * @param userOperFilter
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.uas.user.dto.UserLogonLogDto>
     */
    ListWrapper<UserOperDto> queryUserOper(UserOperFilter userOperFilter, UserInfo userInfo, ReqParam reqParam);
    /**
     * 插入用户操作用户信息的信息
     *
     * @param changeInfo
     * @param beforeInfo
     * @param userOperEnum
     * @param userId
     * @param reqParam
     * @param clientId
     * @return void
     * @throws
     * @author zphu
     * @date 2019/8/16 14:49
     **/
    void insert(String changeInfo, String beforeInfo, UserOperEnum userOperEnum,
                Long userId, ReqParam reqParam, String clientId);

}
