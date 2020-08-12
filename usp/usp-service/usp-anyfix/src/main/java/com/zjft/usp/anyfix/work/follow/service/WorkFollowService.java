package com.zjft.usp.anyfix.work.follow.service;

import com.zjft.usp.anyfix.work.follow.dto.WorkFollowDto;
import com.zjft.usp.anyfix.work.follow.model.WorkFollow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 * 跟进记录表 服务类
 * </p>
 *
 * @author cxd
 * @since 2020-05-11
 */
public interface WorkFollowService extends IService<WorkFollow> {
    /**
     * 根据工单id查询跟进记录列表
     * @date 2020/5/12
     * @param workId
     * @return java.util.List<com.zjft.usp.anyfix.work.follow.model.WorkFollow>
     */
    List<WorkFollowDto> queryByWorkIdAndCorpId(Long workId,Long corpId);
    /**
     * 添加跟进记录
     * @date 2020/5/11
     * @param workFollowDto
     * @return void
     */
   void addWorkFollow(WorkFollowDto workFollowDto, Long userId,Long corpId);
}
