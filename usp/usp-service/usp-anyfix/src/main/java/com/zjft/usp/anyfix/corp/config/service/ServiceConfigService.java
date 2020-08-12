package com.zjft.usp.anyfix.corp.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.config.dto.ServiceConfigDto;
import com.zjft.usp.anyfix.corp.config.dto.ServiceConfigFilter;
import com.zjft.usp.anyfix.corp.config.model.ServiceConfig;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托商服务商关系配置表 服务类
 * </p>
 *
 * @author zrlin
 * @since 2020-04-15
 */
public interface ServiceConfigService extends IService<ServiceConfig> {

    /**
     * 根据工单和配置项关系对象获取委托关系的数据项
     *
     * @param workId
     * @param
     * @return
     */
    List<ServiceConfigDto> getConfigItemByWorkId(Long workId, Map<Integer, String> configItemMap);

    /**
     * 获取企业的配置项
     *
     * @param corpId
     * @param itemIdList
     * @return
     */
    List<ServiceConfigDto> listConfigByCorpId(Long corpId, List<Integer> itemIdList);

    /**
     * 获取企业的配置项
     *
     * @param corpId
     * @param configItemMap
     * @return
     */
    List<ServiceConfigDto> getConfigByCorpId(Long corpId, Map<Integer, String> configItemMap);

    /**
     * 校验参数是否正确
     *
     * @param serviceConfigList
     * @param object
     */
    void validateServiceConfig(List<ServiceConfigDto> serviceConfigList, Object object,
                               Map<Integer, String> itemConfigFormMap);

    /**
     * 添加单条配置
     *
     * @param serviceConfig
     */
    void addServiceConfig(ServiceConfig serviceConfig);

    /**
     * 获得委托商数据项配置列表
     *
     * @param serviceConfigFilter
     * @return
     * @author zgpi
     * @date 2020/6/30 15:19
     **/
    List<ServiceConfigDto> listServiceConfig(ServiceConfigFilter serviceConfigFilter);
}
