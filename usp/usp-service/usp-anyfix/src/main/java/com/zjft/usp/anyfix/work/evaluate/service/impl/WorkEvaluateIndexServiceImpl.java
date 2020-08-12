package com.zjft.usp.anyfix.work.evaluate.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.baseinfo.model.ServiceEvaluate;
import com.zjft.usp.anyfix.baseinfo.service.ServiceEvaluateService;
import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateDto;
import com.zjft.usp.anyfix.work.evaluate.dto.WorkEvaluateIndexDto;
import com.zjft.usp.anyfix.work.evaluate.mapper.WorkEvaluateIndexMapper;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.evaluate.model.WorkEvaluateIndex;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.evaluate.service.WorkEvaluateIndexService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户评价指标表 服务实现类
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkEvaluateIndexServiceImpl extends ServiceImpl<WorkEvaluateIndexMapper, WorkEvaluateIndex> implements WorkEvaluateIndexService {

    @Autowired
    private ServiceEvaluateService serviceEvaluateService;
    @Autowired
    private WorkDealService workDealService;

    @Resource
    private WorkEvaluateIndexMapper workEvaluateIndexMapper;

    @Override
    public List<WorkEvaluateIndex> selectByMonth(WorkEvaluateDto workEvaluateDto, UserInfo userInfo) {
        Assert.isNull(workEvaluateDto,"workEvaluateDto 不能为空");
        Assert.isNull(userInfo,"userInfo 不能为空");
        if(StrUtil.isNotEmpty(workEvaluateDto.getEvaluateId())){
            return workEvaluateIndexMapper.selectByMonth(workEvaluateDto.getMonth(),userInfo.getUserId(),Integer.parseInt(workEvaluateDto.getEvaluateId()));
        }
        return null;
    }

    @Override
    public List<WorkEvaluateIndexDto> listByWorkId(Long workId) {
        List<WorkEvaluateIndexDto> dtoList = new ArrayList<>();
        if(LongUtil.isZero(workId)){
            return dtoList;
        }
        WorkDeal workDeal = this.workDealService.getById(workId);
        if(workDeal == null){
            return dtoList;
        }
        List<WorkEvaluateIndex> workEvaluateIndexList = this.list(new QueryWrapper<WorkEvaluateIndex>().eq("work_id", workId));
        if(workEvaluateIndexList != null){
            Map<Integer, ServiceEvaluate> serviceEvaluateMap = this.serviceEvaluateService.mapIdAndEvaluate(workDeal.getServiceCorp());
            for(WorkEvaluateIndex workEvaluateIndex: workEvaluateIndexList){
                WorkEvaluateIndexDto workEvaluateIndexDto = new WorkEvaluateIndexDto();
                BeanUtils.copyProperties(workEvaluateIndex, workEvaluateIndexDto);
                ServiceEvaluate serviceEvaluate = serviceEvaluateMap.get(workEvaluateIndex.getEvaluateId());
                if(serviceEvaluate != null){
                    workEvaluateIndexDto.setShowType(serviceEvaluate.getShowType());
                    String[] scores = serviceEvaluate.getScores().split(",");
                    String[] labels = serviceEvaluate.getLabels().split(",");
                    workEvaluateIndexDto.setMaxNumber(scores.length);
                    for(int i = 0; i < scores.length; i++){
                        if(scores[i].equals(String.valueOf(workEvaluateIndex.getScore()))){
                            workEvaluateIndexDto.setNumber(i + 1);
                            workEvaluateIndexDto.setLabel(labels[i]);
                            break;
                        }
                    }
                    workEvaluateIndexDto.setEvaluateName(serviceEvaluate.getName());
                }
                dtoList.add(workEvaluateIndexDto);
            }
        }
        return dtoList;
    }


}
