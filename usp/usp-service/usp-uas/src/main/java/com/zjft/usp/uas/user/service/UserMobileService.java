package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.user.model.UserMobile;

/**
 * @Description
 * @Date
 * @Author zphu
 * @Version 1.0
 */
public interface UserMobileService extends IService<UserMobile> {

    /**
     * 插入记录
     *
     * @param mobile
     * @param userId
     * @return void
     * @author zphu
     * @date 2019/8/13 19:25
     **/
    void insert(String mobile, Long userId);

    /**
     * 更新记录
     *
     * @param mobile
     * @param userId
     * @return boolean
     * @author zphu
     * @date 2019/8/13 19:34
     **/
    boolean update(String mobile, Long userId);

    /**
     * 删除记录
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 10:36
     **/
    void delUserMobile(Long userId);

    /**
     * 判断手机号是否已经注册
     * @param mobile 手机号
     * @return true=注册，false=未注册
     */
    boolean isMobileRegister(String mobile);
}
