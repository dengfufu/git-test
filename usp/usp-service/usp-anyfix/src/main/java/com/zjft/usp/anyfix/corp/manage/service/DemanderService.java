package com.zjft.usp.anyfix.corp.manage.service;

import com.zjft.usp.anyfix.corp.manage.dto.DemanderDto;

import java.util.List;
import java.util.Map;

/**
 * 服务委托方服务类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/11/1 10:53
 */
public interface DemanderService {

    /**
     * 根据企业编号获得关联的服务委托方列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/11/1 10:56
     **/
    List<DemanderDto> listDemander(Long corpId);


    Map<Long,String> getDemanderIdNameMap(Long corpId);

    /**
     * 根据企业编号获得关联的服务委托方编号
     * @param corpId
     * @return
     */
    List<Long> demanderCorpIdList(Long corpId);

}
