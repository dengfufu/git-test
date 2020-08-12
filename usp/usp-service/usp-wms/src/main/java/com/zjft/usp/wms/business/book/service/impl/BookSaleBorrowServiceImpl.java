package com.zjft.usp.wms.business.book.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import com.zjft.usp.wms.baseinfo.model.WareModel;
import com.zjft.usp.wms.baseinfo.service.*;
import com.zjft.usp.wms.business.book.dto.BookSaleBorrowResultDto;
import com.zjft.usp.wms.business.book.filter.BookSaleBorrowFilter;
import com.zjft.usp.wms.business.book.mapper.BookSaleBorrowMapper;
import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import com.zjft.usp.wms.business.book.service.BookSaleBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 销售借用待还账表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class BookSaleBorrowServiceImpl extends ServiceImpl<BookSaleBorrowMapper, BookSaleBorrow> implements BookSaleBorrowService {

    @Autowired
    private WareCatalogService wareCatalogService;
    @Autowired
    private WareBrandService wareBrandService;
    @Autowired
    private WareModelService wareModelService;
    @Autowired
    private WarePropertyRightService warePropertyRightService;
    @Autowired
    private WareDepotService wareDepotService;
    @Autowired
    private WareStatusService wareStatusService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private BookSaleBorrowMapper bookSaleBorrowMapper;

    /**
     * 分页查询销售借用代还账
     *
     * @author canlei
     * @param bookSaleBorrowFilter
     * @return
     */
    @Override
    public ListWrapper<BookSaleBorrowResultDto> pageBy(BookSaleBorrowFilter bookSaleBorrowFilter) {
        ListWrapper<BookSaleBorrowResultDto> listWrapper = new ListWrapper<>();
        if (bookSaleBorrowFilter == null || LongUtil.isZero(bookSaleBorrowFilter.getCorpId())) {
            return listWrapper;
        }
        Page page = new Page(bookSaleBorrowFilter.getPageNum(), bookSaleBorrowFilter.getPageSize());
        List<BookSaleBorrowResultDto> bookSaleBorrowResultDtoList = this.bookSaleBorrowMapper.queryByPage(page, bookSaleBorrowFilter);

        addExtraAttribute(bookSaleBorrowResultDtoList, bookSaleBorrowFilter.getCorpId());

        listWrapper.setList(bookSaleBorrowResultDtoList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 根据filter查询销售借用待还账
     *
     * @author canlei
     * @param bookSaleBorrowFilter
     * @return
     */
    @Override
    public List<BookSaleBorrowResultDto> listByFilter(BookSaleBorrowFilter bookSaleBorrowFilter) {
        List<BookSaleBorrowResultDto> list = new ArrayList<>();
        if (LongUtil.isZero(bookSaleBorrowFilter.getCorpId())) {
            return list;
        }
        list = this.bookSaleBorrowMapper.listByFilter(bookSaleBorrowFilter);

        addExtraAttribute(list, bookSaleBorrowFilter.getCorpId());

        return list;
    }

    /**
     * 更新是否销账状态
     *
     * @author Qiugm
     * @date 2019-11-19
     * @param bookSaleBorrow
     * @return boolean
     */
    @Override
    public boolean updateReversed(BookSaleBorrow bookSaleBorrow) {
        UpdateWrapper<BookSaleBorrow> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", bookSaleBorrow.getId());
        return this.update(bookSaleBorrow, updateWrapper);
    }

    /**
     * 根据入库保存明细编号list查询
     *
     * @author canlei
     * @param incomeDetailSaveIdList
     * @param reqParam
     * @return
     */
    @Override
    public List<BookSaleBorrowResultDto> listByIncomeDetailSaveIdList(List<Long> incomeDetailSaveIdList, ReqParam reqParam) {
        List<BookSaleBorrowResultDto> dtoList = new ArrayList<>();
        if (CollectionUtil.isEmpty(incomeDetailSaveIdList)) {
            return dtoList;
        }
        dtoList = this.bookSaleBorrowMapper.listByIncomeDetailSaveIdList(incomeDetailSaveIdList);

        this.addExtraAttribute(dtoList, reqParam.getCorpId());

        return dtoList;
    }

    /**
     * 增加额外属性
     *
     * @author canlei
     * @param bookSaleBorrowResultDtoList
     * @param corpId
     * @return
     */
    protected List<BookSaleBorrowResultDto> addExtraAttribute(List<BookSaleBorrowResultDto> bookSaleBorrowResultDtoList,
                                                               Long corpId) {
        if (CollectionUtil.isNotEmpty(bookSaleBorrowResultDtoList)) {
            // 获取相关人员编号列表
            List<Long> userIdList = null;
            HashSet userIdSet = new HashSet();
            bookSaleBorrowResultDtoList.forEach(bookSaleBorrowResultDto -> {
                userIdSet.add(bookSaleBorrowResultDto.getCreateBy());
                userIdSet.add(bookSaleBorrowResultDto.getReverseBy());
            });
            userIdList = new ArrayList<>(userIdSet);

            // 相关映射
            Map<Long, String> userMap = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList)).getData();
            Map<Long, WareModel> modelMap = this.wareModelService.mapIdAndModel(corpId);
            Map<Long, String> catalogIdAndNameMap = this.wareCatalogService.mapCatalogIdAndName(corpId);
            Map<Long, String> brandIdAndNameMap = this.wareBrandService.mapBrandIdAndName(corpId);
            Map<Long, String> depotIdAndNameMap = this.wareDepotService.mapDepotIdAndName(corpId);
            Map<Long, String> propertyRightIdAndNameMap = this.warePropertyRightService.mapIdAndName(corpId);
            Map<Integer, String> wareStatusMap = this.wareStatusService.mapIdAndName(corpId);

            bookSaleBorrowResultDtoList.forEach(bookSaleBorrowResultDto -> {
                WareModel wareModel = modelMap.get(bookSaleBorrowResultDto.getModelId());
                if (wareModel != null) {
                    bookSaleBorrowResultDto.setCatalogName(catalogIdAndNameMap.get(wareModel.getCatalogId()));
                    bookSaleBorrowResultDto.setBrandName(brandIdAndNameMap.get(wareModel.getBrandId()));
                    bookSaleBorrowResultDto.setModelName(wareModel.getName());
                }
                bookSaleBorrowResultDto.setPropertyRightName(propertyRightIdAndNameMap.get(bookSaleBorrowResultDto.getPropertyRight()));
                bookSaleBorrowResultDto.setDepotName(depotIdAndNameMap.get(bookSaleBorrowResultDto.getDepotId()));
                bookSaleBorrowResultDto.setCreateByName(userMap.get(bookSaleBorrowResultDto.getCreateBy()));
                bookSaleBorrowResultDto.setReverseByName(userMap.get(bookSaleBorrowResultDto.getReverseBy()));
                bookSaleBorrowResultDto.setStatusName(wareStatusMap.get(bookSaleBorrowResultDto.getStatus()));
            });
        }
        return bookSaleBorrowResultDtoList;
    }
}
