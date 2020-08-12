package com.zjft.usp.anyfix.work.auto.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.auto.dto.WorkDispatchServiceCorpDto;
import com.zjft.usp.anyfix.work.auto.filter.WorkDispatchServiceCorpFilter;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.auto.model.WorkDispatchServiceCorp;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.common.model.ListWrapper;

import java.util.Map;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.service
 * @date 2019-09-26 18:20
 * @note
 */
public interface WorkDispatchServiceCorpService extends IService<WorkDispatchServiceCorp> {

    /**
     * 分页查询自动分配服务商规则列表
     *
     * @param workDispatchServiceCorpFilter
     * @return
     * @author zgpi
     * @date 2019/11/13 19:33
     **/
    ListWrapper<WorkDispatchServiceCorpDto> query(WorkDispatchServiceCorpFilter workDispatchServiceCorpFilter);

    String getName(String id, Map<Long, String> map, String connector);

    String getDistrictName(String district, Map<String, String> areaMap, String connector);

    WorkDispatchServiceCorpDto getById(Long id);

    /**
     * 工单分配服务商
     * @param workDispatchServiceCorp
     * @return
     */
    void add(WorkDispatchServiceCorpDto workDispatchServiceCorp);

    /**
     * 工单修改服务商
     * @param workDispatchServiceCorp
     * @return
     */
    void mod(WorkDispatchServiceCorpDto workDispatchServiceCorp);

    /**
     * 工单删除服务商
     * @param id
     */
    void delById(Long id);

    /**
     * 自动分配服务商
     *
     * @param workRequest
     * @param workDeal
     * @return
     * @author zgpi
     * @date 2019/11/13 18:44
     **/
    Long autoGetServiceCorp(WorkRequest workRequest, WorkDeal workDeal);

    /**
     * 从map中获取逗号分隔的key为int类型的value，由逗号分隔返回
     *
     * @param id
     * @param map
     * @param connector
     * @return
     */
    String getIntegerName(String id, Map<Integer, String> map, String connector);

}
