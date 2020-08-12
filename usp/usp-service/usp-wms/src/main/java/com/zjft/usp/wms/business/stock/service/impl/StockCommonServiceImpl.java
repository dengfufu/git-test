package com.zjft.usp.wms.business.stock.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.enums.SituationEnum;
import com.zjft.usp.wms.baseinfo.model.WareModel;
import com.zjft.usp.wms.baseinfo.service.*;
import com.zjft.usp.wms.business.stock.dto.StockCommonResultDto;
import com.zjft.usp.wms.business.stock.filter.StockCommonFilter;
import com.zjft.usp.wms.business.stock.mapper.StockCommonMapper;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.stock.service.StockCommonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 库存实时总账共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class StockCommonServiceImpl extends ServiceImpl<StockCommonMapper, StockCommon> implements StockCommonService {

    @Autowired
    private WareCatalogService wareCatalogService;
    @Autowired
    private WareBrandService wareBrandService;
    @Resource
    private WareModelService wareModelService;
    @Resource
    private WareDepotService wareDepotService;
    @Resource
    private WarePropertyRightService warePropertyRightService;
    @Resource
    private WareStatusService wareStatusService;

    @Override
    public void addStockCommon(StockCommon stockCommon) {

        super.save(stockCommon);
    }

    @Override
    public ListWrapper<StockCommonResultDto> pageBy(StockCommonFilter stockCommonFilter, UserInfo userInfo) {
        Page<StockCommon> page = new Page(stockCommonFilter.getPageNum(), stockCommonFilter.getPageSize());
        List<StockCommonResultDto> stockCommonList = this.baseMapper.listByPage(stockCommonFilter,page);
        addExtraAttribute(stockCommonList, stockCommonFilter);

        return ListWrapper.<StockCommonResultDto>builder()
                .list(stockCommonList)
                .total(page.getTotal())
                .build();
    }

    @Override
    public List<StockCommonResultDto> listByStockIds(Collection<Long> stockIdList,Long corpId) {
        if(CollectionUtil.isNotEmpty(stockIdList)){
            stockIdList.forEach(stockId -> {
                if(LongUtil.isZero(stockId)){
                    stockIdList.remove(stockId);
                }
            });
            Collection<StockCommon> stockCommonList = this.listByIds(stockIdList);
            List<StockCommonResultDto> resultDtoList = this.getResultDtoList((List<StockCommon>) stockCommonList);
            StockCommonFilter stockCommonFilter = new StockCommonFilter();
            stockCommonFilter.setCorpId(corpId);
            addExtraAttribute(resultDtoList,stockCommonFilter);
            return resultDtoList;
        }
        return null;
    }

    @Override
    public Map<Long, StockCommon> mapIdAndObject(Collection<Long> idList) {
        Collection<StockCommon> stockCommons = this.listByIds(idList);
        if(CollectionUtil.isNotEmpty(stockCommons)){
            Map<Long, StockCommon> mapIdAndObject = new HashMap<>();
            for(StockCommon stockCommon : stockCommons){
                mapIdAndObject.put(stockCommon.getId(),stockCommon);
            }
            return mapIdAndObject;
        }
        return null;
    }

    @Override
    public List<StockCommon> selectQtyByTrans(Map<String,Object> map) {
        return this.baseMapper.selectByTrans(map);
    }

    private List<StockCommonResultDto> getResultDtoList(List<StockCommon> stockCommonList) {
        List<StockCommonResultDto> resultDtoList = new ArrayList<>();
        for (StockCommon stockCommon : stockCommonList) {
            StockCommonResultDto resultDto = new StockCommonResultDto();
            BeanUtils.copyProperties(stockCommon, resultDto);
            resultDtoList.add(resultDto);
        }
        return resultDtoList;
    }

    private void addExtraAttribute(List<StockCommonResultDto> stockCommonList, StockCommonFilter stockCommonFilter) {
        if (CollectionUtil.isNotEmpty(stockCommonList)) {
            Map<Long, String> depotIdAndNameMap = this.wareDepotService.mapDepotIdAndName(stockCommonFilter.getCorpId());
            Map<Long, WareModel> modelMap = this.wareModelService.mapIdAndModel(stockCommonFilter.getCorpId());
            Map<Long, String> catalogIdAndNameMap =
                    this.wareCatalogService.mapCatalogIdAndName(stockCommonFilter.getCorpId());
            Map<Long, String> brandIdAndNameMap = this.wareBrandService.mapBrandIdAndName(stockCommonFilter.getCorpId());
            Map<Integer, String> wareStatusMap = this.wareStatusService.mapIdAndName(stockCommonFilter.getCorpId());
            Map<Long, String> warePropertyRightMap =
                    this.warePropertyRightService.mapIdAndName(stockCommonFilter.getCorpId());

            for (StockCommonResultDto stockCommonResultDto : stockCommonList) {
                WareModel model = modelMap.get(stockCommonResultDto.getModelId());
                if (model != null) {
                    stockCommonResultDto.setModelName(model.getName());
                    stockCommonResultDto.setCatalogName(catalogIdAndNameMap.get(model.getCatalogId()));
                    stockCommonResultDto.setBrandName(brandIdAndNameMap.get(model.getBrandId()));
                }
                stockCommonResultDto.setDepotName(depotIdAndNameMap.get(stockCommonResultDto.getDepotId()));
                stockCommonResultDto.setPropertyRightName(warePropertyRightMap.get(stockCommonResultDto.getPropertyRight()));
                stockCommonResultDto.setStatusName(wareStatusMap.get(stockCommonResultDto.getStatus()));
                if (IntUtil.isNotZero(stockCommonResultDto.getSituation())) {
                    stockCommonResultDto.setSituationName(SituationEnum.getNameByCode(stockCommonResultDto.getSituation()));
                }
            }
        }
    }


}
