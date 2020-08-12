package com.zjft.usp.anyfix.corp.user.composite.impl;

import com.zjft.usp.anyfix.corp.user.composite.BranchUserCompoService;
import com.zjft.usp.anyfix.corp.user.service.DeviceBranchUserService;
import com.zjft.usp.anyfix.corp.user.service.ServiceBranchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class BranchUserCompoServiceImpl implements BranchUserCompoService {

    @Autowired
    private ServiceBranchUserService serviceBranchUserService;
    @Autowired
    private DeviceBranchUserService deviceBranchUserService;


    /**
     * 删除网点人员
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/2/25 17:36
     */
    @Override
    public void delBranchUser(Long userId, Long corpId, Long currentUserId, String clientId) {
        serviceBranchUserService.delBranchUserByCorp(userId, corpId, currentUserId, clientId);
    }

    @Override
    public void delServiceDeviceBranchUser(Long userId, Long corpId, Long currentUserId, String clientId) {
        serviceBranchUserService.delBranchUserByCorp(userId, corpId, currentUserId, clientId);
        deviceBranchUserService.delDeviceUserByCorp(userId, corpId, currentUserId, clientId);
    }
}
