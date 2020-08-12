package com.zjft.usp.uas.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.user.model.UserWx;

/**
 * @author chenxiaod
 * @description: 用户微信账号mapper接口
 * @date 2019/8/8 17:12
 */
public interface UserWxMapper extends BaseMapper<UserWx> {

    /**
     * description: 绑定微信
     *
     * @param userWx
     * @return void
     */
    void wxpayBind(UserWx userWx);
}
