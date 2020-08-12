package com.zjft.usp.anyfix.corp.manage.composite;

import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceManagerDto;

import java.util.List;

/**
 * 委托商与服务商客户经理关系聚合类
 *
 * @author zgpi
 * @date 2020/6/15 13:36
 */
public interface DemanderServiceManagerCompoService {

    /**
     * 编辑客户经理
     *
     * @param demanderServiceManagerDto
     * @return
     * @author zgpi
     * @date 2020/6/15 13:41
     **/
    void editDemanderServiceManager(DemanderServiceManagerDto demanderServiceManagerDto);

    /**
     * 获得客户经理
     *
     * @param demanderServiceManagerDto
     * @return
     * @author zgpi
     * @date 2020/6/15 14:36
     **/
    DemanderServiceManagerDto findDemanderServiceManager(DemanderServiceManagerDto demanderServiceManagerDto);

    /**
     * 客户经理对应的委托商编号列表
     *
     * @param corpId
     * @param manager
     * @return
     * @author zgpi
     * @date 2020/6/22 14:58
     **/
    List<Long> listDemanderCorpByManager(Long corpId, Long manager);

    /**
     * 客户经理查询负责的委托商列表
     *
     * @param corpId
     * @param manager
     * @return
     */
    List<DemanderServiceDto> listDemanderByManager(Long corpId, Long manager);

}
