package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.user.model.UserMobileInvalid;

/**
 * @author zphu
 * @date 2019/8/13 19:47
 * @Version 1.0
 **/
public interface UserMobileInvalidService extends IService<UserMobileInvalid> {

    /**
     * 插入记录
     *
     * @param mobile
     * @return void
     * @author zphu
     * @date 2019/8/13 19:57
     **/
    void insert(String mobile);
}
