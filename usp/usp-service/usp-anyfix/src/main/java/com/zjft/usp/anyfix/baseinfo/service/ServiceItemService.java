package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.dto.ServiceItemDto;
import com.zjft.usp.anyfix.baseinfo.filter.ServiceItemFilter;
import com.zjft.usp.anyfix.baseinfo.model.ServiceItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务项目表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface ServiceItemService extends IService<ServiceItem> {

    /**
     * 根据公司获得服务项目
     * @author zgpi
     * @date 2019/9/25 4:56 下午 
     * @param serviceItemFilter
     * @param serviceCorp
     * @return
     **/
    List<ServiceItem> listServiceItemByFilter(ServiceItemFilter serviceItemFilter, Long serviceCorp);

    /**
     * 分页查询服务项目列表
     *
     * @param serviceItemFilter
     * @return
     * @author zgpi
     * @date 2019/11/18 09:58
     **/
    ListWrapper<ServiceItemDto> query(ServiceItemFilter serviceItemFilter);

    /**
     * 获取id和name的映射
     *
     * @author canlei
     * @date 2020/01/08
     * @param serviceCorp
     * @return
     */
    Map<Integer, String> mapIdAndNameByCorp(Long serviceCorp);


    /**
     * 保存服务项目
     * @param serviceItem
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(ServiceItem serviceItem, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改服务项目
     * @param serviceItem
     * @param userInfo
     * @return
     */
    void update(ServiceItem serviceItem, UserInfo userInfo);
}
