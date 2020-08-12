package com.zjft.usp.wms.business.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.wms.business.book.model.BookVendorReturn;

/**
 * <p>
 * 厂商应还销账表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface BookVendorReturnService extends IService<BookVendorReturn> {

    /**
     * 更新是否销账状态
     *
     * @author Qiugm
     * @date 2019-11-19
     * @param bookVendorReturn
     * @return boolean
     */
    boolean updateReversed(BookVendorReturn bookVendorReturn);
}
