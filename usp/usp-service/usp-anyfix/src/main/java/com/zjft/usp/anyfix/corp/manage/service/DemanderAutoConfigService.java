package com.zjft.usp.anyfix.corp.manage.service;

import com.zjft.usp.anyfix.corp.manage.dto.DemanderAutoConfigDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderAutoConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 委托商自动化配置表 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-07-20
 */
public interface DemanderAutoConfigService extends IService<DemanderAutoConfig> {

    /**
     * 根据id获取Dto
     *
     * @param id
     * @return
     */
    DemanderAutoConfigDto findById(Long id);

    /**
     * 配置委托商自动化设置
     *
     * @param demanderAutoConfigDto
     * @param userId
     * @param corpId
     */
    void autoConfig(DemanderAutoConfigDto demanderAutoConfigDto, Long userId, Long corpId);

    /**
     * 查询委托商和自动化配置的映射
     *
     * @param serviceCorp
     * @return
     */
    Map<Long, DemanderAutoConfigDto> mapDemanderAndAutoConfig(Long serviceCorp);

    /**
     * 查询服务商和自动化配置的映射
     *
     * @param demanderCorp
     * @return
     */
    Map<Long, DemanderAutoConfigDto> mapServiceAndAutoConfig(Long demanderCorp);

    /**
     * 根据委托商和服务商查询
     *
     * @param demanderCorp
     * @param serviceCorp
     * @return
     */
    DemanderAutoConfigDto findByDemanderAndService(Long demanderCorp, Long serviceCorp);

}
