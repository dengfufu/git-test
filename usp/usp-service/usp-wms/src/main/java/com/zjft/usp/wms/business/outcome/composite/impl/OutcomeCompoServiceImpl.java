package com.zjft.usp.wms.business.outcome.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import com.zjft.usp.wms.baseinfo.enums.SituationEnum;
import com.zjft.usp.wms.baseinfo.model.WareModel;
import com.zjft.usp.wms.baseinfo.service.*;
import com.zjft.usp.wms.business.outcome.composite.OutcomeCompoService;
import com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeMainCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeStatDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.enums.OutcomeStatusEnum;
import com.zjft.usp.wms.business.outcome.external.OutcomeStockExternalService;
import com.zjft.usp.wms.business.outcome.filter.OutcomeFilter;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.business.outcome.service.*;
import com.zjft.usp.wms.business.outcome.strategy.common.factory.OutcomeStrategyFactory;
import com.zjft.usp.wms.business.stock.dto.StockCommonResultDto;
import com.zjft.usp.wms.business.stock.service.StockCommonService;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.enums.NodeTypeEnum;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeHandlerService;
import com.zjft.usp.wms.flow.service.FlowInstanceNodeService;
import com.zjft.usp.wms.flow.service.FlowInstanceTraceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 处理出库业务逻辑
 *
 * @Author: JFZOU
 * @Date: 2019-11-13 15:04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OutcomeCompoServiceImpl implements OutcomeCompoService {

    @Autowired
    private OutcomeStrategyFactory outcomeStrategyFactory;
    @Autowired
    private OutcomeWareCommonService outcomeWareCommonService;
    @Autowired
    private OutcomeDetailCommonSaveService outcomeDetailCommonSaveService;
    @Autowired
    private OutcomeDetailReturnSaveVendorService outcomeDetailReturnSaveVendorService;
    @Autowired
    private OutcomeMainSaleSaveService outcomeMainSaleSaveService;
    @Autowired
    private OutcomeMainReturnVendorSaveService outcomeMainReturnVendorSaveService;
    @Autowired
    private OutcomeMainCommonSaveService outcomeMainCommonSaveService;
    @Resource
    private LargeClassService largeClassService;
    @Resource
    private SmallClassService smallClassService;
    @Resource
    private WareModelService wareModelService;
    @Resource
    private WareDepotService wareDepotService;
    @Resource
    private WarePropertyRightService warePropertyRightService;
    @Resource
    private WareStatusService wareStatusService;
    @Resource
    private UasFeignService uasFeignService;
    @Autowired
    private FlowInstanceCompoService flowInstanceCompoService;
    @Autowired
    private FlowInstanceTraceService flowInstanceTraceService;
    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;
    @Autowired
    private FlowInstanceNodeHandlerService flowInstanceNodeHandlerService;
    @Autowired
    private WareCatalogService wareCatalogService;
    @Autowired
    private WareBrandService wareBrandService;
    @Autowired
    private OutcomeStockExternalService outcomeStockExternalService;
    @Autowired
    private StockCommonService stockCommonService;

    @Override
    public void add(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        outcomeStrategyFactory.getStrategy(OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + outcomeWareCommonDto.getSmallClassId().toString()).add(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public void delete(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        outcomeStrategyFactory.getStrategy(OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + outcomeWareCommonDto.getSmallClassId().toString()).delete(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public void update(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        outcomeStrategyFactory.getStrategy(OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + outcomeWareCommonDto.getSmallClassId().toString()).update(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public void batchUpdate(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        outcomeStrategyFactory.getStrategy(OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + outcomeWareCommonDto.getSmallClassId().toString()).update(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public void save(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        outcomeStrategyFactory.getStrategy(OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + outcomeWareCommonDto.getSmallClassId().toString()).save(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public ListWrapper<OutcomeWareCommonDto> list(OutcomeFilter outcomeFilter, UserInfo userInfo, ReqParam reqParam) {
        Page<OutcomeWareCommon> page = new Page(outcomeFilter.getPageNum(), outcomeFilter.getPageSize());
        List<OutcomeWareCommonDto> outcomeWareCommonDtoList = this.outcomeWareCommonService.listByPage(outcomeFilter, reqParam, page);
        if (CollectionUtil.isNotEmpty(outcomeWareCommonDtoList)) {
            List<Long> flowInstanceIdList = outcomeWareCommonDtoList.stream()
                    .map(outcomeWareCommonDto -> outcomeWareCommonDto.getFlowInstanceId()).collect(Collectors.toList());
            Map<Long, List<Long>> instanceIdAndAuditUserListMap = this.flowInstanceNodeHandlerService.mapCurAuditUserList(flowInstanceIdList);
            outcomeWareCommonDtoList.forEach(outcomeWareCommonDto -> {
                outcomeWareCommonDto.setAuditUserList(instanceIdAndAuditUserListMap.get(outcomeWareCommonDto.getFlowInstanceId()));
            });
        }
        addExtraAttribute(outcomeWareCommonDtoList, outcomeFilter);

        return ListWrapper.<OutcomeWareCommonDto>builder()
                .list(outcomeWareCommonDtoList)
                .total(page.getTotal())
                .build();
    }

    @Override
    public OutcomeWareCommonDto detail(Long outcomeId, UserInfo userInfo, ReqParam reqParam) {
        OutcomeFilter outcomeFilter = new OutcomeFilter();
        outcomeFilter.setId(outcomeId);
        OutcomeWareCommonDto outcomeWareCommonDto = this.outcomeWareCommonService.getById(outcomeFilter);

        // 查询流程信息
        if (LongUtil.isNotZero(outcomeWareCommonDto.getFlowInstanceId())) {
            outcomeWareCommonDto.setFlowInstanceNodeList(this.flowInstanceNodeService.listAllBy(outcomeWareCommonDto.getFlowInstanceId()));
            outcomeWareCommonDto.setFlowInstanceTraceDtoList(this.flowInstanceTraceService.listSortBy(outcomeWareCommonDto.getFlowInstanceId(), reqParam));
        }
        
        addExtraAttribute(outcomeWareCommonDto);
        return outcomeWareCommonDto;
    }

    @Override
    public void audit(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        outcomeStrategyFactory.getStrategy(OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + outcomeWareCommonDto.getSmallClassId().toString()).audit(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public void batchAudit(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        // 批量审批不区分业务小类
        if (CollectionUtil.isEmpty(outcomeWareCommonDto.getIdList())) {
            throw new AppException("请选择需要审批的入库单");
        }
        Map<Long, OutcomeWareCommon> idAndIncomeMap = this.outcomeWareCommonService.mapIdAndObject(outcomeWareCommonDto.getIdList());
        List<OutcomeWareCommon> updateStatusOutcomeList = new ArrayList<>();
        for (Long outcomeId : outcomeWareCommonDto.getIdList()) {
            OutcomeWareCommon outcomeWareCommon = idAndIncomeMap.get(outcomeId);
            if (outcomeWareCommon != null) {
                OutcomeWareCommonDto singleDto = new OutcomeWareCommonDto();
                BeanUtils.copyProperties(outcomeWareCommon, singleDto);
                singleDto.setNodeEndTypeId(outcomeWareCommonDto.getNodeEndTypeId());
                singleDto.setAuditNote(outcomeWareCommonDto.getAuditNote());
                // 结束当前结点
                this.outcomeStrategyFactory.getStrategy(OutcomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + outcomeWareCommon.getSmallClassId().toString()).endCurrentNode(singleDto, userInfo);
                // 流程结束，调整库存
                if (this.flowInstanceCompoService.isEndFlow(singleDto.getFlowInstanceId())) {
                    if(outcomeWareCommonDto.getIsAdjust()){
                        this.outcomeStockExternalService.adjustStock(outcomeWareCommon,userInfo);
                    }
                    // 已出库
                    outcomeWareCommon.setOutcomeStatus(OutcomeStatusEnum.HAD_OUTCOME.getCode());
                    updateStatusOutcomeList.add(outcomeWareCommon);
                }
            }
        }
        // 批量跟新入库状态
        if(CollectionUtil.isNotEmpty(updateStatusOutcomeList)) {
            this.outcomeWareCommonService.updateBatchById(updateStatusOutcomeList);
        }
    }

    @Override
    public ListWrapper<OutcomeMainCommonSaveDto> listSave(OutcomeFilter outcomeFilter, UserInfo userInfo, ReqParam reqParam) {
        Page<OutcomeWareCommon> page = new Page(outcomeFilter.getPageNum(), outcomeFilter.getPageSize());
        List<OutcomeMainCommonSaveDto> outcomeMainCommonSaveDtoList = this.outcomeMainCommonSaveService.listSaveByPage(outcomeFilter, page);
        addMainExtraAttribute(outcomeMainCommonSaveDtoList, outcomeFilter);

        return ListWrapper.<OutcomeMainCommonSaveDto>builder()
                .list(outcomeMainCommonSaveDtoList)
                .total(page.getTotal())
                .build();
    }

    @Override
    public OutcomeMainCommonSaveDto detailSave(Long outcomeId, UserInfo userInfo, ReqParam reqParam) {
        OutcomeMainCommonSaveDto outcomeMainCommonSaveDto = this.outcomeMainCommonSaveService.getDetailById(outcomeId);
        if(outcomeMainCommonSaveDto == null){
            throw new AppException("该记录不存在");
        }
        List<OutcomeDetailCommonSaveDto> outcomeDetailCommonSaveDtoList = this.outcomeDetailCommonSaveService.listByOutcomeId(outcomeId);
        outcomeMainCommonSaveDto.setOutcomeDetailCommonSaveDtoList(outcomeDetailCommonSaveDtoList);
        List<Long> stockIdList = outcomeDetailCommonSaveDtoList.stream().map(e -> e.getStockId()).collect(Collectors.toList());
        List<StockCommonResultDto> stockCommons = this.stockCommonService.listByStockIds(stockIdList,reqParam.getCorpId());
        if(CollectionUtil.isNotEmpty(stockCommons)){
            stockCommons.forEach(stockCommon -> {
                outcomeDetailCommonSaveDtoList.forEach(outcomeDetailCommonSaveDto -> {
                    if(stockCommon.getId().equals(outcomeDetailCommonSaveDto.getStockId())){
                        stockCommon.setQuantity(outcomeDetailCommonSaveDto.getQuantity());
                    }
                });
            });
        }
        outcomeMainCommonSaveDto.setStockCommonResultDtoList(stockCommons);
        return outcomeMainCommonSaveDto;
    }

    @Override
    public void deleteSave(Long outcomeId) {
        this.outcomeMainCommonSaveService.removeById(outcomeId);
        this.outcomeMainSaleSaveService.removeById(outcomeId);
        this.outcomeMainReturnVendorSaveService.removeById(outcomeId);
        List<OutcomeDetailCommonSave> outcomeDetailCommonSaveList = this.outcomeDetailCommonSaveService.list(new QueryWrapper<OutcomeDetailCommonSave>().eq("outcome_id", outcomeId));
        if(CollectionUtil.isNotEmpty(outcomeDetailCommonSaveList)) {
            List<Long> detailIdList = outcomeDetailCommonSaveList.stream().map(e -> e.getId()).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(detailIdList)){
                this.outcomeDetailCommonSaveService.removeByIds(detailIdList);
                this.outcomeDetailReturnSaveVendorService.removeByIds(detailIdList);
            }
        }
    }

    @Override
    public void updateSave(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        this.deleteSave(outcomeWareCommonDto.getMainId());
        this.save(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public Map<Integer, Long> countByOutcomeStatus(OutcomeFilter outcomeFilter, UserInfo userInfo, ReqParam reqParam) {
        // 所有状态都需要查
        List<OutcomeStatDto> countList = this.outcomeWareCommonService.countByStatus(outcomeFilter);
        OutcomeStatDto outcomeStatSaveDto = this.outcomeMainCommonSaveService.countSave(userInfo, reqParam);
        Map<Integer, Long> map = new HashMap<>();
        List<Integer> listNodeType = NodeTypeEnum.listNodeType();
        if (CollectionUtil.isNotEmpty(countList)) {
            countList.forEach(outcomeStatDto -> {
                if(outcomeStatDto.getFlowNodeType() != null){
                    map.put(outcomeStatDto.getFlowNodeType(), outcomeStatDto.getOutcomeNumber());
                }else{
                    map.put(1, outcomeStatDto.getOutcomeNumber());
                }
            });
        }
        map.put(0, outcomeStatSaveDto == null ? 0L : outcomeStatSaveDto.getOutcomeNumber());
        listNodeType.forEach(nodeType -> {
            if (!map.containsKey(nodeType)) {
                map.put(nodeType, 0L);
            }
        });
        return map;
    }

    /***
     * 增加附加属性
     *
     * @author zphu
     * @date 2019-11-25
     * @param outcomeWareCommonDtoList
     * @param outcomeFilter
     * @return void
     */
    private void addExtraAttribute(List<OutcomeWareCommonDto> outcomeWareCommonDtoList, OutcomeFilter outcomeFilter) {
        if (CollectionUtil.isNotEmpty(outcomeWareCommonDtoList)) {
            List<Long> userIdList = new ArrayList<Long>();

            for (OutcomeWareCommonDto outcomeWareCommonDto : outcomeWareCommonDtoList) {
                userIdList.add(outcomeWareCommonDto.getCreateBy());
                userIdList.add(outcomeWareCommonDto.getUpdateBy());
                if (CollectionUtil.isNotEmpty(outcomeWareCommonDto.getAuditUserList())) {
                    userIdList.addAll(outcomeWareCommonDto.getAuditUserList());
                }
            }

            Map<Long, String> userMap = new HashMap<Long, String>(16);
            if (CollectionUtil.isNotEmpty(userIdList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
                userMap = userMapResult.getData();
            }

            Map<Integer, String> largeClassIdAndNameMap =
                    this.largeClassService.mapClassIdAndName(outcomeFilter.getCorpId());
            Map<Integer, String> smallClassIdAndNameMap =
                    this.smallClassService.mapClassIdAndName(outcomeFilter.getCorpId());
            Map<Long, String> depotIdAndNameMap = this.wareDepotService.mapDepotIdAndName(outcomeFilter.getCorpId());
            Map<Long, WareModel> modelMap = this.wareModelService.mapIdAndModel(outcomeFilter.getCorpId());
            Map<Long, String> catalogIdAndNameMap =
                    this.wareCatalogService.mapCatalogIdAndName(outcomeFilter.getCorpId());
            Map<Long, String> brandIdAndNameMap = this.wareBrandService.mapBrandIdAndName(outcomeFilter.getCorpId());
            Map<Integer, String> wareStatusMap = this.wareStatusService.mapIdAndName(outcomeFilter.getCorpId());
            Map<Long, String> warePropertyRightMap =
                    this.warePropertyRightService.mapIdAndName(outcomeFilter.getCorpId());

            for (OutcomeWareCommonDto outcomeWareCommonDto : outcomeWareCommonDtoList) {
                WareModel model = modelMap.get(outcomeWareCommonDto.getModelId());
                if (model != null) {
                    outcomeWareCommonDto.setModelName(model.getName());
                    outcomeWareCommonDto.setCatalogName(catalogIdAndNameMap.get(model.getCatalogId()));
                    outcomeWareCommonDto.setBrandName(brandIdAndNameMap.get(model.getBrandId()));
                }
                outcomeWareCommonDto.setCreateNameBy(userMap.get(outcomeWareCommonDto.getCreateBy()));
                outcomeWareCommonDto.setUpdateNameBy(userMap.get(outcomeWareCommonDto.getUpdateBy()));
                outcomeWareCommonDto.setLargeClassName(largeClassIdAndNameMap.get(outcomeWareCommonDto.getLargeClassId()));
                outcomeWareCommonDto.setSmallClassName(smallClassIdAndNameMap.get(outcomeWareCommonDto.getSmallClassId()));
                outcomeWareCommonDto.setDepotName(depotIdAndNameMap.get(outcomeWareCommonDto.getDepotId()));
                outcomeWareCommonDto.setPropertyRightName(warePropertyRightMap.get(outcomeWareCommonDto.getPropertyRight()));
                outcomeWareCommonDto.setStatusName(wareStatusMap.get(outcomeWareCommonDto.getStatus()));
                if (IntUtil.isNotZero(outcomeWareCommonDto.getSituation())) {
                    outcomeWareCommonDto.setSituationName(SituationEnum.getNameByCode(outcomeWareCommonDto.getSituation()));
                }
                if (CollectionUtil.isNotEmpty(outcomeWareCommonDto.getAuditUserList())) {
                    String auditUserNames = "";
                    for(Long auditUserId: outcomeWareCommonDto.getAuditUserList()) {
                        auditUserNames += userMap.get(auditUserId) == null ? "" : userMap.get(auditUserId) + ",";
                    }
                    outcomeWareCommonDto.setAuditUserNames(auditUserNames.substring(0, auditUserNames.lastIndexOf(",")));
                }
            }
        }
    }

    private void addMainExtraAttribute(List<OutcomeMainCommonSaveDto> outcomeMainCommonSaveDtoList, OutcomeFilter outcomeFilter) {
        if (CollectionUtil.isNotEmpty(outcomeMainCommonSaveDtoList)) {
            List<Long> userIdList = new ArrayList<Long>();

            for (OutcomeMainCommonSaveDto outcomeMainCommonSaveDto : outcomeMainCommonSaveDtoList) {
                userIdList.add(outcomeMainCommonSaveDto.getUpdateBy());
            }

            Map<Long, String> userMap = new HashMap<Long, String>(16);
            if (CollectionUtil.isNotEmpty(userIdList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
                userMap = userMapResult.getData();
            }

            Map<Integer, String> smallClassIdAndNameMap =
                    this.smallClassService.mapClassIdAndName(outcomeFilter.getCorpId());
            Map<Long, String> depotIdAndNameMap = this.wareDepotService.mapDepotIdAndName(outcomeFilter.getCorpId());

            for (OutcomeMainCommonSaveDto outcomeMainCommonSaveDto : outcomeMainCommonSaveDtoList) {
                outcomeMainCommonSaveDto.setUpdateNameBy(userMap.get(outcomeMainCommonSaveDto.getUpdateBy()));
                outcomeMainCommonSaveDto.setSmallClassName(smallClassIdAndNameMap.get(outcomeMainCommonSaveDto.getSmallClassId()));
                outcomeMainCommonSaveDto.setDepotName(depotIdAndNameMap.get(outcomeMainCommonSaveDto.getDepotId()));

            }
        }
    }
    
    private void addExtraAttribute(OutcomeWareCommonDto outcomeWareCommonDto) {
        if (outcomeWareCommonDto != null) {
            List<Long> userIdList = new ArrayList<>();
            userIdList.add(outcomeWareCommonDto.getCreateBy());
            userIdList.add(outcomeWareCommonDto.getUpdateBy());

            Map<Long, String> userMap = new HashMap<>(16);
            if (CollectionUtil.isNotEmpty(userIdList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
                userMap = userMapResult.getData();
            }

            Map<Integer, String> largeClassIdAndNameMap =
                    this.largeClassService.mapClassIdAndName(outcomeWareCommonDto.getCorpId());
            Map<Integer, String> smallClassIdAndNameMap =
                    this.smallClassService.mapClassIdAndName(outcomeWareCommonDto.getCorpId());
            Map<Long, String> depotIdAndNameMap =
                    this.wareDepotService.mapDepotIdAndName(outcomeWareCommonDto.getCorpId());
            Map<Long, WareModel> modelMap = this.wareModelService.mapIdAndModel(outcomeWareCommonDto.getCorpId());
            Map<Long, String> catalogIdAndNameMap =
                    this.wareCatalogService.mapCatalogIdAndName(outcomeWareCommonDto.getCorpId());
            Map<Long, String> brandIdAndNameMap =
                    this.wareBrandService.mapBrandIdAndName(outcomeWareCommonDto.getCorpId());
            Map<Integer, String> wareStatusMap = this.wareStatusService.mapIdAndName(outcomeWareCommonDto.getCorpId());
            Map<Long, String> warePropertyRightMap =
                    this.warePropertyRightService.mapIdAndName(outcomeWareCommonDto.getCorpId());

            outcomeWareCommonDto.setCatalogName(catalogIdAndNameMap.get(outcomeWareCommonDto));
            outcomeWareCommonDto.setCreateNameBy(userMap.get(outcomeWareCommonDto.getCreateBy()));
            outcomeWareCommonDto.setUpdateNameBy(userMap.get(outcomeWareCommonDto.getUpdateBy()));
            outcomeWareCommonDto.setLargeClassName(largeClassIdAndNameMap.get(outcomeWareCommonDto.getLargeClassId()));
            outcomeWareCommonDto.setSmallClassName(smallClassIdAndNameMap.get(outcomeWareCommonDto.getSmallClassId()));
            outcomeWareCommonDto.setDepotName(depotIdAndNameMap.get(outcomeWareCommonDto.getDepotId()));
            outcomeWareCommonDto.setPropertyRightName(warePropertyRightMap.get(outcomeWareCommonDto.getPropertyRight()));
            outcomeWareCommonDto.setStatusName(wareStatusMap.get(outcomeWareCommonDto.getStatus()));
            outcomeWareCommonDto.setSituationName(SituationEnum.getNameByCode(outcomeWareCommonDto.getSituation()));
            WareModel wareModel = modelMap.get(outcomeWareCommonDto.getModelId());
            if (wareModel != null) {
                outcomeWareCommonDto.setModelName(wareModel.getName());
                outcomeWareCommonDto.setCatalogName(catalogIdAndNameMap.get(wareModel.getCatalogId()));
                outcomeWareCommonDto.setBrandName(brandIdAndNameMap.get(wareModel.getBrandId()));
            }

        }
    }
}
