package com.zjft.usp.anyfix.work.remind.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindDealDto;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindDeal;

import java.util.Date;

/**
 * <p>
 *  工单预警处理接口
 * </p>
 *
 * @author Qiugm
 * @since 2020-05-11
 */
public interface WorkRemindDealService extends IService<WorkRemindDeal> {

    /**
     * 修改工单预警时间
     *
     * @param workRemindDealDto
     * @return
     * @author Qiugm
     * @date 2020-05-14
     */
    void modRemindTime(WorkRemindDealDto workRemindDealDto);

    /**
     * 根据工单ID和预警类型获取工单预警处理信息
     *
     * @param workId
     * @param remindType
     * @return
     * @author Qiugm
     * @date 2020-05-14
     */
    WorkRemindDeal findByWorkIdAndType(long workId, int remindType);

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
    void updateEnabled(Long id, String enabled);

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
    void addMessageQueue(String topic, Long workId, int remindType, Date dealTime);
}
