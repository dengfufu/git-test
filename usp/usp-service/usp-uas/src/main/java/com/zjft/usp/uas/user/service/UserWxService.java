package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.user.model.UserWx;

/**
 * @description: TODO
 * @author chenxiaod
 * @date 2019/8/8 17:16
 */
public interface UserWxService extends IService<UserWx> {

    /**
     * description: 绑定微信
     *
     * @param userWx
     * @return void
     */
    void wxpayBind(UserWx userWx);

    void insert(String openId, Long userId);

    String getOpenidByUserid(Long userId);

    /**
     * 检查该号码是否已经绑定微信
     * @param mobile
     * @return
     */
    Boolean checkIsBindWx(String mobile);

    /**
     * 根据userid删除记录
     * @param userId
     */
    void delByUserId(Long userId);
}