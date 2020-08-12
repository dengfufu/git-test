package com.zjft.usp.uas.right.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.right.dto.SysTenantDto;
import com.zjft.usp.uas.right.filter.SysTenantFilter;
import com.zjft.usp.uas.right.model.SysTenant;

import java.util.List;

/**
 * <p>
 * 租户设置表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysTenantMapper extends BaseMapper<SysTenant> {

    /**
     * 关联查询租户企业的详细信息
     * @param sysTenantFilter
     * @return
     */
    List<SysTenantDto> selectSysTenant (SysTenantFilter sysTenantFilter);

}
