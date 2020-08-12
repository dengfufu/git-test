package com.zjft.usp.anyfix.corp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.manage.model.DemanderServiceManager;

import java.util.List;

/**
 * <p>
 * 委托商与服务商客户经理关系表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-06-15
 */
public interface DemanderServiceManagerService extends IService<DemanderServiceManager> {

    /**
     * 获得客户经理的委托商列表
     *
     * @param serviceCorp
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/6/17 10:30
     **/
    List<Long> listDemanderCorpByManager(Long serviceCorp, Long userId);

}
