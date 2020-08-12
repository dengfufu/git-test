package com.zjft.usp.anyfix.work.attention.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.attention.dto.WorkAttentionDto;
import com.zjft.usp.anyfix.work.attention.mapper.WorkAttentionMapper;
import com.zjft.usp.anyfix.work.attention.model.WorkAttention;
import com.zjft.usp.anyfix.work.attention.service.WorkAttentionService;
import com.zjft.usp.anyfix.work.follow.dto.WorkFollowDto;
import com.zjft.usp.anyfix.work.follow.service.WorkFollowService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 工单关注表 服务实现类
 * </p>
 *
 * @author cxd
 * @since 2020-04-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkAttentionServiceImpl extends ServiceImpl<WorkAttentionMapper, WorkAttention> implements WorkAttentionService {
    @Autowired
    private WorkFollowService workFollowService;
    /**
     * 根据用户查询所有工单关注列表
     * @date 2020/4/14
     * @param userId
     * @return void
     */
    @Override
    public List<WorkAttention> queryWorkAttentionByUserId(Long userId) {
        if(userId == null){
            throw new AppException("参数错误，请检查！");
        }
        return this.list(new QueryWrapper<WorkAttention>().eq("user_id",userId));
    }

    /**
     * 根据工单id和企业id查询所有工单关注列表
     * @date 2020/4/14
     * @param workId corpId
     * @return void
     */
    @Override
    public List<WorkAttention> queryByWorkIdAndCorpId(Long workId,Long corpId) {
        if(workId == null){
            throw new AppException("参数错误，请检查！");
        }
        return this.list(new QueryWrapper<WorkAttention>().eq("work_id",workId).eq("corp_id",corpId));
    }

    /**
     * 根据工单id查询所有工单关注列表
     * @date 2020/4/14
     * @param workId corpId
     * @return void
     */
    @Override
    public List<WorkAttention> queryByWorkId(Long workId) {
        if(workId == null){
            throw new AppException("参数错误，请检查！");
        }
        return this.list(new QueryWrapper<WorkAttention>().eq("work_id",workId));
    }

    /**
     * 添加工单关注
     * @date 2020/4/14
     * @param workAttentionDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void addWorkAttention(WorkAttentionDto workAttentionDto, UserInfo userInfo, ReqParam reqParam) {
        List<WorkAttention> workAttentionList = this.queryByWorkIdAndCorpId(workAttentionDto.getWorkId(),reqParam.getCorpId());
        if (CollectionUtil.isNotEmpty(workAttentionList)) {
            return;
        }else {
            WorkAttention workAttention = new WorkAttention();
            BeanUtils.copyProperties(workAttentionDto, workAttention);
            workAttention.setUserId(userInfo.getUserId());
            workAttention.setId(KeyUtil.getId());
            workAttention.setOperator(userInfo.getUserId());
            workAttention.setOperateTime(DateTime.now());
            this.save(workAttention);

            // 添加跟进记录
            WorkFollowDto workFollowDto = new WorkFollowDto();
            workFollowDto.setWorkId(workAttentionDto.getWorkId());
            workFollowDto.setReferId(workAttention.getId());
            workFollowDto.setFollowRecord("关注工单");
            workFollowService.addWorkFollow(workFollowDto,userInfo.getUserId(),reqParam.getCorpId());
        }
    }

    /**
     * 根据工单编号删除工单关注
     *
     * @param workId
     */
    @Override
    public void deleteByWorkId(Long workId,Long userId,Long corpId) {
        List<WorkAttention> workList = this.queryByWorkIdAndCorpId(workId,corpId);
        if (CollectionUtil.isNotEmpty(workList)) {
            if (workId != null&&userId != null) {
                List<WorkAttention> list = this.list(new QueryWrapper<WorkAttention>().eq("work_id", workId).eq("corp_id",corpId));
                // 删除工单关注
                this.remove(new UpdateWrapper<WorkAttention>().eq("work_id", workId).eq("corp_id",corpId));
                // 添加跟进记录
                WorkFollowDto workFollowDto = new WorkFollowDto();
                workFollowDto.setWorkId(workId);
                if (CollectionUtil.isNotEmpty(list)) {
                    for (WorkAttention workAttention: list){
                        workFollowDto.setReferId(workAttention.getId());
                    }
                }
                workFollowDto.setFollowRecord("取消关注工单");
                workFollowService.addWorkFollow(workFollowDto,userId,corpId);
            }else {
                throw new AppException("参数错误，请检查！");
            }
        }else {
            return;
        }
    }
}
