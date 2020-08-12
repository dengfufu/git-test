package com.zjft.usp.anyfix.work.review.service.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.review.dto.WorkReviewDto;
import com.zjft.usp.anyfix.work.review.mapper.WorkReviewMapper;
import com.zjft.usp.anyfix.work.review.model.WorkReview;
import com.zjft.usp.anyfix.work.review.service.WorkReviewService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * <p>
 * 客户回访记录 服务实现类
 * </p>
 *
 * @author ljzhu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkReviewServiceImpl extends ServiceImpl<WorkReviewMapper, WorkReview> implements WorkReviewService {

    @Autowired
    private UasFeignService uasFeignService;

    @Autowired
    private WorkDealService workDealService;

    @Override
    public void addWorkReview(WorkReviewDto workReviewDto, UserInfo userInfo, ReqParam reqParam) {
        if (LongUtil.isZero(workReviewDto.getWorkId())) {
            throw new AppException("工单编号不能为空");
        }
        workReviewDto.setId(KeyUtil.getId());
        workReviewDto.setCorpId(reqParam.getCorpId());
        workReviewDto.setOperator(userInfo.getUserId());
        workReviewDto.setOperatTime(DateUtil.date().toTimestamp());
        this.save(workReviewDto);

        // 只有解决了才认为是[已回访]
//        if (!StringUtils.isEmpty(workReviewDto.getIsSolved()) && 1 == workReviewDto.getIsSolved()) {
//            this.updateWorkDealIsReview(workReviewDto.getWorkId(), 1);
//        }
        // 只要有回访记录就认为是[已回访]
        this.updateWorkDealIsReview(workReviewDto.getWorkId(), 1);


    }

    @Override
    public List<WorkReviewDto> listWorkReviewsByWorkId(WorkReviewDto workReviewDto, UserInfo userInfo, ReqParam reqParam) {

        QueryWrapper<WorkReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_id", workReviewDto.getWorkId());
        queryWrapper.eq("corp_id", reqParam.getCorpId());
        queryWrapper.orderByDesc("operat_time");
        List<WorkReview> list = this.list(queryWrapper);
        List<WorkReviewDto> workReviewDtoList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> userIdList = new ArrayList<>();
            Set<Long> corpIdSet = new HashSet<>();
            for (WorkReview workReview : list) {
                WorkReviewDto reviewDto = new WorkReviewDto();
                if (LongUtil.isNotZero(workReview.getCorpId())) {
                    corpIdSet.add(workReview.getCorpId());
                }
                BeanUtils.copyProperties(workReview, reviewDto);
                userIdList.add(workReview.getOperator());
                workReviewDtoList.add(reviewDto);
            }
            Result<Map<Long, String>> corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdSet));
            Map<Long, String> corpMap = corpResult.getData();
            corpMap = corpMap == null ? new HashMap<>() : corpMap;

            Map<String, Map<String, String>> userNameMap = uasFeignService.mapCorpUserInfoByUserIdList(userIdList,
                    reqParam.getCorpId()).getData();


            for (WorkReviewDto reviewDto : workReviewDtoList) {
                reviewDto.setCorpName(corpMap.get(reviewDto.getCorpId()));
                Map<String, String> userInfoMap = userNameMap.get(reviewDto.getOperator() + "");
                if (userInfoMap != null) {
                    reviewDto.setUserName(userInfoMap.get("userName"));
                }
            }
        }
        return workReviewDtoList;

    }

    @Override
    public void delWorkReview(WorkReviewDto workReviewDto) {
        if (LongUtil.isZero(workReviewDto.getId())) {
            throw new AppException("客户回访记录编号不能为空");
        }
        this.removeById(workReviewDto.getId());

        QueryWrapper<WorkReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_id", workReviewDto.getWorkId());
        queryWrapper.orderByDesc("operat_time");
        List<WorkReview> list = this.list(queryWrapper);

        if (list != null && list.size() > 0) {


            // 只有解决了才认为是[已回访]
//            AtomicBoolean isSolved = new AtomicBoolean(false);
//            list.forEach(workReview -> {
//                if(workReview.getIsSolved() != null && 1 == workReview.getIsSolved()){
//                    isSolved.set(true);
//                    return;
//                }
//            });
//
//            if(isSolved.get()){
//                // 已解决
//                this.updateWorkDealIsReview(workReviewDto.getWorkId(),1);
//            }else{
//                // 未解决
//                this.updateWorkDealIsReview(workReviewDto.getWorkId(),0);
//            }


            // 只要有回访记录就认为是[已回访]
            this.updateWorkDealIsReview(workReviewDto.getWorkId(), 1);

        } else {
            // 没有回访记录
            this.updateWorkDealIsReview(workReviewDto.getWorkId(), 0);
        }

    }

    @Override
    public void delWorkReviewByWorkId(WorkReviewDto workReviewDto) {
        if (LongUtil.isZero(workReviewDto.getWorkId())) {
            throw new AppException("工单编号不能为空");
        }
        QueryWrapper<WorkReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_id", workReviewDto.getWorkId());
        this.remove(queryWrapper);
        this.updateWorkDealIsReview(workReviewDto.getWorkId(), 0);
    }


    /**
     * 跟新
     *
     * @param workId
     * @param isReview
     */
    private void updateWorkDealIsReview(Long workId, int isReview) {
        WorkDeal workDeal = workDealService.getById(workId);
        workDeal.setIsReview(isReview);
        workDealService.saveOrUpdate(workDeal);
    }
}
