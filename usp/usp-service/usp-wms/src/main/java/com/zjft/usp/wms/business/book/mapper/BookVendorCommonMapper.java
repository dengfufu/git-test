package com.zjft.usp.wms.business.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.book.dto.BookVendorCommonDto;
import com.zjft.usp.wms.business.book.filter.BookVendorFilter;
import com.zjft.usp.wms.business.book.model.BookVendorCommon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 厂商账共用表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface BookVendorCommonMapper extends BaseMapper<BookVendorCommon> {

    /**
     * 分页查询厂商帐
     *
     * @param page
     * @param bookVendorFilter
     * @return java.util.List<com.zjft.usp.wms.business.book.dto.BookVendorCommonDto>
     * @author zphu
     * @date 2019/12/10 10:09
     * @throws
    **/
    List<BookVendorCommonDto> listByPage(Page page, @Param("bookVendorFilter") BookVendorFilter bookVendorFilter);

}
