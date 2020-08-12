package com.zjft.usp.anyfix.work.fee.service;

import com.zjft.usp.anyfix.work.fee.dto.WorkFeeAssortDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeAssortFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeAssortDefine;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.model.ListWrapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类费用定义 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
public interface WorkFeeAssortDefineService extends IService<WorkFeeAssortDefine> {

    /**
     * 分页查询
     *
     * @param workFeeAssortFilter
     * @return
     */
    ListWrapper<WorkFeeAssortDto> query(WorkFeeAssortFilter workFeeAssortFilter);

    /**
     * 添加
     *
     * @param workFeeAssortDto
     * @param userId
     * @param serviceCorp
     */
    void add(WorkFeeAssortDto workFeeAssortDto, Long userId, Long serviceCorp);

    /**
     * 更新
     *
     * @param workFeeAssortDto
     * @param userId
     * @param serviceCorp
     */
    void update(WorkFeeAssortDto workFeeAssortDto, Long userId, Long serviceCorp);

    /**
     * 匹配工单分类费用
     *
     * @param workDto
     * @return
     */
    List<WorkFeeAssortDto> matchWorkFeeAssortDefine(WorkDto workDto);

    /**
     * 根据服务商企业编号获取费用编号与名称的映射
     *
     * @param serviceCorp
     * @return
     */
    Map<Long, String> mapIdAndNameByServiceCorp(Long serviceCorp);

    /**
     * 查询满足分类费用定义的未审核工单
     *
     * @param workFeeAssortDto
     * @return
     */
    List<WorkDto> selectMatchWork(WorkFeeAssortDto workFeeAssortDto);

    /**
     * 获取分类费用规则明细
     *
     * @param assortId
     * @return
     */
    WorkFeeAssortDto getDtoById(Long assortId);

}
