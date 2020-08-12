package com.zjft.usp.anyfix.common.service;


import com.zjft.usp.common.dto.RightScopeDto;

import java.util.List;

/**
 * 数据范围 聚合服务接口类
 *
 * @author zgpi
 * @date 2020/3/13 16:36
 */
public interface DataScopeCompoService {

    /**
     * 获得人员范围权限列表
     *
     * @param corpId
     * @param userId
     * @param rightId
     * @return
     * @author zgpi
     * @date 2020/6/5 10:33
     **/
    List<RightScopeDto> listUserRightScope(Long corpId, Long userId, Long rightId);
}
