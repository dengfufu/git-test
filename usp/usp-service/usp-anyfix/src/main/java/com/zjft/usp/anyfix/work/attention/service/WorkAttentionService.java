package com.zjft.usp.anyfix.work.attention.service;

import com.zjft.usp.anyfix.work.attention.dto.WorkAttentionDto;
import com.zjft.usp.anyfix.work.attention.model.WorkAttention;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 * 工单关注表 服务类
 * </p>
 *
 * @author cxd
 * @since 2020-04-15
 */
public interface WorkAttentionService extends IService<WorkAttention> {
    /**
     * 根据用户查询所有工单关注列表
     * @date 2020/4/14
     * @param userId
     * @return void
     */
    List<WorkAttention> queryWorkAttentionByUserId(Long userId);

    /**
     * 根据工单id和企业id查询所有工单关注列表
     * @date 2020/4/15
     * @param workId  corpId
     * @return java.util.List<com.zjft.usp.anyfix.work.attention.model.WorkAttention>
     */
    List<WorkAttention> queryByWorkIdAndCorpId(Long workId,Long corpId);

    /**
     * 根据工单id查询所有工单关注列表
     * @date 2020/5/28
     * @param workId
     * @return java.util.List<com.zjft.usp.anyfix.work.attention.model.WorkAttention>
     */
    List<WorkAttention> queryByWorkId(Long workId);

    /**
     * 添加工单关注
     * @date 2020/4/14
     * @param workAttentionDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    void addWorkAttention(WorkAttentionDto workAttentionDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据工单编号删除工单关注
     * @date 2020/4/14
     * @param workId
     * @return void
     */
    void deleteByWorkId(Long workId,Long userId,Long corpId);
}
