package com.zjft.usp.anyfix.work.fee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeImplementDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeImplementFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeImplementDefine;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单支出费用定义 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
public interface WorkFeeImplementDefineService extends IService<WorkFeeImplementDefine> {

    /**
     * 分页查询工单支出费用定义
     *
     * @param workFeeImplementFilter
     * @param reqParam
     * @return
     */
    ListWrapper<WorkFeeImplementDto> query(WorkFeeImplementFilter workFeeImplementFilter, ReqParam reqParam);

    /**
     * 添加
     *
     * @param workFeeImplementDto
     * @param userInfo
     * @param reqParam
     */
    void add(WorkFeeImplementDto workFeeImplementDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 更新
     *
     * @param workFeeImplementDto
     * @param userInfo
     */
    void update(WorkFeeImplementDto workFeeImplementDto, UserInfo userInfo);

    /**
     * 根据id获取详情
     *
     * @param implementId
     * @return
     */
    WorkFeeImplementDto getDtoById(Long implementId);

    /**
     * 根据工单查询工单支出费用列表
     *
     * @param workDto
     * @return
     */
    List<WorkFeeImplementDto> listDtoByWork(WorkDto workDto);

    /**
     * 根据服务商企业编号获取费用编号和名称的映射
     *
     * @param serviceCorp
     * @return
     */
    Map<Long, String> mapIdAndNameByCorp(Long serviceCorp);

}
