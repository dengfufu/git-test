package com.zjft.usp.wms.business.consign.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import com.zjft.usp.wms.baseinfo.enums.LargeClassEnum;
import com.zjft.usp.wms.baseinfo.model.ExpressCompany;
import com.zjft.usp.wms.baseinfo.model.WareModel;
import com.zjft.usp.wms.baseinfo.service.*;
import com.zjft.usp.wms.business.consign.composite.ConsignCompoService;
import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.consign.dto.ConsignMainDto;
import com.zjft.usp.wms.business.consign.enums.TransportTypeEnum;
import com.zjft.usp.wms.business.consign.filter.ConsignFilter;
import com.zjft.usp.wms.business.consign.model.ConsignDetail;
import com.zjft.usp.wms.business.consign.model.ConsignMain;
import com.zjft.usp.wms.business.consign.service.ConsignDetailService;
import com.zjft.usp.wms.business.consign.service.ConsignMainService;
import com.zjft.usp.wms.business.consign.strategy.factory.ConsignStrategyFactory;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import com.zjft.usp.wms.business.trans.service.TransWareCommonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zphu
 * @date 2019/12/4 9:32
 * @Version 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class ConsignCompoServiceImpl implements ConsignCompoService {

    @Autowired
    ConsignStrategyFactory consignStrategyFactory;
    @Autowired
    private ConsignDetailService consignDetailService;
    @Autowired
    private ConsignMainService consignMainService;
    @Resource
    private UasFeignService uasFeignService;
    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private TransWareCommonService transWareCommonService;
    @Autowired
    private WareModelService wareModelService;
    @Autowired
    private WareCatalogService wareCatalogService;
    @Autowired
    private WareBrandService wareBrandService;
    @Autowired
    private WareDepotService wareDepotService;

    @Override
    public void add(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam) {
        consignStrategyFactory.getStrategy(ConsignStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + consignMainDto.getLargeClassId().toString()).add(consignMainDto,userInfo,reqParam);
    }

    @Override
    public void receive(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam) {
        consignStrategyFactory.getStrategy(ConsignStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + consignMainDto.getLargeClassId().toString()).receive(consignMainDto,userInfo,reqParam);
    }

    @Override
    public List<ConsignDetailDto> findByFormDetailId(Long formDetailId, ReqParam reqParam) {
        if(LongUtil.isNotZero(formDetailId)){
            List<ConsignDetailDto> consignDetailDtoList = new ArrayList<>();
            List<ConsignDetail> consignDetailList = this.consignDetailService.list(new QueryWrapper<ConsignDetail>().eq("form_detail_id", formDetailId).orderByAsc("form_detail_id"));
            if(CollectionUtil.isNotEmpty(consignDetailList)){
                // 获取主表id，主表id不同代表不同发货单
                List<Long> consignMainIdList = consignDetailList.stream().map(e -> e.getConsignMainId()).collect(Collectors.toList());
                Collection<ConsignMain> consignMains = this.consignMainService.listByIds(consignMainIdList);
                if(CollectionUtil.isNotEmpty(consignMains)){
                    consignDetailList.forEach(consignDetail -> {
                        ConsignDetailDto consignDetailDto = new ConsignDetailDto();
                        // 顺序不可变，否则id会被覆盖
                        consignMains.forEach(consignMain -> {
                            if(consignMain.getId().equals(consignDetail.getConsignMainId())){
                                BeanUtils.copyProperties(consignMain,consignDetailDto);
                            }
                        });
                        BeanUtils.copyProperties(consignDetail,consignDetailDto);
                        consignDetailDtoList.add(consignDetailDto);
                    });
                }
                this.addExtraAttribute(consignDetailDtoList,reqParam.getCorpId());
            return consignDetailDtoList;
            }
        }
        return null;
    }

    @Override
    public ListWrapper<ConsignDetailDto> listByPage(ConsignFilter consignFilter, UserInfo userInfo, ReqParam reqParam) {
        Page<ConsignDetailDto> page = new Page(consignFilter.getPageNum(), consignFilter.getPageSize());
        List<ConsignDetailDto> consignDetailDtoList = this.consignDetailService.listByPage(consignFilter, page);
        addExtraAttribute(consignDetailDtoList,reqParam.getCorpId());

        return ListWrapper.<ConsignDetailDto>builder()
                .list(consignDetailDtoList)
                .total(page.getTotal())
                .build();
    }

    public void addExtraAttribute(List<ConsignDetailDto> consignDetailDtoList, Long corpId){
        if(CollectionUtil.isNotEmpty(consignDetailDtoList)){
            Long cropId = consignDetailDtoList.get(0).getCorpId();

            List<Long> userIdList = new ArrayList<Long>();
            List<String> areaList = new ArrayList<>();
            List<Long> tranIdList = new ArrayList<>();
            for (ConsignDetailDto consignDetailDto : consignDetailDtoList) {
                userIdList.add(consignDetailDto.getConsignBy());
                userIdList.add(consignDetailDto.getReceiverId());
                userIdList.add(consignDetailDto.getSignBy());
                if (StrUtil.isNotBlank(consignDetailDto.getReceiveDistrict())) {
                    if (StrUtil.isNotBlank(consignDetailDto.getReceiveDistrict()) && consignDetailDto.getReceiveDistrict().length() >= 2) {
                        areaList.add(consignDetailDto.getReceiveDistrict().substring(0, 2));
                    }
                    if (StrUtil.isNotBlank(consignDetailDto.getReceiveDistrict()) && consignDetailDto.getReceiveDistrict().length() >= 4) {
                        areaList.add(consignDetailDto.getReceiveDistrict().substring(0, 4));
                    }
                    if (StrUtil.isNotBlank(consignDetailDto.getReceiveDistrict()) && consignDetailDto.getReceiveDistrict().length() >= 6) {
                        areaList.add(consignDetailDto.getReceiveDistrict().substring(0, 6));
                    }
                }
                // 是否调拨发货
                if(consignDetailDto.getLargeClassId() == LargeClassEnum.TRANS.getCode()){
                    tranIdList.add(consignDetailDto.getFormDetailId());
                }
            }
            Map<Long, String> userMap = new HashMap<Long, String>(16);
            if (CollectionUtil.isNotEmpty(userIdList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
                userMap = userMapResult.getData();
            }

            Map<Long, ExpressCompany> mapIdAndExpress = this.expressCompanyService.mapIdAndObject(cropId);
            Map<String, String> mapAreaName = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaList)).getData();
            Map<Long, TransWareCommon> mapIdAndTrans = this.transWareCommonService.mapIdAndObject(tranIdList);

            Map<Long, WareModel> modelMap =  this.wareModelService.mapIdAndModel(corpId);;
            Map<Long, String> catalogIdAndNameMap = this.wareCatalogService.mapCatalogIdAndName(corpId);;
            Map<Long, String> brandIdAndNameMap = this.wareBrandService.mapBrandIdAndName(corpId);
            Map<Long, String> depotIdAndNameMap = this.wareDepotService.mapDepotIdAndName(corpId);

            for (ConsignDetailDto consignDetailDto : consignDetailDtoList) {
                consignDetailDto.setConsignByName(userMap.get(consignDetailDto.getConsignBy()));
                consignDetailDto.setSignByName(userMap.get(consignDetailDto.getSignBy()));
                consignDetailDto.setFromDepotName(depotIdAndNameMap.get(consignDetailDto.getFromDepotId()));

                if(LongUtil.isNotZero(consignDetailDto.getReceiverId())){
                    consignDetailDto.setReceiveName(userMap.get(consignDetailDto.getReceiverId()));
                }
                if(mapIdAndExpress.get(consignDetailDto.getExpressCorpId()) != null){
                    consignDetailDto.setExpressCorpName(mapIdAndExpress.get(consignDetailDto.getExpressCorpId()).getName());
                }
                if(mapIdAndTrans.get(consignDetailDto.getFormDetailId()) != null){
                    TransWareCommon wareCommon = mapIdAndTrans.get(consignDetailDto.getFormDetailId());
                    consignDetailDto.setTransCode(wareCommon.getTransCode());
                    if(wareCommon.getModelId() != null){
                        WareModel model = modelMap.get(wareCommon.getModelId());
                        consignDetailDto.setModelName(model.getName());
                        consignDetailDto.setCatalogName(catalogIdAndNameMap.get(model.getCatalogId()));
                        consignDetailDto.setBrandName(brandIdAndNameMap.get(model.getBrandId()));
                    }
                }
                if (StrUtil.isNotBlank(consignDetailDto.getReceiveDistrict())) {
                    String address = "";
                    if (StrUtil.isNotBlank(consignDetailDto.getReceiveDistrict()) && consignDetailDto.getReceiveDistrict().length() >= 2) {
                        address = mapAreaName.get(consignDetailDto.getReceiveDistrict().substring(0, 2));
                    }
                    if (StrUtil.isNotBlank(consignDetailDto.getReceiveDistrict()) && consignDetailDto.getReceiveDistrict().length() >= 4) {
                        address = address + mapAreaName.get(consignDetailDto.getReceiveDistrict().substring(0, 4));
                    }
                    if (StrUtil.isNotBlank(consignDetailDto.getReceiveDistrict()) && consignDetailDto.getReceiveDistrict().length() >= 6) {
                        address = address + mapAreaName.get(consignDetailDto.getReceiveDistrict().substring(0, 6));
                    }
                    consignDetailDto.setReceiveDistrictAddress(address);
                }
                consignDetailDto.setTransportTypeName(TransportTypeEnum.getNameByCode(consignDetailDto.getTransportTypeId()));
            }
        }
    }
}
