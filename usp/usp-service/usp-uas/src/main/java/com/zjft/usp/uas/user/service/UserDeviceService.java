package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.model.UserDevice;

/**
 * @author zphu
 * @Description
 * @date 2019/8/9 8:57
 * @Version 1.0
 **/
public interface UserDeviceService extends IService<UserDevice> {

    /**
     * 保存用户设备信息
     * @param reqParam
     * @param userId
     */
    void addUserDeviceInfo(ReqParam reqParam, Long userId);

    /**
     * 是否有记录
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 10:31
     **/
    boolean findHasRecord(Long userId);
}
