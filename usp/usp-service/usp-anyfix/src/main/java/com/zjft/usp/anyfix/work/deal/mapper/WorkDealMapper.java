package com.zjft.usp.anyfix.work.deal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.deal.dto.EngineerDto;
import com.zjft.usp.anyfix.work.deal.filter.WorkDealFilter;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单处理信息表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
public interface WorkDealMapper extends BaseMapper<WorkDeal> {

    /**
     * 查询接单工程师接单情况
     * @param workFilter
     * @return
     */
    List<EngineerDto>  queryEngineerDto(@Param("workFilter") WorkFilter workFilter);

    /**
     * 查询已完成、已评价工单信息(OA报销专用)
     *
     * @param workDealFilter
     * @return
     * @author jfzou
     * @date 2020/02/16 22:40 下午
     **/
    List<WorkDeal> queryCloseWorkForCost(@Param("workDealFilter") WorkDealFilter workDealFilter);


    /**
     * 查询同一设备是否有多个未完成的工单
     * @param workRequestDto
     * 查询时需要以下参数
     * deviceIdList：设备id集合
     * workStatusList：工单状态集合
     * demanderCorp: 服务委托方id
     * serial: 厂商序列号
     * model: 设备型号id
     * workType: 工单类型
     * @return
     */
    List<WorkDeal> listSameWork(WorkRequestDto workRequestDto);
}
