package com.zjft.usp.anyfix.work.remind.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.remind.dto.WorkRemindDealDto;
import com.zjft.usp.anyfix.work.remind.model.WorkRemindDeal;

/**
 * <p>
 * 工单预警处理Mapper 接口
 * </p>
 *
 * @author Qiugm
 * @since 2020-05-11
 */
public interface WorkRemindDealMapper extends BaseMapper<WorkRemindDeal> {

    /**
     * 添加工单预警处理
     *
     * @param workRemindDealDto
     * @return
     * @author Qiugm
     * @date 2020-05-14
     */
    void addWorkRemindDeal(WorkRemindDealDto workRemindDealDto);

    /**
     * 修改工单预警时间
     *
     * @param workRemindDealDto
     * @return
     * @author Qiugm
     * @date 2020-05-14
     */
    void modWorkRemindDeal(WorkRemindDealDto workRemindDealDto);
}
