package com.zjft.usp.wms.business.book.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.book.dto.BookSaleBorrowResultDto;
import com.zjft.usp.wms.business.book.filter.BookSaleBorrowFilter;
import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 销售借用待还账表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface BookSaleBorrowMapper extends BaseMapper<BookSaleBorrow> {

    /**
     * 分页查询销售借用待还账
     *
     * @author canlei
     * @param page
     * @param bookSaleBorrowFilter
     * @return
     */
    List<BookSaleBorrowResultDto> queryByPage(Page page, @Param("bookSaleBorrowFilter") BookSaleBorrowFilter bookSaleBorrowFilter);

    /**
     * 根据filter查询
     *
     * @author canlei
     * @param bookSaleBorrowFilter
     * @return
     */
    List<BookSaleBorrowResultDto> listByFilter(@Param("bookSaleBorrowFilter") BookSaleBorrowFilter bookSaleBorrowFilter);

    /**
     * 根据入库保存明细编号list查询
     *
     * @author canlei
     * @param incomeDetailSaveIdList
     * @return
     */
    List<BookSaleBorrowResultDto> listByIncomeDetailSaveIdList(@Param("incomeDetailSaveIdList") List<Long> incomeDetailSaveIdList);

}
