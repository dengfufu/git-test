package com.zjft.usp.uas.corp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.*;
import com.zjft.usp.uas.corp.filter.CorpRegistryFilter;
import com.zjft.usp.uas.corp.model.CorpRegistry;

import java.util.List;
import java.util.Map;

/**
 * @author canlei
 * 2019-08-04
 */
public interface CorpRegistryService extends IService<CorpRegistry> {

    /**
     * 分页查询企业
     *
     * @param corpRegistryFilter
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2019/12/18 16:48
     **/
    ListWrapper<CorpDto> query(CorpRegistryFilter corpRegistryFilter, Long curUserId);

    /**
     * 企业注册
     *
     * @param corpRegistryDto
     * @param reqParam
     * @param curUserId
     * @return
     */
    Long corpRegistry(CorpRegistryDto corpRegistryDto, ReqParam reqParam, Long curUserId);

    /**
     * 添加企业
     *
     * @param corpRegistry
     * @return
     * @author zgpi
     * @date 2020/1/16 10:54
     **/
    Long addCorp(CorpRegistry corpRegistry);

    /**
     * 企业详情
     *
     * @param corpId
     * @return
     */
    CorpDto queryCorpInfo(Long corpId, Long curUserId);

    /**
     * 修改企业管理密码
     *
     * @param corpRegistryDto
     * @param reqParam
     * @param curUserId
     */
    void changePassword(CorpRegistryDto corpRegistryDto, ReqParam reqParam, Long curUserId, String clientId) throws Exception;

    /**
     * 修改企业注册信息
     *
     * @param corpRegistryDto
     * @param reqParam
     * @param curUserId
     */
    void updateCorpReg(CorpRegistryDto corpRegistryDto, ReqParam reqParam, Long curUserId, String clientId);

    /**
     * 根据条件查询企业
     *
     * @param corpRegistryFilter
     * @param curUserId
     * @return
     */
    List<CorpDto> listCorpByFilter(CorpRegistryFilter corpRegistryFilter, Long curUserId);

    /**
     * 获取人员规模列表
     *
     * @return
     */
    List<StaffScopeDto> listStaffScope();

    /**
     * 判断公司名称是否存在
     *
     * @param corpName
     * @return boolean
     */
    boolean ifNameExists(String corpName);

    /**
     * 获取企业注册的映射
     *
     * @param corpIdList
     * @return boolean
     */
    Map<Long, CorpRegistry> mapCorpIdAndRegistry(List<Long> corpIdList);

    /**
     * 获得企业ID与简称映射
     *
     * @param corpIdList
     * @return
     * @author zgpi
     * @date 2020/6/3 20:08
     **/
    Map<Long, String> mapCorpIdAndShortName(List<Long> corpIdList);

    /**
     * 根据公钥获取解密后的密码
     *
     * @param encryptedPw
     * @param publicKey
     * @return
     */
    String decipherPasswd(String encryptedPw, String publicKey);

    /**
     * 获取企业名称列表
     *
     * @param corpIdList
     * @return
     */
    Map<Long, String> getCorpNameList(List<Long> corpIdList);

    /**
     * 根据企业编号list获取企业信息
     *
     * @param corpIdList
     * @return
     */
    List<CorpDto> listCorpByIdList(List<Long> corpIdList);

    /**
     * 根据条件查询企业编号列表
     *
     * @param corpRegistryFilter
     * @return
     */
    List<Long> listCorpIdByFilter(CorpRegistryFilter corpRegistryFilter);

    /**
     * 查询用户所在企业的编号和名称映射
     *
     * @param userId
     * @return
     */
    List<CorpNameDto> listCorpNameMapByUserId(Long userId);

    /**
     * 模糊查询企业
     *
     * @param corpRegistryFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 10:52
     **/
    List<CorpRegistry> matchCorp(CorpRegistryFilter corpRegistryFilter);

    /**
     * 企业信息带有地址
     *
     * @param corpId
     * @return
     */
    CorpDto getCorpInfoWithAddress(Long corpId);

    /**
     * 根据名称获取企业信息
     *
     * @param corpName
     * @return
     */
    List<CorpRegistry> getCorpByName(String corpName);

    /**
     * 注册企业并设置为系统委托商
     *
     * @param jsonString
     * @return
     */
    Long feignCreateDemander(String jsonString);

    /**
     * 根据企业编号查询企业信息带有注册人姓名
     * @param corpId
     * @return
     */
    CorpDto getCorpDetailWithRegUserNameById(Long corpId);

    /**
     * 根据corpId查询企业
     * @date 2020/6/11
     * @param corpId
     * @return com.zjft.usp.uas.corp.model.CorpRegistry
     */
    CorpRegistry getCorpInfoById(Long corpId);

    /**
     * 自动认证
     * @date 2020/6/14
     * @param corpId
     * @return void
     */
    void addVerifyByCorpId(Long corpId);

    String getMaxCorpCode();

    void setMaxCorpCode(Long corpId);

    /**
     * 根据用户id查询企业信息和用户权限
     * @param userId
     * @author xpwu
     * @return
     */
    List<CorpRoleDto> listCorpRoleByUserId(Long userId);

    /**
     * 获取企业系统编号与企业编号的映射
     *
     * @param corpIdList
     * @return
     */
    Map<Long, String> mapCorpIdAndCode(List<Long> corpIdList);
}
