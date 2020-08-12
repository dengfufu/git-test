package com.zjft.usp.anyfix.corp.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.corp.config.dto.ServiceConfigDto;
import com.zjft.usp.anyfix.corp.config.model.ServiceConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 委托商服务商关系配置表 Mapper 接口
 * </p>
 *
 * @author zrlin
 * @since 2020-04-15
 */
public interface ServiceConfigMapper extends BaseMapper<ServiceConfig> {


    void updateItemValue(@Param("serviceConfigList") List<ServiceConfig> serviceConfigList,
                         @Param("itemIdList") List<Integer> itemList,
                         @Param("id") Long id);

    List<ServiceConfigDto> selectByWorkIdWithDemander(@Param("workId") Long  workId,
                                                      @Param("itemIdList") List<Integer> itemIdList);

    void updateSingItemValue(@Param("serviceConfig") ServiceConfig serviceConfig);

    List<ServiceConfigDto> selectConfigList(@Param("id") Long id,
                                            @Param("itemIdList") List<Integer> itemIdList);

    List<ServiceConfigDto> selectByDemanderService(@Param("demanderCorp") Long demanderCorp,
                                                             @Param("serviceCorp") Long serviceCorp,
                                                            @Param("itemIdList") List<Integer> itemIdList);
}
