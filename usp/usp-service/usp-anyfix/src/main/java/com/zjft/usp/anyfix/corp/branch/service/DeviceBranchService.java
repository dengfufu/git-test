package com.zjft.usp.anyfix.corp.branch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.branch.dto.DeviceBranchDto;
import com.zjft.usp.anyfix.corp.branch.filter.DeviceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.model.DeviceBranch;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备网点表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-24
 */
public interface DeviceBranchService extends IService<DeviceBranch> {

    ListWrapper<DeviceBranchDto> queryDeviceBranch(DeviceBranchFilter deviceBranchFilter);

    List<DeviceBranch> listDeviceBranch(DeviceBranchFilter deviceBranchFilter);

    /**
     * 分页查询客户设备网点列表
     *
     * @param deviceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/5 16:48
     **/
    ListWrapper<DeviceBranchDto> queryDeviceBranchByCustom(DeviceBranchFilter deviceBranchFilter);

    DeviceBranchDto findDtoById(Long branchId);

    /**
     * 添加设备网点
     *
     * @param deviceBranchDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/12/18 10:23
     **/
    Long addDeviceBranch(DeviceBranchDto deviceBranchDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改设备网点
     *
     * @param deviceBranchDto
     * @return
     * @author zgpi
     * @date 2019/12/18 10:24
     **/
    void updateDeviceBranch(DeviceBranchDto deviceBranchDto);

    /**
     * 删除设备网点
     *
     * @param branchId
     * @return
     * @author zgpi
     * @date 2019/12/18 10:24
     **/
    void delDeviceBranch(Long branchId);

    /**
     * 下级网点数
     *
     * @param upperBranchId
     * @return
     * @author zgpi
     * @date 2019/11/5 18:23
     **/
    Integer countByUpperId(Long upperBranchId);

    /**
     * 根据企业编号获取设备网点映射
     * @param customCorp
     * @return
     */
    Map<Long, DeviceBranch> mapDeviceBranchByCorp(Long customCorp);

    Map<Long, String> mapDeviceBranchNameByCorp(Long customCorp);


    /**
     * 根据客户企业ID列表获得设备网点ID与名称映射
     *
     * @author zgpi
     * @date 2019/10/18 9:28 上午
     * @param corpIdList
     * @return
     **/
    Map<Long, String> mapDeviceBranchByCorpIdList(List<Long> corpIdList);

    /**
     * 根据客户编号列表获得设备网点ID与名称映射
     *
     * @param customIdList
     * @return
     * @author zgpi
     * @date 2020/1/16 14:53
     **/
    Map<Long, String> mapCustomDeviceBranchByCustomIdList(List<Long> customIdList);

    /**
     * 根据网点编号列表获得编号与名称映射
     *
     * @param branchIdList 网点编号列表
     * @return
     * @author zgpi
     * @date 2020/2/14 14:09
     */
    Map<Long, String> mapIdAndNameByIdList(List<Long> branchIdList);

    /**
     * 模糊查询设备网点
     *
     * @param deviceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 10:22
     **/
    List<DeviceBranchDto> matchDeviceBranch(DeviceBranchFilter deviceBranchFilter);

    /**
     * 模糊查询相关企业的设备网点
     *
     * @param deviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/2/14 16:19
     */
    List<DeviceBranchDto> matchRelateDeviceBranch(DeviceBranchFilter deviceBranchFilter);

    /**
     * 获得设备网点全称
     *
     * @param deviceBranch
     * @return
     **/
    String buildFullBranchName(DeviceBranch deviceBranch);

    /**
     * 批量增加设备网点
     */
    Map<String,Long> batchAddDeviceBranch(List<DeviceBranch> deviceBranches);

    /**
     * 根据网点ID列表获得设备网点ID与名称映射
     *
     * @param branchIdList
     * @return
     * @author zgpi
     * @date 2020/4/21 10:23
     **/
    Map<Long, String> mapDeviceBranchByBranchIdList(Collection<Long> branchIdList);

    /**
     * 查询上次设备网点
     * @param deviceFilter
     * @return
     */
    List<DeviceBranch> selectUpperBranchList(DeviceBranchFilter deviceFilter);
}
