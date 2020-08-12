package com.zjft.usp.wms.business.book.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.book.dto.BookVendorCommonDto;
import com.zjft.usp.wms.business.book.filter.BookVendorFilter;
import com.zjft.usp.wms.business.book.mapper.BookVendorCommonMapper;
import com.zjft.usp.wms.business.book.model.BookVendorCommon;
import com.zjft.usp.wms.business.book.service.BookVendorCommonService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 厂商账共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class BookVendorCommonServiceImpl extends ServiceImpl<BookVendorCommonMapper, BookVendorCommon> implements BookVendorCommonService {

    @Override
    public ListWrapper<BookVendorCommonDto> queryBookVendor(BookVendorFilter bookVendorFilter, ReqParam reqParam) {
        Page<BookVendorCommon> page = new Page(bookVendorFilter.getPageNum(), bookVendorFilter.getPageSize());
        List<BookVendorCommonDto> bookVendorCommonDtoList = this.baseMapper.listByPage(page,bookVendorFilter);
        return ListWrapper.<BookVendorCommonDto>builder()
                .list(bookVendorCommonDtoList)
                .total(page.getTotal())
                .build();
    }
}
