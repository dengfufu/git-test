package com.zjft.usp.anyfix.corp.config.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.corp.config.constant.ItemConfig;
import com.zjft.usp.anyfix.corp.config.dto.ServiceConfigDto;
import com.zjft.usp.anyfix.corp.config.dto.ServiceConfigFilter;
import com.zjft.usp.anyfix.corp.config.mapper.ServiceConfigMapper;
import com.zjft.usp.anyfix.corp.config.model.ServiceConfig;
import com.zjft.usp.anyfix.corp.config.service.ServiceConfigService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托商服务商关系配置表 服务实现类
 * </p>
 *
 * @author zrlin
 * @since 2020-04-15
 */
@Service
public class ServiceConfigServiceImpl extends ServiceImpl<ServiceConfigMapper, ServiceConfig> implements ServiceConfigService {

    @Override
    public void addServiceConfig(ServiceConfig serviceConfig) {
        if (LongUtil.isZero(serviceConfig.getId())) {
            throw new AppException("编号不能为空");
        }
        if (serviceConfig.getItemId() == null) {
            throw new AppException("项目编号不能为空");
        }
        QueryWrapper<ServiceConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_id", serviceConfig.getItemId());
        queryWrapper.eq("id", serviceConfig.getId());
        ServiceConfig foundServiceConfig = this.getOne(queryWrapper);
        if (foundServiceConfig == null) {
            this.save(serviceConfig);
        } else if (!foundServiceConfig.getItemValue().equals(serviceConfig.getItemValue())) {
            this.baseMapper.updateSingItemValue(serviceConfig);
        }
    }

    /**
     * 获得委托商数据项配置列表
     *
     * @param serviceConfigFilter
     * @return
     * @author zgpi
     * @date 2020/6/30 15:19
     **/
    @Override
    public List<ServiceConfigDto> listServiceConfig(ServiceConfigFilter serviceConfigFilter) {
        if (LongUtil.isZero(serviceConfigFilter.getWorkId()) && LongUtil.isZero(serviceConfigFilter.getReferId())) {
            throw new AppException("工单编号或者委托关系编号不能为空");
        }
        List<ServiceConfigDto> serviceConfigDtoList;
        if (LongUtil.isNotZero(serviceConfigFilter.getWorkId())) {
            serviceConfigDtoList = this.listConfigItemByWorkId(serviceConfigFilter.getWorkId(), serviceConfigFilter.getItemIdList());
        } else {
            serviceConfigDtoList = this.listConfigByCorpId(serviceConfigFilter.getReferId(), serviceConfigFilter.getItemIdList());
        }
        return serviceConfigDtoList;
    }

    @Override
    public List<ServiceConfigDto> getConfigItemByWorkId(Long workId, Map<Integer, String> configItemMap) {
        List<Integer> itemList = ItemConfig.getItemIdList(configItemMap);
        return this.listConfigItemByWorkId(workId, itemList);
    }

    @Override
    public List<ServiceConfigDto> listConfigByCorpId(Long corpId, List<Integer> itemIdList) {
        if (LongUtil.isZero(corpId)) {
            throw new AppException("企业编号不能为空");
        }
        List<ServiceConfigDto> serviceConfigDtoList = this.baseMapper.selectConfigList(corpId, itemIdList);
        this.handleSelectedServiceConfigList(serviceConfigDtoList);
        return serviceConfigDtoList;
    }

    @Override
    public List<ServiceConfigDto> getConfigByCorpId(Long corpId, Map<Integer, String> configItemMap) {
        List<Integer> itemIdList = ItemConfig.getItemIdList(configItemMap);
        return this.listConfigByCorpId(corpId, itemIdList);
    }

    void handleSelectedServiceConfigList(List<ServiceConfigDto> serviceConfigDtoList) {
        for (ServiceConfigDto serviceConfigDto : serviceConfigDtoList) {
            if (StrUtil.isEmpty(serviceConfigDto.getItemValue())) {
                serviceConfigDto.setItemValue(serviceConfigDto.getDefaultValue());
            }
        }
    }

    @Override
    public void validateServiceConfig(List<ServiceConfigDto> serviceConfigList, Object object,
                                      Map<Integer, String> itemConfigFormMap) {
        if (CollectionUtil.isNotEmpty(serviceConfigList)) {
            for (ServiceConfigDto serviceConfigDto : serviceConfigList) {
                Field field;
                try {
                    field = object.getClass().getDeclaredField(itemConfigFormMap.get(serviceConfigDto.getItemId()));
                } catch (NoSuchFieldException e) {
                    try {
                        field = object.getClass().getSuperclass().getDeclaredField(itemConfigFormMap.get(serviceConfigDto.getItemId()));
                    } catch (NoSuchFieldException ex) {
                        throw new AppException("自定义配置匹配不到校验的字段");
                    }
                }
                field.setAccessible(true);
                // 2为必填
                if (serviceConfigDto.getItemValue().equals("2")) {
                    try {
                        if (field.getType().equals(Long.class)) {
                            Long value = (Long) field.get(object);
                            if (LongUtil.isZero(value)) {
                                throw new AppException(serviceConfigDto.getDescription());
                            }
                        } else if (field.getType().equals(String.class)) {
                            String value = (String) field.get(object);
                            if (StrUtil.isEmpty(value)) {
                                throw new AppException(serviceConfigDto.getDescription());
                            }
                        }
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
    }

    private List<ServiceConfigDto> listConfigItemByWorkId(Long workId, List<Integer> itemIdList) {
        if (LongUtil.isZero(workId)) {
            throw new AppException("工单编号不能为空");
        }
        List<ServiceConfigDto> serviceConfigDtoList = this.baseMapper.selectByWorkIdWithDemander(workId, itemIdList);
        this.handleSelectedServiceConfigList(serviceConfigDtoList);
        return serviceConfigDtoList;
    }

}
