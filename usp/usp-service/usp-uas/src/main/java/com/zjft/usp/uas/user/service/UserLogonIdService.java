package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.user.model.UserLogonId;

/**
 * @author zphu
 * @date 2019/8/13 16:53
 * @Version 1.0
 **/
public interface UserLogonIdService extends IService<UserLogonId> {

    /**
     * 更新或者保存用户登录id
     *
     * @param logonId 登录id
     * @param userId  用户id
     * @return boolean true=保存或者更新成功 false=该用户名已经被占用
     * @author zphu
     * @date 2019/8/13 16:56
     **/
    boolean saveOrUpdate(String logonId, long userId);
}
