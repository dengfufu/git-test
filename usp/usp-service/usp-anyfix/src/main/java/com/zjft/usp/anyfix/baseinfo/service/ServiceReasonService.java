package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.filter.ServiceReasonFilter;
import com.zjft.usp.anyfix.baseinfo.model.ServiceReason;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 * 服务商原因表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface ServiceReasonService extends IService<ServiceReason> {

    /**
     * 获得服务商可用的原因列表
     * @author zgpi
     * @date 2019/10/11 4:27 下午
     * @param serviceCorp
     * @param reasonType
     * @return
     **/
    List<ServiceReason> listEnableServiceReason(Long serviceCorp, Integer reasonType);


    /**
     * 根据服务商和原因类型获取列表
     * @param customCorp
     * @param type
     * @return
     */
    List<ServiceReason> selectByCorpAndType(Long customCorp, String type);

    /**
     * 分页查询服务商原因列表
     *
     * @param serviceReasonFilter
     * @return
     * @author zgpi
     * @date 2019/11/18 14:41
     **/
    ListWrapper<ServiceReason> query(ServiceReasonFilter serviceReasonFilter);

    /**
     * 保存客户原因
     * @param serviceReason
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(ServiceReason serviceReason, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改客户端原因
     * @param serviceReason
     * @param userInfo
     * @return
     */
    void update(ServiceReason serviceReason, UserInfo userInfo);
}
