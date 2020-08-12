package com.zjft.usp.anyfix.corp.branch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.branch.dto.ServiceBranchDto;
import com.zjft.usp.anyfix.corp.branch.filter.ServiceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务网点表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
public interface ServiceBranchService extends IService<ServiceBranch> {

    /**
     * 分页查询设备网点
     *
     * @param serviceBranchFilter
     * @param userInfo
     * @return
     */
    ListWrapper<ServiceBranchDto> query(ServiceBranchFilter serviceBranchFilter, UserInfo userInfo);

    /**
     * 根据网点编号查询
     *
     * @param branchId
     * @return
     */
    ServiceBranchDto findByBranchId(Long branchId);

    /**
     * 添加服务网点
     *
     * @param serviceBranchDto
     * @return
     * @author zgpi
     * @date 2020/3/10 20:25
     */
    Long addBranch(ServiceBranchDto serviceBranchDto);

    /**
     * 修改服务网点
     *
     * @param serviceBranchDto
     * @return
     * @author zgpi
     * @date 2020/3/10 20:25
     */
    void updateBranch(ServiceBranchDto serviceBranchDto);

    /**
     * 根据服务商编号获取服务网点映射
     *
     * @param serviceCorp
     * @return
     */
    Map<Long, ServiceBranch> mapServiceBranchByCorp(Long serviceCorp);

    /**
     * 服务网点列表
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:14
     */
    List<ServiceBranch> listServiceBranch(ServiceBranchFilter serviceBranchFilter);

    List<ServiceBranch> listServiceBranchByServiceUserId(Long userId);

    /**
     * 根据服务商企业ID列表获得服务网点ID与名称映射
     *
     * @param corpIdList
     * @return
     * @author zgpi
     * @date 2019/10/18 9:23 上午
     **/
    Map<Long, String> mapServiceBranchByCorpIdList(List<Long> corpIdList);

    /**
     * 模糊查询服务网点
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 10:22
     **/
    List<ServiceBranch> matchServiceBranch(ServiceBranchFilter serviceBranchFilter);

    /**
     * 获得所有上级网点列表（包括自己）
     *
     * @param branchId
     * @return
     * @author zgpi
     * @date 2020/3/9 15:59
     */
    List<ServiceBranch> listAllUpperServiceBranch(Long branchId);

    /**
     * 分页查询服务网点下级列表
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:34
     */
    ListWrapper<ServiceBranchDto> queryAllLowerServiceBranch(ServiceBranchFilter serviceBranchFilter);

    /**
     * 分页查询服务网点同级列表
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:34
     */
    ListWrapper<ServiceBranchDto> querySameServiceBranch(ServiceBranchFilter serviceBranchFilter);


    /**
     * 匹配网点编号和名字
     *
     * @param serviceBranchIdList
     * @return
     */
    Map<Long, String> mapIdAndNameByBranchIdList(List<Long> serviceBranchIdList);

    /**
     * 根据网点ID列表获得服务网点ID与名称映射
     *
     * @param branchIdList
     * @return
     * @author zgpi
     * @date 2020/4/21 10:23
     **/
    Map<Long, String> mapServiceBranchByBranchIdList(Collection<Long> branchIdList);

    /**
     * 根据userId查询服务网点
     * @param userId
     * @return
     */
    List<ServiceBranch> getBranchByUserId(Long userId);
}
