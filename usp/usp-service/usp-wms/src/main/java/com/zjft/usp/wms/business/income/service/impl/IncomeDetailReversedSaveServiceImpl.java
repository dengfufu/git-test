package com.zjft.usp.wms.business.income.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.book.dto.BookSaleBorrowResultDto;
import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import com.zjft.usp.wms.business.book.service.BookSaleBorrowService;
import com.zjft.usp.wms.business.income.model.IncomeDetailReversedSave;
import com.zjft.usp.wms.business.income.mapper.IncomeDetailReversedSaveMapper;
import com.zjft.usp.wms.business.income.service.IncomeDetailReversedSaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 入库明细通用销账暂存表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-11-28
 */
@Service
public class IncomeDetailReversedSaveServiceImpl extends ServiceImpl<IncomeDetailReversedSaveMapper, IncomeDetailReversedSave> implements IncomeDetailReversedSaveService {

    @Autowired
    private BookSaleBorrowService bookSaleBorrowService;

    /**
     * 根据保存入库明细号查询销账明细编号list
     *
     * @author canlei
     * @param detailId
     * @return
     */
    @Override
    public List<Long> listBookIdByDetailId(Long detailId) {
        List<Long> list = new ArrayList<>();
        if (LongUtil.isZero(detailId)) {
            return list;
        }
        List<IncomeDetailReversedSave> incomeDetailReversedSaves = this.list(new QueryWrapper<IncomeDetailReversedSave>()
                .eq("detail_id", detailId));
        if (CollectionUtil.isNotEmpty(incomeDetailReversedSaves)) {
            list = incomeDetailReversedSaves.stream().map(incomeDetailReversedSave -> incomeDetailReversedSave.getBookId()).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 根据保存的主表id删除销账明细
     *
     * @author canlei
     * @param detailIdList
     */
    @Override
    public void deleteByDetailIdList(List<Long> detailIdList) {
        if (CollectionUtil.isEmpty(detailIdList)) {
            throw new AppException("入库保存单明细编号不能为空");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("detail_id", detailIdList);
        this.remove(queryWrapper);
    }

    /**
     * 根据入库单号删除
     *
     * @author canlei
     * @param incomeId
     */
    @Override
    public void deleteByIncomeId(Long incomeId) {
        if (LongUtil.isZero(incomeId)) {
            throw new AppException("入库单编号不能为空");
        }
        this.baseMapper.deleteByIncomeId(incomeId);
    }

    /**
     * 根据入库保存的detailIdList获取detailId和List<BookSaleBorrowResultDto>的映射
     *
     * @author canlei
     * @param detailIdList
     * @param reqParam
     * @return
     */
    @Override
    public Map<Long, List<BookSaleBorrowResultDto>> mapSaleBorrowByDetailIdList(List<Long> detailIdList, ReqParam reqParam) {
        Map<Long, List<BookSaleBorrowResultDto>> map = new HashMap<>();
        if (CollectionUtil.isEmpty(detailIdList)) {
            return map;
        }
        List<BookSaleBorrowResultDto> dtoList = this.bookSaleBorrowService.listByIncomeDetailSaveIdList(detailIdList, reqParam);

        if (CollectionUtil.isNotEmpty(dtoList)) {
            dtoList.forEach(bookSaleBorrowResultDto -> {
                if (map.containsKey(bookSaleBorrowResultDto.getIncomeDetailSaveId())) {
                    List<BookSaleBorrowResultDto> tempList = map.get(bookSaleBorrowResultDto.getIncomeDetailSaveId());
                    tempList.add(bookSaleBorrowResultDto);
                    map.put(bookSaleBorrowResultDto.getIncomeDetailSaveId(), tempList);
                } else {
                    List<BookSaleBorrowResultDto> tempList = new ArrayList<>();
                    tempList.add(bookSaleBorrowResultDto);
                    map.put(bookSaleBorrowResultDto.getIncomeDetailSaveId(), tempList);
                }
            });
        }

        return map;
    }
}
