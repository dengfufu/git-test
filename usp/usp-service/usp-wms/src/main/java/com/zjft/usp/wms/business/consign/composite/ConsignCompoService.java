package com.zjft.usp.wms.business.consign.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.consign.dto.ConsignMainDto;
import com.zjft.usp.wms.business.consign.filter.ConsignFilter;

import java.util.List;

/**
 * @author zphu
 * @date 2019/12/4 9:32
 * @Version 1.0
 **/
public interface ConsignCompoService {

    /**
     * 提交发货申请
     *
     * @param consignMainDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/12/4 9:44
     * @throws
     **/
    void add(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 货物签收(可用于单个签收，也可用于批量签收）
     *
     * @param consignMainDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu 
     * @date 2019/12/4 20:12
     * @throws 
    **/
    void receive(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据关联id查询发货记录详情
     *
     * @param formDetailId
     * @return com.zjft.usp.wms.business.consign.dto.ConsignMainDto
     * @author zphu
     * @date 2019/12/5 10:56
     * @throws
    **/
    List<ConsignDetailDto> findByFormDetailId(Long formDetailId,  ReqParam reqParam);

    /**
     * 分页查询发货列表
     *
     * @param consignFilter
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.business.consign.dto.ConsignDetailDto>
     * @author zphu
     * @date 2019/12/6 11:03
     * @throws
    **/
    ListWrapper<ConsignDetailDto> listByPage(ConsignFilter consignFilter,UserInfo userInfo,ReqParam reqParam);
}
