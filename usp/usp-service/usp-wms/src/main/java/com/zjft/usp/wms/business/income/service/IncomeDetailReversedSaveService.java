package com.zjft.usp.wms.business.income.service;

import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.book.dto.BookSaleBorrowResultDto;
import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import com.zjft.usp.wms.business.income.model.IncomeDetailReversedSave;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 入库明细通用销账暂存表 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-11-28
 */
public interface IncomeDetailReversedSaveService extends IService<IncomeDetailReversedSave> {

    /**
     * 根据入库保存明细编号获取销账明细编号list
     *
     * @author canlei
     * @param detailId
     * @return
     */
    List<Long> listBookIdByDetailId(Long detailId);

    /**
     * 根据入库保存明细编号列表删除
     *
     * @author canlei
     * @param detailIdList
     */
    void deleteByDetailIdList(List<Long> detailIdList);

    /**
     * 根据入库单号删除
     *
     * @author canlei
     * @param incomeId
     */
    void deleteByIncomeId(Long incomeId);

    /**
     * 根据detailIdList获取detailId和List<BookSaleBorrowResultDto>的映射
     *
     * @author canlei
     * @param detailIdList
     * @param reqParam
     * @return
     */
    Map<Long, List<BookSaleBorrowResultDto>> mapSaleBorrowByDetailIdList(List<Long> detailIdList, ReqParam reqParam);

}
