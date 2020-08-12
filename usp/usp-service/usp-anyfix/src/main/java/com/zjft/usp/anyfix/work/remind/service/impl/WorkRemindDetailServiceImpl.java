package com.zjft.usp.anyfix.work.remind.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindDetailDto;
import com.zjft.usp.anyfix.work.remind.enums.WorkRemindTypeEnum;
import com.zjft.usp.anyfix.work.remind.mapper.WorkRemindDetailMapper;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindDetail;
import com.zjft.usp.anyfix.work.remind.service.WorkRemindDetailService;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2020-04-20 11:20
 * @Version 1.0
 */
@Service
public class WorkRemindDetailServiceImpl extends ServiceImpl<WorkRemindDetailMapper, WorkRemindDetail> implements
        WorkRemindDetailService {

    @Override
    public List<WorkRemindDetailDto> listByRemindId(Long remindId) {
        List<WorkRemindDetailDto> workRemindDetailDtoList = new ArrayList<>();

        QueryWrapper queryWrapper = new QueryWrapper();
        if (LongUtil.isNotZero(remindId)) {
            queryWrapper.eq("remind_id", remindId);
        }
        queryWrapper.orderByAsc("remind_type");
        List<WorkRemindDetail> workRemindDetailList = this.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(workRemindDetailList)) {
            workRemindDetailDtoList = JsonUtil
                    .parseArray(JsonUtil.toJson(workRemindDetailList), WorkRemindDetailDto.class);
            for (WorkRemindDetailDto workRemindDetailDto : workRemindDetailDtoList) {
                workRemindDetailDto
                        .setRemindTypeName(WorkRemindTypeEnum.getNameByCode(workRemindDetailDto.getRemindType()));
            }
        }
        return workRemindDetailDtoList;
    }

    @Override
    public void deleteByRemindId(Long remindId) {
        if (LongUtil.isNotZero(remindId)) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("remind_id", remindId);
            this.remove(updateWrapper);
        }
    }
}
