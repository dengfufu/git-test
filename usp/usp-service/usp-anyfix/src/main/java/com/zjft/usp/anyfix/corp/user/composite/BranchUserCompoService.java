package com.zjft.usp.anyfix.corp.user.composite;

/**
 * 网点人员聚合服务类
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/02/25 17:36
 */
public interface BranchUserCompoService {

    /**
     * 删除网点人员
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/2/25 17:36
     */
    @Deprecated
    void delBranchUser(Long userId, Long corpId, Long currentUserId, String clientId);

    /**
     * 删除服务网点以及设备网点人员
     *
     * @param userId
     * @param corpId
     * @param currentUserId
     * @param clientId
     */
    void delServiceDeviceBranchUser(Long userId, Long corpId, Long currentUserId, String clientId);

}
