package com.zjft.usp.wms.business.book.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.book.dto.BookSaleBorrowResultDto;
import com.zjft.usp.wms.business.book.filter.BookSaleBorrowFilter;
import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售借用待还账表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface BookSaleBorrowService extends IService<BookSaleBorrow> {


    /**
     * 销售借用出库账务分页查询
     *
     * @param bookSaleBorrowFilter
     * @return
     */
    ListWrapper<BookSaleBorrowResultDto> pageBy(BookSaleBorrowFilter bookSaleBorrowFilter);

    /**
     * 销售借用出库账务查询
     *
     * @param bookSaleBorrowFilter
     * @return
     */
    List<BookSaleBorrowResultDto> listByFilter(BookSaleBorrowFilter bookSaleBorrowFilter);

    /**
     * 更新是否销账状态
     *
     * @author Qiugm
     * @date 2019-11-19
     * @param bookSaleBorrow
     * @return boolean
     */
    boolean updateReversed(BookSaleBorrow bookSaleBorrow);

    /**
     * 根据入库保存明细编号list查询
     *
     * @author canlei
     * @param incomeDetailSaveIdList
     * @param reqParam
     * @return
     */
    List<BookSaleBorrowResultDto> listByIncomeDetailSaveIdList(List<Long> incomeDetailSaveIdList, ReqParam reqParam);

}
