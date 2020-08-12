package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.dto.FaultTypeDto;
import com.zjft.usp.anyfix.baseinfo.filter.FaultTypeFilter;
import com.zjft.usp.anyfix.baseinfo.model.FaultType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 故障现象表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface FaultTypeService extends IService<FaultType> {

    /**
     * 查询客户可用的故障现象列表
     * @author zgpi
     * @date 2019/10/10 4:36 下午
     * @param customCorp
     * @return
     **/
    List<FaultType> listEnableFaultTypeByCorp(Long customCorp);

    /**
     * 分页查询故障现象
     * @param faultTypeFilter
     * @return
     */
    ListWrapper<FaultTypeDto> query(FaultTypeFilter faultTypeFilter);

    /**
     * 故障现象映射
     *
     * @param demanderCorp
     * @return
     * @author zgpi
     * @date 2019/11/12 18:14
     **/
    Map<Integer, String> mapIdAndName(Long demanderCorp);

    /**
     * 保存故障现象
     * @param faultType
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(FaultType faultType, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改故障现象
     * @param faultType
     * @param userInfo
     * @return
     */
    void update(FaultType faultType, UserInfo userInfo);
}
