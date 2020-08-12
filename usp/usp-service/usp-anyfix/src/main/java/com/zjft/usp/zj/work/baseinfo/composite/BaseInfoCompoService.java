package com.zjft.usp.zj.work.baseinfo.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.device.atm.dto.DeviceDto;
import com.zjft.usp.zj.device.atm.filter.DeviceFilter;
import com.zjft.usp.zj.work.baseinfo.dto.*;
import com.zjft.usp.zj.work.baseinfo.filter.*;

import java.util.List;
import java.util.Map;

/**
 * 基础数据聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:31
 **/
public interface BaseInfoCompoService {

    /**
     * 获得CASE基本信息
     * 工程师信息、当前时间等
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/26 16:10
     */
    CaseBaseDto findCaseBase(UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得CASE查询的基本信息
     * 分布列表，权限列表
     *
     * @param serviceBranch
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/13 11:27
     */
    CaseBaseDto findCaseQueryBase(String serviceBranch, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得工程师
     *
     * @param serviceBranchName
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/26 16:10
     */
    CaseBaseDto findEngineer(String serviceBranchName, UserInfo userInfo, ReqParam reqParam);

    /**
     * CASE类型列表
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 09:26
     */
    List<String> listCaseType(UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得CASE子类型列表
     *
     * @param caseType
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 10:40
     */
    Map<Integer, String> listCaseSubType(String caseType, UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询用户权限范围内的服务站列表
     *
     * @param serviceBranchFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 13:38
     */
    ListWrapper<ServiceBranchDto> queryServiceBranch(ServiceBranchFilter serviceBranchFilter,
                                                     UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询设备网点列表
     *
     * @param deviceBranchFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 21:48
     */
    ListWrapper<DeviceBranchDto> queryDeviceBranch(DeviceBranchFilter deviceBranchFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询总行列表
     *
     * @param bankFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 20:19
     */
    ListWrapper<BankDto> queryHeadBank(BankFilter bankFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询分行列表
     *
     * @param bankFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/23 21:48
     */
    ListWrapper<BankDto> querySubBank(BankFilter bankFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询机器型号列表
     *
     * @param deviceModelFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/24 20:25
     */
    ListWrapper<DeviceModelDto> queryDeviceModel(DeviceModelFilter deviceModelFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获取交通工具列表
     *
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, String> listTraffic(UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据客户获取软件版本列表，包含软件版本、sp软件版本、bv软件版本和其他软件版本
     *
     * @param customId
     * @param modelId
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<String, List<SoftVersionDto>> listSoftVersion(String customId, String modelId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询机器型号列表
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/25 14:17
     */
    List<DeviceModelDto> listDeviceModel(UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询机器列表
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/25 10:56
     */
    List<DeviceDto> listDevice(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得机器详情
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 20:26
     */
    DeviceDto findDeviceDetail(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得保修状态列表
     *
     * @param serviceBranchName
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 20:50
     */
    List<WarrantyStatusDto> listWarrantyStatus(String serviceBranchName, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据制造号查找终端号
     *
     * @param modelId
     * @param serials
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 09:42
     */
    String findDeviceCodesBySerials(String modelId, String serials, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据终端号查找制造号
     *
     * @param deviceCodes
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 09:42
     */
    String findSerialsByDeviceCodes(String deviceCodes, UserInfo userInfo, ReqParam reqParam);

    /**
     * 检查设备状态
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/28 11:40
     */
    DeviceCheckDto checkDeviceStatus(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询是否已存在CASE
     *
     * @param bankCode
     * @param branchName
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/26 11:10
     */
    Boolean ifExistCase(String bankCode, String branchName, UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询工程师列表
     *
     * @param engineerFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/3/27 11:21
     */
    ListWrapper<EngineerDto> queryEngineer(EngineerFilter engineerFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询跟单规则
     *
     * @param workTypeName
     * @param traceRequired
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<TraceRuleDto> listTraceRule(String workTypeName, String traceRequired, UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询紫金公司服务主管列表(含400)
     *
     * @param userFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-29
     */
    ListWrapper<UserDto> queryManager(UserFilter userFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获取是否人为损坏映射Map
     *
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<Integer, String> listManMade(UserInfo userInfo, ReqParam reqParam);

    /**
     * 获取巡检处理方式映射Map
     *
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<Integer, String> listDealWay(UserInfo userInfo, ReqParam reqParam);
}
