package com.zjft.usp.uas.right.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.right.dto.SysTenantDto;
import com.zjft.usp.uas.right.filter.SysTenantFilter;
import com.zjft.usp.uas.right.model.SysTenant;

import java.util.List;

/**
 * <p>
 * 租户设置表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
public interface SysTenantService extends IService<SysTenant> {

    /**
     * 分页查询系统租户
     *
     * @param sysTenantFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 17:13
     **/
    ListWrapper<SysTenantDto> query(SysTenantFilter sysTenantFilter);

    /**
     * 添加系统租户
     *
     * @param sysTenant
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2019/11/26 18:31
     **/
    void addSysTenant(SysTenant sysTenant, UserInfo userInfo);

    /**
     * 修改系统租户
     *
     * @param sysTenant
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2019/11/26 18:31
     **/
    void updateSysTenant(SysTenant sysTenant, UserInfo userInfo);

    /**
     * 删除系统租户
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/11/26 18:32
     **/
    void delSysTenant(Long corpId);

    /**
     * 租户列表
     * @param sysTenantFilter
     * @return
     */
    List<SysTenantDto> list(SysTenantFilter sysTenantFilter);

    /**
     * 设置委托商
     * @param corpId
     * @param userId
     */
    void setDemander(Long corpId, Long userId);
}
