package com.zjft.usp.wms.business.income.composite.impl;

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
import com.zjft.usp.wms.business.book.dto.BookSaleBorrowResultDto;
import com.zjft.usp.wms.business.book.filter.BookSaleBorrowFilter;
import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import com.zjft.usp.wms.business.book.service.BookSaleBorrowService;
import com.zjft.usp.wms.business.income.composite.IncomeCompoService;
import com.zjft.usp.wms.business.income.dto.IncomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.income.dto.IncomeStatDto;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;
import com.zjft.usp.wms.business.income.enums.IncomeStatusEnum;
import com.zjft.usp.wms.business.income.external.StockExternalService;
import com.zjft.usp.wms.business.income.filter.IncomeFilter;
import com.zjft.usp.wms.business.income.model.*;
import com.zjft.usp.wms.business.income.service.*;
import com.zjft.usp.wms.business.income.strategy.factory.IncomeStrategyFactory;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.model.FlowInstanceNode;
import com.zjft.usp.wms.flow.model.FlowInstanceNodeHandler;
import com.zjft.usp.wms.flow.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物料入库服务类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-13 13:58
 **/
@Transactional(rollbackFor = Exception.class)
@Component
public class IncomeCompoServiceImpl implements IncomeCompoService {
    @Autowired
    IncomeStrategyFactory incomeStrategyFactory;
    @Autowired
    private IncomeWareCommonService incomeWareCommonService;
    @Autowired
    private IncomeWarePurchaseService incomeWarePurchaseService;
    @Autowired
    private IncomeMainCommonSaveService incomeMainCommonSaveService;
    @Autowired
    private IncomeMainPurchaseSaveService incomeMainPurchaseSaveService;
    @Autowired
    private IncomeDetailCommonSaveService incomeDetailCommonSaveService;
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
    private FlowInstanceService flowInstanceService;
    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;
    @Autowired
    private WareCatalogService wareCatalogService;
    @Autowired
    private WareBrandService wareBrandService;
    @Autowired
    private FlowInstanceTraceService flowInstanceTraceService;
    @Autowired
    private FlowInstanceCompoService flowInstanceCompoService;
    @Autowired
    private StockExternalService stockAndLogTxExternalService;
    @Autowired
    private BookSaleBorrowService bookSaleBorrowService;
    @Autowired
    private IncomeDetailReversedSaveService incomeDetailReversedSaveService;
    @Autowired
    private IncomeWareReversedService incomeWareReversedService;
    @Autowired
    private FlowInstanceNodeHandlerService flowInstanceNodeHandlerService;

    /**
     * 保存入库申请
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void save(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        incomeStrategyFactory.getStrategy(IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + incomeWareCommonDto.getSmallClassId()
                .toString()).save(incomeWareCommonDto, userInfo, reqParam);
    }

    /**
     * 修改入库申请
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void update(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        incomeStrategyFactory.getStrategy(IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + incomeWareCommonDto.getSmallClassId().toString())
                .update(incomeWareCommonDto, userInfo, reqParam);
    }

    /**
     * 添加入库申请
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void add(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        incomeStrategyFactory.getStrategy(IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + incomeWareCommonDto.getSmallClassId().toString())
                .add(incomeWareCommonDto, userInfo, reqParam);
    }

    /**
     * 删除入库申请
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void delete(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        IncomeMainCommonSave incomeMainCommonSave =
                this.incomeMainCommonSaveService.getById(incomeWareCommonDto.getId());
        if (incomeMainCommonSave != null) {
            // 删除销账明细
            this.incomeDetailReversedSaveService.deleteByIncomeId(incomeWareCommonDto.getId());
            this.incomeMainCommonSaveService.removeById(incomeWareCommonDto.getId());
            this.incomeMainPurchaseSaveService.removeById(incomeWareCommonDto.getId());
            this.incomeDetailCommonSaveService.deleteByIncomeId(incomeWareCommonDto.getId());
        } else {
            // 更新销账表数据
            List<BookSaleBorrow> bookSaleBorrowList = new ArrayList<>();
            List<Long> bookIdList = this.incomeWareReversedService.listBookIdByDetailId(incomeWareCommonDto.getId());
            if (CollectionUtil.isNotEmpty(bookIdList)) {
                bookIdList.forEach(bookId -> {
                    BookSaleBorrow bookSaleBorrow = new BookSaleBorrow();
                    bookSaleBorrow.setId(bookId);
                    bookSaleBorrow.setReversed("N");
                    bookSaleBorrow.setReverseDetailId(0L);
                    bookSaleBorrowList.add(bookSaleBorrow);
                });
                this.bookSaleBorrowService.updateBatchById(bookSaleBorrowList);
            }
            // 删除销账明细
            this.incomeWareReversedService.deleteByDetailId(incomeWareCommonDto.getId());

            this.incomeWareCommonService.removeById(incomeWareCommonDto.getId());
            this.incomeWarePurchaseService.removeById(incomeWareCommonDto.getId());
            this.flowInstanceService.removeById(incomeWareCommonDto.getFlowInstanceId());
            this.flowInstanceNodeService.removeByFlowId(incomeWareCommonDto.getFlowInstanceId());
        }
    }

    /**
     * 审批入库申请
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void audit(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        incomeStrategyFactory.getStrategy(IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + incomeWareCommonDto.getSmallClassId().toString())
                .audit(incomeWareCommonDto, userInfo, reqParam);
    }

    /**
     * 批量审批入库申请
     *
     * @author Qiugm
     * @date 2019-11-22
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void batchAudit(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        // 批量审批不区分业务小类
        if (incomeWareCommonDto == null) {
            throw new AppException("审批信息为空，请确认");
        }
        if (CollectionUtil.isEmpty(incomeWareCommonDto.getIncomeIdList())) {
            throw new AppException("请选择需要审批的入库单");
        }
        Map<Long, IncomeWareCommon> idAndIncomeMap = this.incomeWareCommonService.mapByCorpIdAndIncomeIds(reqParam.getCorpId(),
                incomeWareCommonDto.getIncomeIdList());
        List<IncomeWareCommon> updateStatusIncomeList = new ArrayList<>();
        for (Long incomeId : incomeWareCommonDto.getIncomeIdList()) {
            IncomeWareCommon incomeWareCommon = idAndIncomeMap.get(incomeId);
            if (incomeWareCommon != null) {
                IncomeWareCommonDto singleDto = new IncomeWareCommonDto();
                BeanUtils.copyProperties(incomeWareCommon, singleDto);
                singleDto.setNodeEndTypeId(incomeWareCommonDto.getNodeEndTypeId());
                singleDto.setAuditNote(incomeWareCommonDto.getAuditNote());
                // 结束当前结点
                this.incomeStrategyFactory.getStrategy(IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + incomeWareCommon.getSmallClassId().toString())
                        .endCurrentNode(singleDto, userInfo);
                // 流程结束，调整库存
                if (this.flowInstanceCompoService.isEndFlow(singleDto.getFlowInstanceId())) {
                    this.stockAndLogTxExternalService.adjustStock(singleDto);

                    // 已入库
                    incomeWareCommon.setIncomeStatus(IncomeStatusEnum.HAD_INCOME.getCode());
                    updateStatusIncomeList.add(incomeWareCommon);
                }
            }
        }
        // 批量更新入库状态
        if(CollectionUtil.isNotEmpty(updateStatusIncomeList)) {
            this.incomeWareCommonService.updateBatchById(updateStatusIncomeList);
        }
    }

    /**
     * 查看申请详情
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param applyId
     * @param reqParam
     * @return com.zjft.usp.wms.business.income.dto.IncomeMainCommonDto
     */
    @Override
    public IncomeWareCommonDto viewIncome(Long applyId, ReqParam reqParam) {
        IncomeWareCommonDto incomeWareCommonDto = new IncomeWareCommonDto();
        IncomeMainCommonSave incomeMainCommonSave = this.incomeMainCommonSaveService.getById(applyId);
        if (incomeMainCommonSave != null) {
            BeanUtils.copyProperties(incomeMainCommonSave, incomeWareCommonDto);
            List<IncomeDetailCommonSave> incomeDetailCommonSaveList =
                    this.incomeDetailCommonSaveService.list(new QueryWrapper<IncomeDetailCommonSave>().
                            eq("income_id", applyId));
            List<IncomeDetailCommonSaveDto> incomeDetailCommonSaveDtoList =
                    JsonUtil.parseArray(JsonUtil.toJson(incomeDetailCommonSaveList), IncomeDetailCommonSaveDto.class);
            incomeWareCommonDto.setIncomeDetailCommonSaveDtoList(incomeDetailCommonSaveDtoList);
            // 查询采购信息
            IncomeMainPurchaseSave incomeMainPurchaseSave = this.incomeMainPurchaseSaveService.getById(applyId);
            if (incomeMainPurchaseSave != null) {
                BeanUtils.copyProperties(incomeMainPurchaseSave, incomeWareCommonDto);
            }
            // 查询销账信息
            if (CollectionUtil.isNotEmpty(incomeDetailCommonSaveDtoList)) {
                List<Long> detailIdList = incomeDetailCommonSaveDtoList.stream()
                        .map(incomeDetailCommonSaveDto -> incomeDetailCommonSaveDto.getId()).collect(Collectors.toList());
                Map<Long, List<BookSaleBorrowResultDto>> bookMap = incomeDetailReversedSaveService.mapSaleBorrowByDetailIdList(detailIdList, reqParam);
                incomeDetailCommonSaveDtoList.forEach(incomeDetailCommonSaveDto -> {
                    incomeDetailCommonSaveDto.setBookSaleBorrowResultDtoList(bookMap.get(incomeDetailCommonSaveDto.getId()));
                });
            }
        } else {
            IncomeWareCommon incomeWareCommon = this.incomeWareCommonService.getById(applyId);
            BeanUtils.copyProperties(incomeWareCommon, incomeWareCommonDto);
            // 查询采购信息
            IncomeWarePurchase incomeWarePurchase = this.incomeWarePurchaseService.getById(applyId);
            if (incomeWarePurchase != null) {
                BeanUtils.copyProperties(incomeWarePurchase, incomeWareCommonDto);
            }
            // 查询销账记录
            BookSaleBorrowFilter bookSaleBorrowFilter = new BookSaleBorrowFilter();
            bookSaleBorrowFilter.setReverseDetailId(applyId);
            bookSaleBorrowFilter.setCorpId(reqParam.getCorpId());
            List<BookSaleBorrowResultDto> bookSaleBorrowResultDtoList = bookSaleBorrowService.listByFilter(bookSaleBorrowFilter);
            incomeWareCommonDto.setBookSaleBorrowResultDtoList(bookSaleBorrowResultDtoList);
        }
        // 查询流程信息
        if (LongUtil.isNotZero(incomeWareCommonDto.getFlowInstanceId())) {
            FlowInstance flowInstance = this.flowInstanceService.getById(incomeWareCommonDto.getFlowInstanceId());
            FlowInstanceNode currentNode = this.flowInstanceNodeService.getById(flowInstance.getCurrentNodeId());
            // 当前节点名称
            incomeWareCommonDto.setCurNodeName(currentNode == null ? "" : currentNode.getTemplateNodeName());
            // 审批人列表
            if (flowInstance != null) {
                List<FlowInstanceNodeHandler> handlers = this.flowInstanceNodeHandlerService.listByInstanceNodeId(flowInstance.getCurrentNodeId());
                if (CollectionUtil.isNotEmpty(handlers)) {
                    incomeWareCommonDto.setAuditUserList(handlers.stream()
                            .map(flowInstanceNodeHandler -> flowInstanceNodeHandler.getAssignedToBy()).collect(Collectors.toList()));
                }
            }
            // 流程节点列表
            incomeWareCommonDto.setFlowInstanceNodeList(this.flowInstanceNodeService.listAllBy(incomeWareCommonDto.getFlowInstanceId()));
            // 流程审批追踪列表
            incomeWareCommonDto.setFlowInstanceTraceDtoList(this.flowInstanceTraceService.listSortBy(incomeWareCommonDto.getFlowInstanceId(), reqParam));
        }

        addExtraAttribute(incomeWareCommonDto);
        return incomeWareCommonDto;
    }

    /**
     * 增加附加属性
     *
     * @author Qiugm
     * @date 2019-11-18
     * @param incomeWareCommonDto
     * @return void
     */
    private void addExtraAttribute(IncomeWareCommonDto incomeWareCommonDto) {
        if (incomeWareCommonDto != null) {
            List<Long> userIdList = new ArrayList<>();
            userIdList.add(incomeWareCommonDto.getCreateBy());
            userIdList.add(incomeWareCommonDto.getUpdateBy());

            Map<Long, String> userMap = new HashMap<>(16);
            if (CollectionUtil.isNotEmpty(userIdList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
                userMap = userMapResult.getData();
            }

            Map<Integer, String> largeClassIdAndNameMap =
                    this.largeClassService.mapClassIdAndName(incomeWareCommonDto.getCorpId());
            Map<Integer, String> smallClassIdAndNameMap =
                    this.smallClassService.mapClassIdAndName(incomeWareCommonDto.getCorpId());
            Map<Long, String> depotIdAndNameMap =
                    this.wareDepotService.mapDepotIdAndName(incomeWareCommonDto.getCorpId());
            Map<Long, WareModel> modelMap = this.wareModelService.mapIdAndModel(incomeWareCommonDto.getCorpId());
            Map<Long, String> catalogIdAndNameMap =
                    this.wareCatalogService.mapCatalogIdAndName(incomeWareCommonDto.getCorpId());
            Map<Long, String> brandIdAndNameMap =
                    this.wareBrandService.mapBrandIdAndName(incomeWareCommonDto.getCorpId());
            Map<Integer, String> wareStatusMap = this.wareStatusService.mapIdAndName(incomeWareCommonDto.getCorpId());
            Map<Long, String> warePropertyRightMap =
                    this.warePropertyRightService.mapIdAndName(incomeWareCommonDto.getCorpId());

            incomeWareCommonDto.setCreateNameBy(userMap.get(incomeWareCommonDto.getCreateBy()));
            incomeWareCommonDto.setUpdateNameBy(userMap.get(incomeWareCommonDto.getUpdateBy()));
            incomeWareCommonDto.setLargeClassName(largeClassIdAndNameMap.get(incomeWareCommonDto.getLargeClassId()));
            incomeWareCommonDto.setSmallClassName(smallClassIdAndNameMap.get(incomeWareCommonDto.getSmallClassId()));
            incomeWareCommonDto.setDepotName(depotIdAndNameMap.get(incomeWareCommonDto.getDepotId()));
            incomeWareCommonDto.setPropertyRightName(warePropertyRightMap.get(incomeWareCommonDto.getPropertyRight()));
            if (CollectionUtil.isNotEmpty(incomeWareCommonDto.getIncomeDetailCommonSaveDtoList())) {
                for (IncomeDetailCommonSaveDto incomeDetailCommonSaveDto :
                        incomeWareCommonDto.getIncomeDetailCommonSaveDtoList()) {
                    incomeDetailCommonSaveDto.setStatusName(wareStatusMap.get(incomeDetailCommonSaveDto.getStatus()));
                    incomeDetailCommonSaveDto.setSituationName(SituationEnum.getNameByCode(incomeDetailCommonSaveDto.getSituation()));
                    WareModel wareModel = modelMap.get(incomeDetailCommonSaveDto.getModelId());
                    if (wareModel != null) {
                        incomeDetailCommonSaveDto.setModelName(wareModel.getName());
                        incomeDetailCommonSaveDto.setCatalogId(wareModel.getCatalogId());
                        incomeDetailCommonSaveDto.setCatalogName(catalogIdAndNameMap.get(wareModel.getCatalogId()));
                        incomeDetailCommonSaveDto.setBrandId(wareModel.getBrandId());
                        incomeDetailCommonSaveDto.setBrandName(brandIdAndNameMap.get(wareModel.getBrandId()));
                    }
                }
            } else {
                incomeWareCommonDto.setStatusName(wareStatusMap.get(incomeWareCommonDto.getStatus()));
                if (IntUtil.isNotZero(incomeWareCommonDto.getSituation())) {
                    incomeWareCommonDto.setSituationName(SituationEnum.getNameByCode(incomeWareCommonDto.getSituation()));
                }
                WareModel wareModel = modelMap.get(incomeWareCommonDto.getModelId());
                if (wareModel != null) {
                    incomeWareCommonDto.setModelName(wareModel.getName());
                    incomeWareCommonDto.setCatalogId(wareModel.getCatalogId());
                    incomeWareCommonDto.setBrandId(wareModel.getBrandId());
                    incomeWareCommonDto.setCatalogName(catalogIdAndNameMap.get(wareModel.getCatalogId()));
                    incomeWareCommonDto.setBrandName(brandIdAndNameMap.get(wareModel.getBrandId()));
                }
            }
        }
    }

    /**
     * 查询入库申请记录
     *
     * @author Qiugm
     * @date 2019-11-11
     * @param incomeFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.business.income.dto.IncomeMainCommonDto>
     */
    @Override
    public ListWrapper<IncomeWareCommonDto> listIncome(IncomeFilter incomeFilter) {
        Page<IncomeWareCommon> page = new Page(incomeFilter.getPageNum(), incomeFilter.getPageSize());
        List<IncomeWareCommonDto> incomeWareCommonDtoList = this.incomeWareCommonService.listByPage(page, incomeFilter);

        if (CollectionUtil.isNotEmpty(incomeWareCommonDtoList)) {
            List<Long> flowInstanceIdList = incomeWareCommonDtoList.stream()
                    .map(incomeWareCommonDto -> incomeWareCommonDto.getFlowInstanceId()).collect(Collectors.toList());
            Map<Long, List<Long>> instanceIdAndAuditUserListMap = this.flowInstanceNodeHandlerService.mapCurAuditUserList(flowInstanceIdList);
            incomeWareCommonDtoList.forEach(incomeWareCommonDto -> {
                incomeWareCommonDto.setAuditUserList(instanceIdAndAuditUserListMap.get(incomeWareCommonDto.getFlowInstanceId()));
            });
        }

        addExtraAttribute(incomeWareCommonDtoList, incomeFilter);

        return ListWrapper.<IncomeWareCommonDto>builder()
                .list(incomeWareCommonDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 查询暂存未提交的入库单
     *
     * @author Qiugm
     * @date 2019-11-19
     * @param incomeFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto>
     */
    @Override
    public ListWrapper<IncomeWareCommonDto> listSaveIncome(IncomeFilter incomeFilter) {
        Page<IncomeMainCommonSave> page = new Page<>(incomeFilter.getPageNum(), incomeFilter.getPageSize());
        List<IncomeMainCommonSave> incomeMainCommonSaveList = this.incomeMainCommonSaveService.listByPage(page,
                incomeFilter);
        List<IncomeWareCommonDto> incomeWareCommonDtoList =
                JsonUtil.parseArray(JsonUtil.toJson(incomeMainCommonSaveList), IncomeWareCommonDto.class);
        addExtraAttribute(incomeWareCommonDtoList, incomeFilter);

        return ListWrapper.<IncomeWareCommonDto>builder()
                .list(incomeWareCommonDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 根据状态统计数量
     *
     * @author canlei
     * @param incomeFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public Map<Integer, Long> countByIncomeStatus(IncomeFilter incomeFilter, UserInfo userInfo, ReqParam reqParam) {
        // 所有状态都需要查
        incomeFilter.setIncomeStatuses(null);
        List<IncomeStatDto> countList = this.incomeWareCommonService.countByIncomeStatus(incomeFilter, userInfo.getUserId());
        IncomeStatDto incomeStatSaveDto = this.incomeMainCommonSaveService.countSave(userInfo, reqParam);
        Map<Integer, Long> map = new HashMap<>();
        List<Integer> incomeStatusList = IncomeStatusEnum.listIncomeStatus();
        if (CollectionUtil.isNotEmpty(countList)) {
            countList.forEach(incomeStatDto -> {
                map.put(incomeStatDto.getIncomeStatus(), incomeStatDto.getIncomeNumber());
            });
        }
        map.put(0, incomeStatSaveDto == null ? 0L : incomeStatSaveDto.getIncomeNumber());
        incomeStatusList.forEach(incomeStatus -> {
            if (!map.containsKey(incomeStatus)) {
                map.put(incomeStatus, 0L);
            }
        });
        return map;
    }

    /***
     * 增加附加属性
     *
     * @author Qiugm
     * @date 2019-11-18
     * @param incomeWareCommonDtoList
     * @param incomeFilter
     * @return void
     */
    private void addExtraAttribute(List<IncomeWareCommonDto> incomeWareCommonDtoList, IncomeFilter incomeFilter) {
        if (CollectionUtil.isNotEmpty(incomeWareCommonDtoList)) {
            List<Long> userIdList = new ArrayList<Long>();
            for (IncomeWareCommonDto incomeWareCommonDto : incomeWareCommonDtoList) {
                userIdList.add(incomeWareCommonDto.getCreateBy());
                userIdList.add(incomeWareCommonDto.getUpdateBy());
                if (CollectionUtil.isNotEmpty(incomeWareCommonDto.getAuditUserList())) {
                    userIdList.addAll(incomeWareCommonDto.getAuditUserList());
                }
            }

            Map<Long, String> userMap = new HashMap<Long, String>(16);
            if (CollectionUtil.isNotEmpty(userIdList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
                userMap = userMapResult.getData();
            }

            Map<Integer, String> largeClassIdAndNameMap =
                    this.largeClassService.mapClassIdAndName(incomeFilter.getCorpId());
            Map<Integer, String> smallClassIdAndNameMap =
                    this.smallClassService.mapClassIdAndName(incomeFilter.getCorpId());
            Map<Long, String> depotIdAndNameMap = this.wareDepotService.mapDepotIdAndName(incomeFilter.getCorpId());
            Map<Long, WareModel> modelMap = this.wareModelService.mapIdAndModel(incomeFilter.getCorpId());
            Map<Long, String> catalogIdAndNameMap =
                    this.wareCatalogService.mapCatalogIdAndName(incomeFilter.getCorpId());
            Map<Long, String> brandIdAndNameMap = this.wareBrandService.mapBrandIdAndName(incomeFilter.getCorpId());
            Map<Integer, String> wareStatusMap = this.wareStatusService.mapIdAndName(incomeFilter.getCorpId());
            Map<Long, String> warePropertyRightMap =
                    this.warePropertyRightService.mapIdAndName(incomeFilter.getCorpId());

            for (IncomeWareCommonDto incomeWareCommonDto : incomeWareCommonDtoList) {
                WareModel model = modelMap.get(incomeWareCommonDto.getModelId());
                if (model != null) {
                    incomeWareCommonDto.setModelName(model.getName());
                    incomeWareCommonDto.setCatalogId(model.getCatalogId());
                    incomeWareCommonDto.setBrandId(model.getBrandId());
                    incomeWareCommonDto.setCatalogName(catalogIdAndNameMap.get(model.getCatalogId()));
                    incomeWareCommonDto.setBrandName(brandIdAndNameMap.get(model.getBrandId()));
                }
                incomeWareCommonDto.setCreateNameBy(userMap.get(incomeWareCommonDto.getCreateBy()));
                incomeWareCommonDto.setUpdateNameBy(userMap.get(incomeWareCommonDto.getUpdateBy()));
                incomeWareCommonDto.setLargeClassName(largeClassIdAndNameMap.get(incomeWareCommonDto.getLargeClassId()));
                incomeWareCommonDto.setSmallClassName(smallClassIdAndNameMap.get(incomeWareCommonDto.getSmallClassId()));
                incomeWareCommonDto.setDepotName(depotIdAndNameMap.get(incomeWareCommonDto.getDepotId()));
                incomeWareCommonDto.setPropertyRightName(warePropertyRightMap.get(incomeWareCommonDto.getPropertyRight()));
                incomeWareCommonDto.setStatusName(wareStatusMap.get(incomeWareCommonDto.getStatus()));
                if (IntUtil.isNotZero(incomeWareCommonDto.getSituation())) {
                    incomeWareCommonDto.setSituationName(SituationEnum.getNameByCode(incomeWareCommonDto.getSituation()));
                }
                if (CollectionUtil.isNotEmpty(incomeWareCommonDto.getAuditUserList())) {
                    String auditUserNames = "";
                    for(Long auditUserId: incomeWareCommonDto.getAuditUserList()) {
                        auditUserNames += userMap.get(auditUserId) == null ? "" : userMap.get(auditUserId) + ",";
                    }
                    incomeWareCommonDto.setAuditUserNames(auditUserNames.substring(0, auditUserNames.lastIndexOf(",")));
                }
            }
        }
    }

}