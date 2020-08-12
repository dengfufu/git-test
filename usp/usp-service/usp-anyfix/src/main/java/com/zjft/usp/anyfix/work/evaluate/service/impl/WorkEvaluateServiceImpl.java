package com.zjft.usp.anyfix.work.evaluate.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.model.ServiceEvaluate;
import com.zjft.usp.anyfix.baseinfo.service.ServiceEvaluateService;
import com.zjft.usp.anyfix.baseinfo.service.ServiceEvaluateTagService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.evaluate.dto.*;
import com.zjft.usp.anyfix.work.evaluate.mapper.WorkEvaluateMapper;
import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluate;
import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluateIndex;
import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluateTag;
import com.zjft.usp.anyfix.work.evaluate.service.WorkEvaluateIndexService;
import com.zjft.usp.anyfix.work.evaluate.service.WorkEvaluateService;
import com.zjft.usp.anyfix.work.evaluate.service.WorkEvaluateTagService;
import com.zjft.usp.anyfix.work.operate.model.WorkOperate;
import com.zjft.usp.anyfix.work.operate.service.WorkOperateService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatTimeEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.anyfix.work.request.service.WorkRequestService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 客户评价表 服务实现类
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkEvaluateServiceImpl extends ServiceImpl<WorkEvaluateMapper, WorkEvaluate> implements WorkEvaluateService {

    @Autowired
    private WorkRequestService workRequestService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkEvaluateTagService workEvaluateTagService;
    @Autowired
    private WorkEvaluateIndexService workEvaluateIndexService;
    @Autowired
    private ServiceEvaluateTagService serviceEvaluateTagService;
    @Autowired
    private WorkOperateService workOperateService;
    @Autowired
    private ServiceEvaluateService serviceEvaluateService;
    @Resource
    private WorkEvaluateMapper workEvaluateMapper;
    @Resource
    private UasFeignService uasFeignService;

    /**
     * 添加客户评价
     *
     * @param workEvaluateDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/14 3:21 下午
     **/
    @Override
    public void addWorkEvaluate(WorkEvaluateDto workEvaluateDto, UserInfo userInfo, ReqParam reqParam) {
        WorkRequest workRequest = workRequestService.getById(workEvaluateDto.getWorkId());
        WorkDeal workDeal = workDealService.getById(workEvaluateDto.getWorkId());
        if (workRequest == null || workDeal == null) {
            throw new AppException("工单不存在！");
        }
        if (workDeal.getWorkStatus() != WorkStatusEnum.TO_EVALUATE.getCode()) {
            throw new AppException("工单状态不正确，当前状态为[" + WorkStatusEnum.getNameByCode(workDeal.getWorkStatus()) + "]！");
        }
        if (!workRequest.getCreator().equals(userInfo.getUserId())
                || !workDeal.getEngineer().equals(userInfo.getUserId())) {
            throw new AppException("您不是建单人或维护工程师，不能评价工单！");
        }

        //插入评价主表记录
        WorkEvaluate workEvaluate = new WorkEvaluate();
        BeanUtils.copyProperties(workEvaluateDto, workEvaluate);
        workEvaluate.setLon(reqParam.getLon());
        workEvaluate.setLat(reqParam.getLat());
        workEvaluate.setOperator(userInfo.getUserId());
        workEvaluate.setOperateTime(DateUtil.date().toTimestamp());
        this.save(workEvaluate);

        //插入评价标签表
        List<WorkEvaluateTagDto> workEvaluateTagDtoList = workEvaluateDto.getWorkEvaluateTagDtoList();
        if (workEvaluateTagDtoList != null) {
            for (WorkEvaluateTagDto workEvaluateTagDto : workEvaluateTagDtoList) {
                WorkEvaluateTag workEvaluateTag = new WorkEvaluateTag();
                workEvaluateTag.setWorkId(workEvaluateDto.getWorkId());
                workEvaluateTag.setTagId(workEvaluateTagDto.getTagId());
                workEvaluateTagService.save(workEvaluateTag);
            }
        }

        //插入评价指标表
        if (workEvaluateDto.getWorkEvaluateIndexDtoList() != null) {
            for (WorkEvaluateIndexDto workEvaluateIndexDto : workEvaluateDto.getWorkEvaluateIndexDtoList()) {
                WorkEvaluateIndex workEvaluateIndex = new WorkEvaluateIndex();
                workEvaluateIndex.setWorkId(workEvaluateDto.getWorkId());
                workEvaluateIndex.setEvaluateId(workEvaluateIndexDto.getEvaluateId());
                workEvaluateIndex.setScore(workEvaluateIndexDto.getScore());
                workEvaluateIndexService.save(workEvaluateIndex);
            }
        }

        workDeal = new WorkDeal();
        workDeal.setWorkId(workEvaluateDto.getWorkId());
        workDeal.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workDeal.setEvaluateTime(DateUtil.date().toTimestamp());
        workDealService.updateById(workDeal);

        // 添加操作记录
        WorkOperate workOperate = new WorkOperate();
        workOperate.setWorkId(workDeal.getWorkId());
        workOperate.setWorkStatus(WorkStatusEnum.CLOSED.getCode());
        workOperate.setOperator(userInfo.getUserId());
        workOperate.setCorp(reqParam.getCorpId());
        workOperateService.addWorkOperateByEvaluate(workOperate);
    }

    /**
     * 查询某月的评价指标信息
     *
     * @param workId
     * @return
     * @author zphu
     * @date 2019/9/25 9:00
     **/
    @Override
    public WorkEvaluateDto selectByWorkId(Long workId) {
        WorkEvaluateDto workEvaluateDto = new WorkEvaluateDto();
        if (LongUtil.isZero(workId)) {
            return null;
        }
        WorkEvaluate workEvaluate = this.getById(workId);
        if (workEvaluate != null) {
            BeanUtils.copyProperties(workEvaluate, workEvaluateDto);
        } else {
            return null;
        }
        WorkDeal workDeal = this.workDealService.getById(workId);
        if (workDeal == null) {
            return null;
        }
        List<WorkEvaluateIndexDto> workEvaluateIndexDtos = this.workEvaluateIndexService.listByWorkId(workId);
        workEvaluateDto.setWorkEvaluateIndexDtoList(workEvaluateIndexDtos);
        List<WorkEvaluateTag> workEvaluateTags = this.workEvaluateTagService.list(new QueryWrapper<WorkEvaluateTag>().eq("work_id", workId));
        if (workEvaluateTags != null && workEvaluateTags.size() > 0) {
            Map<Integer, String> tagMap = this.serviceEvaluateTagService.mapIdAndNameByCorp(workDeal.getServiceCorp());
            List<WorkEvaluateTagDto> workEvaluateTagDtos = new ArrayList<>();
            for (WorkEvaluateTag workEvaluateTag : workEvaluateTags) {
                WorkEvaluateTagDto workEvaluateTagDto = new WorkEvaluateTagDto();
                BeanUtils.copyProperties(workEvaluateTag, workEvaluateTagDto);
                workEvaluateTagDto.setTagName(tagMap.get(workEvaluateTag.getTagId()));
                workEvaluateTagDtos.add(workEvaluateTagDto);
            }
            workEvaluateDto.setWorkEvaluateTagDtoList(workEvaluateTagDtos);
        }
        return workEvaluateDto;
    }

    @Override
    public List<WorkEvaluateDto> selectByWorkIds(List<Long> workIdList, Long corpId) {
        if (corpId == null || workIdList == null || workIdList.size() == 0) {
            return null;
        }
        List<WorkEvaluate> workEvaluateList = this.list(new QueryWrapper<WorkEvaluate>().in("work_id", workIdList));
        if (workEvaluateList != null) {
            List<WorkEvaluateTag> workEvaluateTagList = this.workEvaluateTagService.list(new QueryWrapper<WorkEvaluateTag>().in("work_id", workIdList));
            List<WorkEvaluateIndex> workEvaluateIndexList = this.workEvaluateIndexService.list(new QueryWrapper<WorkEvaluateIndex>().in("work_id", workIdList));
            Map<Integer, String> mapTagIdAndName = this.serviceEvaluateTagService.mapIdAndNameByCorp(corpId);
            Map<Integer, ServiceEvaluate> mapIndexIdAndEvaluate = this.serviceEvaluateService.mapIdAndEvaluate(corpId);
            List<WorkEvaluateDto> workEvaluateDtoList = new LinkedList<>();
            for (WorkEvaluate workEvaluate : workEvaluateList) {
                WorkEvaluateDto workEvaluateDto = new WorkEvaluateDto();
                BeanUtils.copyProperties(workEvaluate, workEvaluateDto);
                //添加taglist
                List<WorkEvaluateTagDto> workEvaluateTagDtoList = new LinkedList<>();
                for (WorkEvaluateTag workEvaluateTag : workEvaluateTagList) {
                    if (workEvaluateTag.getWorkId().equals(workEvaluate.getWorkId())) {
                        WorkEvaluateTagDto workEvaluateTagDto = new WorkEvaluateTagDto();
                        BeanUtils.copyProperties(workEvaluateTag, workEvaluateTagDto);
                        workEvaluateTagDto.setTagName(mapTagIdAndName.get(workEvaluateTag.getTagId()));
                        workEvaluateTagDtoList.add(workEvaluateTagDto);
                    }
                }
                workEvaluateDto.setWorkEvaluateTagDtoList(workEvaluateTagDtoList);

                //添加indexlist
                List<WorkEvaluateIndexDto> workEvaluateIndexDtoList = new LinkedList<>();
                for (WorkEvaluateIndex workEvaluateIndex : workEvaluateIndexList) {
                    if (workEvaluateIndex.getWorkId().equals(workEvaluate.getWorkId())) {
                        WorkEvaluateIndexDto workEvaluateIndexDto = new WorkEvaluateIndexDto();
                        BeanUtils.copyProperties(workEvaluateIndex, workEvaluateIndexDto);
                        workEvaluateIndexDto.setEvaluateName(mapIndexIdAndEvaluate.get(workEvaluateIndex.getEvaluateId()).getName());
                        workEvaluateIndexDtoList.add(workEvaluateIndexDto);
                    }
                }
                workEvaluateDto.setWorkEvaluateIndexDtoList(workEvaluateIndexDtoList);
                workEvaluateDtoList.add(workEvaluateDto);
            }
            return workEvaluateDtoList;
        }
        return null;
    }

    @Override
    public List<WorkEvaluateDto> listByDate(WorkEvaluateDto workEvaluateDto, UserInfo userInfo, ReqParam reqParam) {
        List<WorkDeal> workDealList = this.workDealService.list(new QueryWrapper<WorkDeal>().eq("engineer", userInfo.getUserId()).eq("service_corp", reqParam.getCorpId()).
                between("finish_time", workEvaluateDto.getDateBegin(), workEvaluateDto.getDateEnd()));
        if (workDealList != null) {
            List<Long> workIdList = new LinkedList<>();
            for (WorkDeal workDeal : workDealList) {
                if (workDeal.getEvaluateTime() != null) {
                    workIdList.add(workDeal.getWorkId());
                }
            }
            List<WorkEvaluateDto> workEvaluateDtoList = this.selectByWorkIds(workIdList, reqParam.getCorpId());
            if (workEvaluateDtoList != null && workEvaluateDtoList.size() > 0) {
                for (WorkEvaluateDto workEvaluate : workEvaluateDtoList) {
                    workEvaluate.setWorkNum(workDealList.size());
                }
                return workEvaluateDtoList;
            }
        }
        return null;
    }

    @Override
    public List<WorkEvaluateDto> listByIndex(WorkEvaluateDto workEvaluateDto, UserInfo userInfo, ReqParam reqParam) {
        Page<WorkEvaluateDto> page = new Page(workEvaluateDto.getPageNum(), workEvaluateDto.getPageSize());
        List<WorkEvaluateDto> workEvaluateDtoList = this.workEvaluateMapper.queryEvaluateByIndex(page, workEvaluateDto, userInfo, reqParam);
        if (workEvaluateDtoList != null && workEvaluateDtoList.size() > 0) {
            List<Long> demanderCorpList = new LinkedList<>();
            List<Long> workIdList = new LinkedList<>();
            for (WorkEvaluateDto workEvaluateDto1 : workEvaluateDtoList) {
                demanderCorpList.add(workEvaluateDto1.getDemanderCorp());
                workIdList.add(workEvaluateDto1.getWorkId());
            }
            List<WorkEvaluateDto> workEvaluateDtoListReturn = this.selectByWorkIds(workIdList, reqParam.getCorpId());
            // 获取进行评价的公司的name的map
            Map<Long, String> corpMap = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(demanderCorpList)).getData();
            for (WorkEvaluateDto workEvaluateDto1 : workEvaluateDtoList) {
                for (WorkEvaluateDto workEvaluateDto2 : workEvaluateDtoListReturn) {
                    if (workEvaluateDto1.getWorkId() != null && workEvaluateDto1.getWorkId().equals(workEvaluateDto2.getWorkId())) {
                        workEvaluateDto2.setDemanderCorpName(corpMap.get(workEvaluateDto1.getDemanderCorp()));
                        workEvaluateDto2.setDemanderCorp(workEvaluateDto1.getDemanderCorp());
                    }

                }
            }
            return workEvaluateDtoListReturn;
        }
        return null;
    }

    @Override
    public Map<String, Object> listWorkEvaluateCountDto(WorkEvaluateDto workEvaluateDto, Long corpId) {
        cn.hutool.core.lang.Assert.notNull(workEvaluateDto.getCountTime(), "统计时间不能为空");

        // 按照月份查询统计列表
        List<WorkEvaluateCountDto> dtoList = this.workEvaluateMapper.
                queryWorkEvaluateCountDto(corpId, workEvaluateDto.getEvaluateId(), workEvaluateDto.getCountTime());
        // 查询服务评价
        ServiceEvaluate serviceEvaluate = serviceEvaluateService.getById(workEvaluateDto.getEvaluateId());

        // 统计查询对应评价的数量
        List<WorkEvaluateTotalCountDto> countDtoList = this.workEvaluateMapper.queryWorkEvaluateTotalCountDto
                (corpId, workEvaluateDto.getEvaluateId(), workEvaluateDto.getCountTime());
        // 描述-统计数
        Map<String, Integer> nameCountMap = new HashMap<>();
        // 为月份统计

        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> returnMap = new HashMap<>();
        // 获取这个月的时间
        int day = 0;
        if (workEvaluateDto.getCountTime() == WorkStatTimeEnum.THIS_MONTH.getCode()) {
            day = getCurrentDay();
        } else {
            day = getCurrentMonth();
        }
        // 查到的分数对应的描述项不为空
        if (StrUtil.isNotBlank(serviceEvaluate.getLabels())) {
            // 划分出来
            String[] scoreStr = serviceEvaluate.getScores().split(",");
            // 分数对应描述的集合
            Map<String, String> scoreMap = new HashMap<>();
            String[] labels = serviceEvaluate.getLabels().split(",");
            if (scoreStr.length != labels.length) {
                throw new AppException("该评价指标没有对应的评价描述！");
            }
            for (int i = 0; i < labels.length; i++) {
                scoreMap.put(scoreStr[i], labels[i]);
                nameCountMap.put(labels[i], 0);
            }
            for (int i = 1; i <= day; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("date", i);
                for (String str : labels) {
                    map.put(str, 0);
                }
                list.add(map);
            }
            for (WorkEvaluateCountDto countDto : dtoList) {
                if (countDto.getCount() != 0) {
                    int date = countDto.getDate();
                    Map<String, Object> map = list.get(date - 1);
                    int score = countDto.getScore();
                    String label = scoreMap.get(score + "");
                    map.put(label, countDto.getCount());
                }
            }
            // 用于显示
            for (WorkEvaluateTotalCountDto countDto : countDtoList) {
                int score = countDto.getScore();
                String label = scoreMap.get(score + "");
                nameCountMap.put(label, countDto.getCount());
            }
        }
        returnMap.put("list", list);
        returnMap.put("countList", nameCountMap);
        return returnMap;
    }

    public static int getDayCountByCurrentMonth() {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }


    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }


}
