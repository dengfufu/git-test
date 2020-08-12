package com.zjft.usp.anyfix.work.review.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.review.dto.WorkReviewDto;
import com.zjft.usp.anyfix.work.review.model.WorkReview;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 * 客户回访记录 服务类
 * </p>
 *
 * @author ljzhu
 */
public interface WorkReviewService extends IService<WorkReview> {

    /**
     * 添加客户回访记录
     * @param workReviewDto
     * @param userInfo
     * @param reqParam
     * @author ljzhu
     */
    void addWorkReview(WorkReviewDto workReviewDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询某工单的客户回访记录
     * @param workReviewDto
     * @param userInfo
     * @param reqParam
     * @author ljzhu
     */
    List<WorkReviewDto> listWorkReviewsByWorkId(WorkReviewDto workReviewDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 删除一条客户回访记录
     * @param workReviewDto
     */
    void delWorkReview(WorkReviewDto workReviewDto);

    /**
     * 删除一个工单的所有客户回访记录
     * @param workReviewDto
     */
    void delWorkReviewByWorkId(WorkReviewDto workReviewDto);
}
