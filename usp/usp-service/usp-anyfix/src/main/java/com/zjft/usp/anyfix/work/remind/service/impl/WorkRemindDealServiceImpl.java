package com.zjft.usp.anyfix.work.remind.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindDealDto;
import com.zjft.usp.anyfix.work.remind.mapper.WorkRemindDealMapper;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindDeal;
import com.zjft.usp.anyfix.work.remind.service.WorkRemindDealService;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.mq.util.MqSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单预警处理实现类
 * </p>
 *
 * @author Qiugm
 * @since 2020-05-11
 */
@Service
public class WorkRemindDealServiceImpl extends ServiceImpl<WorkRemindDealMapper, WorkRemindDeal> implements
        WorkRemindDealService {
    @Resource
    private WorkRemindDealMapper workRemindDealMapper;
    @Autowired
    private MqSenderUtil mqSenderUtil;

    /**
     * 修改工单预警时间
     *
     * @param workRemindDealDto
     * @return
     * @author Qiugm
     * @date 2020-05-14
     */
    @Override
    public void modRemindTime(WorkRemindDealDto workRemindDealDto) {
        WorkRemindDeal workRemindDeal = this.findByWorkIdAndType(workRemindDealDto.getWorkId(),
                workRemindDealDto.getRemindType());
        if (workRemindDeal == null) {
            workRemindDealDto.setId(KeyUtil.getId());
            workRemindDealMapper.addWorkRemindDeal(workRemindDealDto);
        } else {
            workRemindDealMapper.modWorkRemindDeal(workRemindDealDto);
        }
    }

    /**
     * 根据工单ID和预警类型获取工单预警处理信息
     *
     * @param workId
     * @param remindType
     * @return
     * @author Qiugm
     * @date 2020-05-14
     */
    public WorkRemindDeal findByWorkIdAndType(long workId, int remindType) {
        WorkRemindDeal workRemindDeal = null;
        if (LongUtil.isNotZero(workId) && IntUtil.isNotZero(remindType)) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("work_id", workId);
            queryWrapper.eq("remind_type", remindType);
            List<WorkRemindDeal> workRemindDealList = this.list(queryWrapper);
            if (workRemindDealList != null && !workRemindDealList.isEmpty()) {
                workRemindDeal = workRemindDealList.get(0);
            }
        }
        return workRemindDeal;
    }

    /**
     * 修改预警时间处理记录是否有效
     * 针对预警时间延期后，工单处理人在预警时间作了处理
     *
     * @param id
     * @param enabled
     * @return
     * @author Qiugm
     * @date 2020-05-15
     */
    public void updateEnabled(Long id, String enabled) {
        if (LongUtil.isNotZero(id) && StrUtil.isNotEmpty(enabled)) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.set("enabled", enabled);
            updateWrapper.eq("id", id);
            this.update(updateWrapper);
        }
    }

    /**
     * 添加消息到消息队列
     *
     * @param topic
     * @param workId
     * @param remindType
     * @param dealTime
     * @return
     * @author Qiugm
     * @date 2020-05-15
     */
    @Override
    public void addMessageQueue(String topic, Long workId, int remindType, Date dealTime) {
        Map<String, Object> msg = new HashMap<>(1);
        msg.put("workId", workId);
        msg.put("remindType", remindType);
        msg.put("dealTime", dealTime);
        mqSenderUtil.sendMessage(topic, JsonUtil.toJson(msg));
    }

}
