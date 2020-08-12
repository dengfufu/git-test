package com.zjft.usp.anyfix.work.follow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.work.follow.dto.WorkFollowDto;
import com.zjft.usp.anyfix.work.follow.model.WorkFollow;
import com.zjft.usp.anyfix.work.follow.mapper.WorkFollowMapper;
import com.zjft.usp.anyfix.work.follow.service.WorkFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.support.model.WorkSupport;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 跟进记录表 服务实现类
 * </p>
 *
 * @author cxd
 * @since 2020-05-11
 */
@Service
public class WorkFollowServiceImpl extends ServiceImpl<WorkFollowMapper, WorkFollow> implements WorkFollowService {
    /**
     * 根据工单id查询跟进记录列表
     * @date 2020/5/12
     * @param workId
     * @return List<WorkSupportDto>
     */
    @Override
    public List<WorkFollowDto> queryByWorkIdAndCorpId(Long workId,Long corpId) {
        if(workId == null){
            throw new AppException("参数错误，请检查！");
        }
        List<WorkFollow> workFollowList = this.list(new QueryWrapper<WorkFollow>().eq("work_id",workId)
                .eq("corp_id",corpId).orderByAsc("operate_time"));
        List<WorkFollowDto> workFollowDtolist = new ArrayList<>();;
        if (CollectionUtil.isNotEmpty(workFollowList)) {
            WorkFollowDto workFollowDto;
            for (WorkFollow workFollow : workFollowList) {
                workFollowDto = new WorkFollowDto();
                BeanUtils.copyProperties(workFollow, workFollowDto);
                workFollowDtolist.add(workFollowDto);
            }
        }
        return workFollowDtolist;
    }

    /**
     * 添加跟进记录
     * @date 2020/5/11
     * @param workFollowDto
     * @return void
     */
    @Override
    public void addWorkFollow(WorkFollowDto workFollowDto, Long userId,Long corpId) {
        WorkFollow workFollow = new WorkFollow();
        BeanUtils.copyProperties(workFollowDto, workFollow);
        workFollow.setId(KeyUtil.getId());
        workFollow.setOperator(userId);
        workFollow.setCorpId(corpId);
        workFollow.setOperateTime(DateTime.now());
        this.save(workFollow);
    }
}
