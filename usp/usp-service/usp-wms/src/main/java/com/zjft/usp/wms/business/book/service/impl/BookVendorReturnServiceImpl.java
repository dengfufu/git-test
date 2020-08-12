package com.zjft.usp.wms.business.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.wms.business.book.mapper.BookVendorReturnMapper;
import com.zjft.usp.wms.business.book.model.BookVendorReturn;
import com.zjft.usp.wms.business.book.service.BookVendorReturnService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 厂商应还销账表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class BookVendorReturnServiceImpl extends ServiceImpl<BookVendorReturnMapper, BookVendorReturn> implements BookVendorReturnService {

    /***
     * 更新是否销账状态
     *
     * @author Qiugm
     * @date 2019-11-19
     * @param bookVendorReturn
     * @return boolean
     */
    @Override
    public boolean updateReversed(BookVendorReturn bookVendorReturn) {
        UpdateWrapper<BookVendorReturn> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", bookVendorReturn.getId());
        return this.update(bookVendorReturn, updateWrapper);
    }

}
