package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.user.model.UserApp;

/**
 * @author zphu
 * @date 2019/8/13 20:31
 * @Version 1.0
 **/
public interface UserAppService extends IService<UserApp> {

    /**
     * 插入记录
     *
     * @param clientId
     * @param userId
     */
    void insert(String clientId, Long userId);
}
