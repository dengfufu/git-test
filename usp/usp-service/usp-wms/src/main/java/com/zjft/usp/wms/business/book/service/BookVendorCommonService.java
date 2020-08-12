package com.zjft.usp.wms.business.book.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.book.dto.BookVendorCommonDto;
import com.zjft.usp.wms.business.book.filter.BookVendorFilter;
import com.zjft.usp.wms.business.book.model.BookVendorCommon;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 厂商账共用表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface BookVendorCommonService extends IService<BookVendorCommon> {

    /**
     * 查询厂商返回明细
     * @param bookVendorFilter
     * @param reqParam
     * @return
     */
    ListWrapper<BookVendorCommonDto> queryBookVendor(BookVendorFilter bookVendorFilter, ReqParam reqParam);
}
