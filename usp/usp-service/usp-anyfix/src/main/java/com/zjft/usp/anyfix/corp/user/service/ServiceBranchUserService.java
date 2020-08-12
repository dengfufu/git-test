package com.zjft.usp.anyfix.corp.user.service;

import com.zjft.usp.anyfix.corp.user.dto.CorpUserDto;
import com.zjft.usp.anyfix.corp.user.dto.ServiceBranchUserDto;
import com.zjft.usp.anyfix.corp.user.filter.ServiceBranchUserFilter;
import com.zjft.usp.anyfix.corp.user.model.ServiceBranchUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务网点人员表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
public interface ServiceBranchUserService extends IService<ServiceBranchUser> {

    /**
     * 查询网点所有人员列表
     *
     * @param branchId
     * @return
     */
    List<ServiceBranchUser> listByBranchId(Long branchId);

    /**
     * 查询网点所有人员
     *
     * @param serviceBranchUserFilter
     * @return
     */
    List<ServiceBranchUserDto> listDtoBy(ServiceBranchUserFilter serviceBranchUserFilter);

    /**
     * 查询网点所有人员id列表
     *
     * @param branchId
     * @return
     */
    List<Long> listUserIdsByBranchId(Long branchId);

    /**
     * 添加
     *
     * @param serviceBranchUserDto
     * @param reqParam
     */
    void addBranchUser(ServiceBranchUserDto serviceBranchUserDto, ReqParam reqParam);

    /**
     * 条件分页查询
     *
     * @param serviceBranchUserFilter
     * @return
     */
    ListWrapper<ServiceBranchUserDto> query(ServiceBranchUserFilter serviceBranchUserFilter);

    /**
     * 删除网点人员
     *
     * @param userId
     * @param branchId
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/11/28 09:52
     **/
    void delBranchUser(Long userId, Long branchId, ReqParam reqParam);

    /**
     * 模糊查询人员
     *
     * @param serviceBranchUserFilter
     * @return
     * @author zgpi
     * @date 2019/12/23 14:51
     **/
    List<CorpUserDto> matchCorpUser(ServiceBranchUserFilter serviceBranchUserFilter);

    /**
     * 匹配可选人员
     *
     * @param serviceBranchUserFilter
     * @return
     */
    List<CorpUserDto> matchAvailable(ServiceBranchUserFilter serviceBranchUserFilter);

    /**
     * 展示可选的人员列表
     *
     * @param serviceBranchUserFilter
     * @return
     */
    ListWrapper<CorpUserDto> queryAvailable(ServiceBranchUserFilter serviceBranchUserFilter);

    /**
     * 删除服务网点人员
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/2/25 17:19
     */
    void delBranchUserByCorp(Long userId, Long corpId, Long currentUserId, String clientId);

    /**
     * 人员编号与服务网点名称(带省份)映射
     *
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2020/3/13 09:46
     */
    Map<Long, String> mapUserIdAndServiceBranchNames(List<Long> userIdList);

    /**
     * 根据条件获得人员列表
     *
     * @param serviceBranchUserFilter
     * @return
     * @author zgpi
     * @date 2020/3/13 10:30
     */
    List<Long> listUserIdByFeign(ServiceBranchUserFilter serviceBranchUserFilter);

    /**
     * 根据人员获得所在服务网点
     *
     * @param userid
     * @return
     */
    List<Long> listBranchsByUserId(Long userid);

    /**
     * 查询人员的服务网点信息
     *
     * @param userId
     * @return
     */
    String getBranchNamesByUserId(Long userId);

    /**
     * 是否该网点人员
     *
     * @param branchId
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/4/24 14:47
     **/
    boolean ifBranchUser(Long branchId, Long userId);
}
