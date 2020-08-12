package com.zjft.usp.anyfix.work.support.service;

import com.zjft.usp.anyfix.corp.user.dto.CorpUserDto;
import com.zjft.usp.anyfix.work.support.dto.WorkSupportDto;
import com.zjft.usp.anyfix.work.support.model.WorkSupport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 * 技术支持 服务类
 * </p>
 *
 * @author cxd
 * @since 2020-04-21
 */
public interface WorkSupportService extends IService<WorkSupport> {
    /**
     * 根据id查询列表
     * @date 2020/4/29
     * @param Id
     * @return com.zjft.usp.anyfix.work.support.model.WorkSupport
     */
    WorkSupport queryById(Long Id);
    /**
     * 根据用户查询所有技术支持的工单列表
     * @date 2020/4/14
     * @param userId
     * @return void
     */
    List<WorkSupport> queryWorkSupportByUserId(Long userId);

    /**
     * 根据用户id和工单id查询所有工单技术支持列表
     * @date 2020/4/15
     * @param workId
     * @param userId
     * @return java.util.List<com.zjft.usp.anyfix.work.attention.model.WorkAttention>
     */
    List<WorkSupport> queryByWorkIdAndUserId(Long workId,Long userId);

    /**
     * 根据工单id和企业id查询所有工单技术支持列表
     * @date 2020/4/22
     * @param workId corpId
     * @return java.util.List<com.zjft.usp.anyfix.work.support.model.WorkSupport>
     */
    List<WorkSupportDto> queryByWorkIdAndCorpId(Long workId,Long corpId);

    /**
     * 添加工单技术支持
     * @date 2020/4/21
     * @param workSupportDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void addWorkSupport(WorkSupportDto workSupportDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据工单编号删除工单技术支持
     * @date 2020/4/21
     * @param workId
     * @param userId
     * @return void
     */
    void deleteByWorkId(Long workId,Long userId);

    /**
     * 关闭工单技术支持
     * @date 2020/4/22
     * @param workSupportDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void finishWorkSupport(WorkSupportDto workSupportDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据企业获取技术支持人员列表
     * @date 2020/5/14
     * @param workSupportDto
     * @return java.util.List<com.zjft.usp.anyfix.corp.user.dto.CorpUserDto>
     */
    List<CorpUserDto> queryListCorpUser(WorkSupportDto workSupportDto,ReqParam reqParam);
}
