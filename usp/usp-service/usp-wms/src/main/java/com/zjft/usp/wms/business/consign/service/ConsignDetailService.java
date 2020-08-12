package com.zjft.usp.wms.business.consign.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.consign.filter.ConsignFilter;
import com.zjft.usp.wms.business.consign.model.ConsignDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 发货明细信息表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-22
 */
public interface ConsignDetailService extends IService<ConsignDetail> {

    /**
     * 分页查询发货列表
     *
     * @param consignFilter
     * @param page
     * @return java.util.List<com.zjft.usp.wms.business.consign.dto.ConsignDetailDto>
     * @author zphu
     * @date 2019/12/6 11:10
     * @throws
     **/
    List<ConsignDetailDto> listByPage(ConsignFilter consignFilter, Page page);
}
