package com.zjft.usp.anyfix.corp.manage.mapper;

import com.zjft.usp.anyfix.corp.manage.dto.DemanderAutoConfigDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderAutoConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 委托商自动化配置表 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-07-20
 */
public interface DemanderAutoConfigMapper extends BaseMapper<DemanderAutoConfig> {

    /**
     * 根据服务商查询
     *
     * @param serviceCorp
     * @return
     */
    List<DemanderAutoConfigDto> listDtoByFilter(@Param("serviceCorp") Long serviceCorp, @Param("demanderCorp") Long demanderCorp);

}
