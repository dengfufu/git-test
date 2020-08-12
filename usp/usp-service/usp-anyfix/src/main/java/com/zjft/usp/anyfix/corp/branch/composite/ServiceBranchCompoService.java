package com.zjft.usp.anyfix.corp.branch.composite;

import com.zjft.usp.anyfix.corp.branch.dto.ServiceBranchDto;
import com.zjft.usp.anyfix.corp.branch.dto.ServiceBranchTreeDto;
import com.zjft.usp.anyfix.corp.branch.filter.ServiceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.common.model.UserInfo;

import java.util.List;

/**
 * 服务网点聚合服务类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/10 09:19
 */
public interface ServiceBranchCompoService {

    /**
     * 添加服务网点
     *
     * @param serviceBranchDto
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2020/3/10 20:15
     */
    void addServiceBranch(ServiceBranchDto serviceBranchDto, UserInfo userInfo);

    /**
     * 修改服务网点
     *
     * @param serviceBranchDto
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2020/3/10 20:15
     */
    void updateServiceBranch(ServiceBranchDto serviceBranchDto, UserInfo userInfo);

    /**
     * 删除服务网点
     *
     * @param branchId
     * @return
     * @author zgpi
     * @date 2020/3/10 21:23
     */
    void delServiceBranch(Long branchId);

    /**
     * 服务网点树
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:38
     */
    List<ServiceBranchTreeDto> listServiceBranchTree(ServiceBranchFilter serviceBranchFilter);

    /**
     * 上级服务网点树
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:38
     */
    List<ServiceBranchTreeDto> listAllUpperServiceBranchTree(ServiceBranchFilter serviceBranchFilter);

    /**
     * 获得用人员所在服务网点编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 17:44
     */
    List<Long> listOwnBranchId(Long userId, Long corpId);

    /**
     * 获得人员所在服务网点的下级网点编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 17:45
     */
    List<Long> listOwnLowerBranchId(Long userId, Long corpId);

    /**
     * 获得人员所在服务网点及下级网点编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 17:45
     */
    List<Long> listOwnAndLowerBranchId(Long userId, Long corpId);

    /**
     * 获得所有下级网点编号列表
     *
     * @param branchIdList
     * @return
     * @author zgpi
     * @date 2020/6/5 09:28
     **/
    List<Long> listLowerBranchId(List<Long> branchIdList);

    /**
     * 获得用人员所在服务网点列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/5/13 09:11
     **/
    List<ServiceBranch> listOwnBranch(Long userId, Long corpId);
}
