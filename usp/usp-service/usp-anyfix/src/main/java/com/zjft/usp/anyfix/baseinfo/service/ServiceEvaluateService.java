package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.filter.ServiceEvaluateFilter;
import com.zjft.usp.anyfix.baseinfo.model.ServiceEvaluate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务评价指标表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface ServiceEvaluateService extends IService<ServiceEvaluate> {

    /**
     * 查询服务商评价指标
     *
     * @param serviceCrop
     * @return
     * @throws
     * @author zphu
     * @date 2019/9/24 16:19
     **/
    List<ServiceEvaluate> listServiceEvaluate(Long serviceCrop);

    /**
     * 根据企业编号获取映射
     * @param serviceCorp
     * @return
     */
    Map<Integer, ServiceEvaluate> mapIdAndEvaluate(Long serviceCorp);

    /**
     * 分页查询服务评价
     *
     * @param serviceEvaluateFilter
     * @return
     * @author zgpi
     * @date 2019/11/18 15:29
     **/
    ListWrapper<ServiceEvaluate> query(ServiceEvaluateFilter serviceEvaluateFilter);

    /**
     * 保存服务评价指标
     * @param serviceEvaluate
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(ServiceEvaluate serviceEvaluate, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改服务评价指标
     * @param serviceEvaluate
     * @param userInfo
     * @return
     */
    void update(ServiceEvaluate serviceEvaluate, UserInfo userInfo);
}
