package com.zjft.usp.wms.business.consign.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.consign.filter.ConsignFilter;
import com.zjft.usp.wms.business.consign.model.ConsignDetail;
import com.zjft.usp.wms.business.consign.mapper.ConsignDetailMapper;
import com.zjft.usp.wms.business.consign.service.ConsignDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 发货明细信息表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-22
 */
@Service
public class ConsignDetailServiceImpl extends ServiceImpl<ConsignDetailMapper, ConsignDetail> implements ConsignDetailService {

    @Override
    public List<ConsignDetailDto> listByPage(ConsignFilter consignFilter, Page page) {
        return this.baseMapper.listByPage(consignFilter,page);
    }
}
