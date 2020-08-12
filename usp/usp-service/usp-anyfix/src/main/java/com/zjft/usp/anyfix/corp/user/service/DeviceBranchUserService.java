package com.zjft.usp.anyfix.corp.user.service;

import com.zjft.usp.anyfix.corp.user.dto.CorpUserDto;
import com.zjft.usp.anyfix.corp.user.dto.DeviceBranchUserDto;
import com.zjft.usp.anyfix.corp.user.filter.DeviceBranchUserFilter;
import com.zjft.usp.anyfix.corp.user.model.DeviceBranchUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;

import java.util.List;

/**
 * <p>
 * 设备网点人员表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
public interface DeviceBranchUserService extends IService<DeviceBranchUser> {

    /**
     * 分页查询设备网点人员
     *
     * @param deviceBranchUserFilter
     * @return
     * @author zgpi
     * @date 2019/12/18 09:57
     **/
    ListWrapper<DeviceBranchUserDto> query(DeviceBranchUserFilter deviceBranchUserFilter);

    /**
     * 添加网点人员
     *
     * @param deviceBranchUserDto
     * @return
     * @author zgpi
     * @date 2019/11/28 09:48
     **/
    void addBranchUser(DeviceBranchUserDto deviceBranchUserDto);

    /**
     * 删除网点人员
     *
     * @param userId
     * @param branchId
     * @return
     * @author zgpi
     * @date 2019/11/28 09:48
     **/
    void delBranchUser(Long userId, Long branchId);

    /**
     * 分页查询可选择的设备网点人员
     *
     * @param serviceBranchUserFilter
     * @return
     */
    ListWrapper<CorpUserDto> queryAvailable(DeviceBranchUserFilter serviceBranchUserFilter);

    /**
     * 匹配设备网点人员
     *
     * @param serviceBranchUserFilter
     * @return
     */
    List<CorpUserDto> matchAvailable(DeviceBranchUserFilter serviceBranchUserFilter);

    /**
     * 删除设备网点人员
     *
     * @param userId
     * @param corpId
     * @param currentUserId
     * @param clientId
     */
    void delDeviceUserByCorp(Long userId, Long corpId, Long currentUserId, String clientId);

}
