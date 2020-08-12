package com.zjft.usp.uas.corp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.CorpDto;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.corp.dto.CorpUserInfoDto;
import com.zjft.usp.uas.corp.filter.CorpUserFilter;
import com.zjft.usp.uas.corp.model.CorpUser;

import java.util.List;
import java.util.Map;

/**
 * @author canlei
 * @date 2019-08-04
 */
public interface CorpUserService extends IService<CorpUser> {

    /**
     * 获取用户企业列表
     *
     * @param corpUserDto
     * @return
     */
    List<CorpDto> listCorpInfo(CorpUserDto corpUserDto);

    /**
     * 判断用户是否已在企业
     *
     * @param userId
     * @param corpId
     * @return
     */
    boolean ifUserInCorp(long userId, long corpId);

    /**
     * 账号已经被被其他人绑定
     *
     * @param account
     * @param corpId
     * @param userId
     * @return
     */
    boolean ifAccountInCorp(String account, long corpId, long userId);

    /**
     * 将用户加入企业
     *
     * @param userId
     * @param corpId
     * @param userName
     */
    void addCorpUser(long userId, long corpId, String userName);

    /**
     * 设置管理员
     * @date 2020/6/9
     * @param userId
     * @param corpId
     * @return void
     */
    void setAdmin(long userId, long corpId);

    /**
     * 分页查询企业人员
     *
     * @param corpUserFilter
     * @return
     */
    ListWrapper<CorpUserDto> query(CorpUserFilter corpUserFilter);

    /**
     * 根据企业id分页查询企业所有人员(无过滤)
     *
     * @param corpUserFilter
     * @return
     */
    ListWrapper<CorpUserDto> queryCorpUser(CorpUserFilter corpUserFilter);

    /**
     * 根据企业获取用户
     *
     * @param corpId
     * @return
     */
    Map<Long, CorpUser> mapUserIdAndCorpUser(Long corpId);

    /**
     * 根据企业获取用户映射
     *
     * @param corpId
     * @return
     */
    Map<Long, String> mapUserIdAndName(Long corpId);

    /**
     * 根据用户获取企业
     *
     * @param userId
     * @return
     */
    Map<Long, CorpUser> mapCorpIdByUser(Long userId);

    /**
     * 根据用户查询企业编号List
     *
     * @param userId
     * @return
     */
    List<Long> listCorpIdByUserId(Long userId);

    /**
     * 根据企业查询人员编号列表
     *
     * @param corpId
     * @return
     */
    List<Long> listUserIdByCorpId(Long corpId);

    /**
     * 根据企业编号获取企业用户
     *
     * @param corpUserDto
     * @param reqParam
     * @return
     */
    List<CorpUserDto> getCorpUserByCorpId(CorpUserDto corpUserDto, ReqParam reqParam);

    /**
     * 添加企业员工
     *
     * @param corpUserDto
     */
    void addCorpUser(CorpUserDto corpUserDto);

    /**
     * 更新企业员工
     *
     * @param corpUserDto
     */
    void updateCorpUser(CorpUserDto corpUserDto);

    /**
     * 删除企业员工
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 09:59
     **/
    void delCorpUser(Long corpId, Long userId, Long operatorId, String clientId);

    /**
     * 隐藏企业员工
     * @date 2020/6/8
     * @param corpUserFilter
     * @return void
     */
    void hiddenCorpUser(CorpUserFilter corpUserFilter);

    /**
     * 根据企业编号列表获得用户ID与名称映射
     *
     * @param corpIdList
     * @return
     * @author zgpi
     * @date 2019/10/12 3:24 下午
     **/
    Map<Long, String> mapUserIdAndNameByCorpIdList(List<Long> corpIdList);

    /**
     * 根据userId查询企业列表
     * @date 2020/5/27
     * @param userIdList
     * @return java.util.List<java.lang.Long>
     */
    Map<Long, List<Long>> listCorpIdListByUserId(List<Long> userIdList);

    /**
     * 根据人员编号和企业编号获得企业人员信息
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/10/30 10:04
     **/
    CorpUser findCorpUserByUserIdAndCorpId(Long userId, Long corpId);

    /**
     * 根据企业编号和人员编号List获取人员编号和名称映射以及头像信息
     *
     * @param userIdList
     * @param corpId
     * @return
     */
    Map<Long, CorpUserInfoDto> mapCorpUserInfoByUserIdList(List<Long> userIdList, Long corpId);

    /**
     * 模糊查询企业人员列表
     *
     * @param corpUserFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 08:40
     **/
    List<CorpUserDto> matchCorpUser(CorpUserFilter corpUserFilter);

    /**
     * 模糊查询企业人员姓名列表
     *
     * @param corpUserFilter
     * @return
     **/
    List<CorpUserDto> matchCorpUserNames(CorpUserFilter corpUserFilter);
    
    /**
     * 模糊查询企业人员列表（用于筛选）
     * @date 2020/6/9
     * @param corpUserFilter
     * @return java.util.List<com.zjft.usp.uas.corp.dto.CorpUserDto>
     */
    List<CorpUserDto> matchCorpUserByCorp(CorpUserFilter corpUserFilter);

    /**
     * 根据手机查询用户
     */
    CorpUserInfoDto getUserInfoByMobile(String mobile);

    /**
     * 根据企业编号和人员编号列表获得企业员工账号列表
     *
     * @param corpUserFilter
     * @return
     * @author zgpi
     * @date 2020/4/7 20:56
     */
    List<Long> listUserIdByAccountList(CorpUserFilter corpUserFilter);

    /**
     * 用户是否是企业人员
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/13 09:59
     **/
    Boolean ifCorpUser(Long corpId, Long userId);
}
