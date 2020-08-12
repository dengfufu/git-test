package com.zjft.usp.wms.business.income.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.StringUtils;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.enums.LargeClassEnum;
import com.zjft.usp.wms.business.book.model.BookSaleBorrow;
import com.zjft.usp.wms.business.book.service.BookSaleBorrowService;
import com.zjft.usp.wms.business.common.enums.SaveStatusEnum;
import com.zjft.usp.wms.business.income.dto.IncomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;
import com.zjft.usp.wms.business.income.enums.IncomeStatusEnum;
import com.zjft.usp.wms.business.income.external.StockExternalService;
import com.zjft.usp.wms.business.income.model.*;
import com.zjft.usp.wms.business.income.service.*;
import com.zjft.usp.wms.business.income.strategy.IncomeStrategy;
import com.zjft.usp.wms.business.income.strategy.factory.IncomeStrategyFactory;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.enums.NodeEndTypeEnum;
import com.zjft.usp.wms.flow.enums.SubmitEnum;
import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.model.FlowInstanceNodeHandler;
import com.zjft.usp.wms.flow.service.FlowInstanceService;
import com.zjft.usp.wms.generator.BusinessCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料入库策略抽象类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-14 14:46
 **/
public abstract class AbstractIncomeStrategy implements IncomeStrategy {
    @Autowired
    private IncomeMainCommonSaveService incomeMainCommonSaveService;
    @Autowired
    private IncomeDetailCommonSaveService incomeDetailCommonSaveService;
    @Autowired
    private IncomeWareCommonService incomeWareCommonService;
    @Autowired
    private IncomeMainPurchaseSaveService incomeMainPurchaseSaveService;
    @Autowired
    private IncomeWarePurchaseService incomeWarePurchaseService;
    @Resource
    private StockExternalService stockAndLogTxExternalService;
    @Autowired
    private FlowInstanceCompoService flowInstanceCompoService;
    @Autowired
    private FlowInstanceService flowInstanceService;
    @Autowired
    private IncomeDetailReversedSaveService incomeDetailReversedSaveService;
    @Autowired
    private IncomeWareReversedService incomeWareReversedService;
    @Autowired
    private BookSaleBorrowService bookSaleBorrowService;
    @Resource
    private BusinessCodeGenerator businessCodeGenerator;


    /**
     * 公司采购入库
     */
    public static final String INCOME_CORP_PURCHASE = IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "10";
    /**
     * 厂商物料入库
     */
    public static final String INCOME_VENDOR_WARE = IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "20";
    /**
     * 现有物料入库
     */
    public static final String INCOME_EXIST_WARE = IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "30";
    /**
     * 厂商返还入库
     */
    public static final String INCOME_VENDOR_RETURN = IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "40";
    /**
     * 销售借用归还入库
     */
    public static final String INCOME_SALE_LOAN_RETURN = IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "50";
    /**
     * 领用退料入库
     */
    public static final String INCOME_BORROW_RETURN = IncomeStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + "60";

    /**
     * 保存入库申请单
     *
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author Qiugm
     * @date 2019-11-13
     */
    @Override
    public void save(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        // 检查表单数据
        this.checkIncomeData(incomeWareCommonDto);
        // 保存申请单信息
        this.saveIncome(incomeWareCommonDto, userInfo, reqParam);
        // 保存申请单物料明细信息
        this.saveIncomeDetail(incomeWareCommonDto);
    }

    /**
     * 保存申请单信息
     *
     * @param incomeMainCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author Qiugm
     * @date 2019-11-21
     */
    protected void saveIncome(IncomeWareCommonDto incomeMainCommonDto, UserInfo userInfo, ReqParam reqParam) {
        IncomeMainCommonSave incomeMainCommon = new IncomeMainCommonSave();
        BeanUtils.copyProperties(incomeMainCommonDto, incomeMainCommon);
        incomeMainCommon.setId(KeyUtil.getId());
        incomeMainCommon.setLargeClassId(LargeClassEnum.INCOME.getCode());
        incomeMainCommon.setFlowInstanceId(0L);
        incomeMainCommon.setCorpId(reqParam.getCorpId());
        incomeMainCommon.setCreateBy(userInfo.getUserId());
        incomeMainCommon.setCreateTime(DateUtil.date().toTimestamp());
        incomeMainCommon.setSaveStatus(SaveStatusEnum.SAVE.getCode());
        incomeMainCommonDto.setId(incomeMainCommon.getId());
        // 添加申请单信息
        this.incomeMainCommonSaveService.save(incomeMainCommon);

        // 若有采购合同号，保存采购相关信息
        if (!StringUtils.isEmpty(incomeMainCommonDto.getContId())) {
            IncomeMainPurchaseSave incomeMainPurchaseSave = new IncomeMainPurchaseSave();
            BeanUtils.copyProperties(incomeMainCommonDto, incomeMainPurchaseSave);
            incomeMainPurchaseSave.setId(incomeMainCommon.getId());
            incomeMainPurchaseSaveService.save(incomeMainPurchaseSave);
        }
    }

    /**
     * 保存申请明细信息
     *
     * @param incomeMainCommonDto
     * @return void
     * @author Qiugm
     * @date 2019-11-21
     */
    protected void saveIncomeDetail(IncomeWareCommonDto incomeMainCommonDto) {
        if (incomeMainCommonDto != null) {
            List<IncomeDetailCommonSaveDto> incomeDetailCommonSaveDtoList = incomeMainCommonDto.getIncomeDetailCommonSaveDtoList();
            List<IncomeDetailCommonSave> incomeDetailCommonSaveList = new ArrayList<>();
            List<IncomeDetailReversedSave> incomeDetailReversedSaveList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(incomeDetailCommonSaveDtoList)) {
                for (IncomeDetailCommonSaveDto incomeDetailCommonSaveDto : incomeDetailCommonSaveDtoList) {
                    IncomeDetailCommonSave incomeDetailCommonSave = (IncomeDetailCommonSave) incomeDetailCommonSaveDto;
                    incomeDetailCommonSave.setId(KeyUtil.getId());
                    incomeDetailCommonSave.setIncomeId(incomeMainCommonDto.getId());
                    incomeDetailCommonSave.setSn(incomeDetailCommonSave.getSn().toUpperCase());
                    incomeDetailCommonSave.setBarcode(incomeDetailCommonSave.getBarcode().toUpperCase());
                    incomeDetailCommonSaveList.add(incomeDetailCommonSave);

                    // 如果有销账明细，则保存相关信息
                    if (CollectionUtil.isNotEmpty(incomeDetailCommonSaveDto.getBookIdList())) {
                        incomeDetailCommonSaveDto.getBookIdList().forEach(bookId -> {
                            IncomeDetailReversedSave incomeDetailReversedSave = new IncomeDetailReversedSave();
                            incomeDetailReversedSave.setId(KeyUtil.getId());
                            incomeDetailReversedSave.setBookId(bookId);
                            incomeDetailReversedSave.setDetailId(incomeDetailCommonSave.getId());
                            incomeDetailReversedSaveList.add(incomeDetailReversedSave);
                        });
                    }
                }
            }
            // 添加申请明细信息
            this.incomeDetailCommonSaveService.saveBatch(incomeDetailCommonSaveList);
            // 添加销账信息
            if (CollectionUtil.isNotEmpty(incomeDetailReversedSaveList)) {
                this.incomeDetailReversedSaveService.saveBatch(incomeDetailReversedSaveList);
            }
        }
    }

    /**
     * 入库申请提交
     *
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author Qiugm
     * @date 2019-11-13
     */
    @Override
    public void add(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        // 添加申请单信息，按明细拆单
        this.addIncome(incomeWareCommonDto, userInfo, reqParam);
    }

    /**
     * 添加申请单信息
     * 按明细拆单
     *
     * @param incomeMainCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author Qiugm
     * @date 2019-11-21
     */
    protected void addIncome(IncomeWareCommonDto incomeMainCommonDto, UserInfo userInfo, ReqParam reqParam) {
        List<IncomeWareCommon> incomeWareCommonList = new ArrayList<>();
        List<IncomeWarePurchase> incomeWarePurchaseList = new ArrayList<>();

        if (incomeMainCommonDto != null &&
                CollectionUtil.isNotEmpty(incomeMainCommonDto.getIncomeDetailCommonSaveDtoList())) {
            List<IncomeDetailCommonSaveDto> incomeDetailCommonSaveList =
                    incomeMainCommonDto.getIncomeDetailCommonSaveDtoList();
            // 生成流程实例
            List<Long> flowIdList =
                    this.flowInstanceCompoService.createInstanceList(incomeDetailCommonSaveList.size(),
                            reqParam.getCorpId(), incomeMainCommonDto.getLargeClassId(),
                            incomeMainCommonDto.getSmallClassId(), userInfo);

            for (int i = 0; i < incomeDetailCommonSaveList.size(); i++) {
                long flowId = flowIdList.get(i);
                IncomeDetailCommonSave incomeDetailCommonSave = incomeDetailCommonSaveList.get(i);
                IncomeWareCommon incomeWareCommon = makeIncomeWareCommon(incomeMainCommonDto, incomeDetailCommonSave,
                        userInfo, reqParam);
                if (incomeWareCommon != null) {
                    incomeWareCommon.setId(KeyUtil.getId());
                    incomeWareCommon.setLargeClassId(LargeClassEnum.INCOME.getCode());
                    incomeWareCommon.setIncomeDate(DateTime.now().toDateStr());
                    incomeWareCommon.setIncomeCode(this.businessCodeGenerator.getIncomeWareCode(reqParam.getCorpId(), ""));
                    incomeWareCommon.setFlowInstanceId(flowId);
                    incomeWareCommon.setGroupCode(this.businessCodeGenerator.getIncomeGroupCode(reqParam.getCorpId(), ""));
                    incomeWareCommonList.add(incomeWareCommon);
                }

                // 若有采购合同号，保存采购相关信息
                if (!StringUtils.isEmpty(incomeMainCommonDto.getContId())) {
                    IncomeWarePurchase incomeWarePurchase = new IncomeWarePurchase();
                    BeanUtils.copyProperties(incomeMainCommonDto, incomeWarePurchase);
                    incomeWarePurchase.setId(incomeWareCommon.getId());
                    incomeWarePurchaseList.add(incomeWarePurchase);
                }
            }
        }

        if (CollectionUtil.isNotEmpty(incomeWareCommonList)) {
            // 批量添加申请单信息
            this.incomeWareCommonService.saveBatch(incomeWareCommonList);

            for (IncomeWareCommon incomeWareCommon : incomeWareCommonList) {
                IncomeWareCommonDto incomeWareCommonDto = JsonUtil.parseObject(JsonUtil.toJson(incomeWareCommon),
                        IncomeWareCommonDto.class);
                incomeWareCommonDto.setNodeEndTypeId(NodeEndTypeEnum.PASS.getCode());
                incomeWareCommonDto.setAuditNote(incomeWareCommon.getDescription());
                // 结束当前结点
                this.endCurrentNode(incomeWareCommonDto, userInfo);

                // 流程结束，调整库存
                if (this.flowInstanceCompoService.isEndFlow(incomeWareCommon.getFlowInstanceId())) {
                    // 调整库存
                    this.stockAndLogTxExternalService.adjustStock(incomeWareCommon);
                    // 已入库
                    incomeWareCommon.setIncomeStatus(IncomeStatusEnum.HAD_INCOME.getCode());
                    this.incomeWareCommonService.updateIncomeStatus(incomeWareCommon);
                }
            }
        }

        if (CollectionUtil.isNotEmpty(incomeWarePurchaseList)) {
            this.incomeWarePurchaseService.saveBatch(incomeWarePurchaseList);
        }

        // 删除保存记录
        if (LongUtil.isNotZero(incomeMainCommonDto.getId())) {
            this.incomeDetailCommonSaveService.deleteByIncomeId(incomeMainCommonDto.getId());
            this.incomeMainPurchaseSaveService.removeById(incomeMainCommonDto.getId());
            this.incomeMainCommonSaveService.removeById(incomeMainCommonDto.getId());
        }
    }

    /**
     * 创造IncomeWareCommon对象
     *
     * @param incomeMainCommonDto
     * @param incomeDetailCommonSave
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.wms.business.income.model.IncomeWareCommon
     * @author Qiugm
     * @date 2019-11-22
     */
    protected IncomeWareCommon makeIncomeWareCommon(IncomeWareCommonDto incomeMainCommonDto,
                                                    IncomeDetailCommonSave incomeDetailCommonSave, UserInfo userInfo,
                                                    ReqParam reqParam) {
        IncomeWareCommon incomeWareCommon = null;
        if (incomeMainCommonDto != null) {
            incomeWareCommon = new IncomeWareCommon();
            BeanUtils.copyProperties(incomeMainCommonDto, incomeWareCommon);
            incomeWareCommon.setCorpId(reqParam.getCorpId());
            incomeWareCommon.setCreateBy(userInfo.getUserId());
            incomeWareCommon.setCreateTime(DateUtil.date().toTimestamp());
            incomeWareCommon.setIncomeStatus(IncomeStatusEnum.NO_INCOME.getCode());
            BeanUtils.copyProperties(incomeDetailCommonSave, incomeWareCommon);
            if (StrUtil.isNotBlank(incomeDetailCommonSave.getSn())) {
                incomeWareCommon.setSn(incomeDetailCommonSave.getSn().toUpperCase());
            }
            if (StrUtil.isNotBlank(incomeDetailCommonSave.getBarcode())) {
                incomeWareCommon.setBarcode(incomeDetailCommonSave.getBarcode().toUpperCase());
            }
        }
        return incomeWareCommon;
    }

    /**
     * 审批入库申请
     *
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author Qiugm
     * @date 2019-11-14
     */
    @Override
    public void audit(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        // 结束当前结点
        this.endCurrentNode(incomeWareCommonDto, userInfo);
        // 流程结束，调整库存
        if (this.flowInstanceCompoService.isEndFlow(incomeWareCommonDto.getFlowInstanceId())) {
            this.stockAndLogTxExternalService.adjustStock(incomeWareCommonDto);

            IncomeWareCommon incomeWareCommon = new IncomeWareCommon();
            incomeWareCommon.setId(incomeWareCommonDto.getId());
            // 已入库
            incomeWareCommon.setIncomeStatus(IncomeStatusEnum.HAD_INCOME.getCode());
            this.incomeWareCommonService.updateIncomeStatus(incomeWareCommon);
        }
    }

    /**
     * 修改入库申请
     *
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author Qiugm
     * @date 2019-11-14
     */
    @Override
    public void update(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        // 检查表单数据
        this.checkIncomeData(incomeWareCommonDto);
        IncomeMainCommonSave incomeMainCommonSave =
                this.incomeMainCommonSaveService.getById(incomeWareCommonDto.getId());
        if (incomeMainCommonSave != null) {
            updateIncomeSave(incomeWareCommonDto, userInfo, reqParam);
        } else {
            updateIncome(incomeWareCommonDto, userInfo, reqParam);
        }
    }

    /**
     * 结束当前流程结点
     *
     * @param incomeWareCommonDto
     * @param userInfo
     * @return void
     * @author Qiugm
     * @date 2019-11-22
     */
    @Override
    public void endCurrentNode(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo) {
        FlowInstanceNodeHandler flowInstanceNodeHandler = null;
        FlowInstance flowInstance = this.flowInstanceService.getById(incomeWareCommonDto.getFlowInstanceId());
        if (flowInstance != null) {
            flowInstanceNodeHandler = flowInstanceCompoService.getBy(flowInstance.getCurrentNodeId(),
                    userInfo.getUserId());
        }
        if (flowInstanceNodeHandler != null) {
            FlowInstanceNodeDealResultDto flowInstanceNodeDealResultDto = new FlowInstanceNodeDealResultDto();
            flowInstanceNodeDealResultDto.setNodeHandlerId(flowInstanceNodeHandler.getId());
            flowInstanceNodeDealResultDto.setIsSubmit(SubmitEnum.YES.getCode());
            flowInstanceNodeDealResultDto.setNodeEndTypeId(incomeWareCommonDto.getNodeEndTypeId());
            flowInstanceNodeDealResultDto.setNodeEndTypeName(NodeEndTypeEnum.getNameByCode(flowInstanceNodeDealResultDto.getNodeEndTypeId()));
            flowInstanceNodeDealResultDto.setDoDescription(incomeWareCommonDto.getAuditNote());
            this.flowInstanceCompoService.endCurrentNode(flowInstanceNodeDealResultDto, userInfo);
        }
    }

    /**
     * 更新入库信息
     *
     * @param incomeMainCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author Qiugm
     * @date 2019-11-20
     */
    protected void updateIncome(IncomeWareCommonDto incomeMainCommonDto, UserInfo userInfo, ReqParam reqParam) {
        IncomeWareCommon incomeMainCommon = new IncomeWareCommon();
        BeanUtils.copyProperties(incomeMainCommonDto, incomeMainCommon);
        incomeMainCommon.setUpdateBy(userInfo.getUserId());
        incomeMainCommon.setUpdateTime(DateUtil.date().toTimestamp());
        // 更新申请单信息
        this.incomeWareCommonService.updateById(incomeMainCommon);

        // 若有采购合同号，保存采购相关信息
        if (!StringUtils.isEmpty(incomeMainCommonDto.getContId())) {
            IncomeWarePurchase incomeWarePurchase = new IncomeWarePurchase();
            BeanUtils.copyProperties(incomeMainCommonDto, incomeWarePurchase);
            incomeWarePurchaseService.updateById(incomeWarePurchase);
        }

        // 若有销账明细，则更新销账信息
        List<BookSaleBorrow> bookSaleBorrowOldList = new ArrayList<>();
        List<BookSaleBorrow> bookSaleBorrowNewList = new ArrayList<>();
        List<Long> bookIdList = this.incomeWareReversedService.listBookIdByDetailId(incomeMainCommonDto.getId());
        if (CollectionUtil.isNotEmpty(bookIdList)) {
            bookIdList.forEach(bookId -> {
                BookSaleBorrow bookSaleBorrow = new BookSaleBorrow();
                bookSaleBorrow.setId(bookId);
                bookSaleBorrow.setReversed("N");
                bookSaleBorrow.setReverseDetailId(0L);
                bookSaleBorrowOldList.add(bookSaleBorrow);
            });
            if (CollectionUtil.isNotEmpty(bookSaleBorrowOldList)) {
                this.bookSaleBorrowService.updateBatchById(bookSaleBorrowOldList);
            }
        }
        if (CollectionUtil.isNotEmpty(incomeMainCommonDto.getBookSaleBorrowResultDtoList())) {
            incomeMainCommonDto.getBookSaleBorrowResultDtoList().forEach(bookSaleBorrowResultDto -> {
                BookSaleBorrow bookSaleBorrow = new BookSaleBorrow();
                BeanUtils.copyProperties(bookSaleBorrowResultDto, bookSaleBorrow);
                bookSaleBorrow.setReversed("Y");
                bookSaleBorrow.setReverseDetailId(incomeMainCommonDto.getId());
                bookSaleBorrow.setReverseBy(userInfo.getUserId());
                bookSaleBorrow.setReverseTime(DateTime.now());
                bookSaleBorrowNewList.add(bookSaleBorrow);
            });
            if (CollectionUtil.isNotEmpty(bookSaleBorrowNewList)) {
                this.bookSaleBorrowService.updateBatchById(bookSaleBorrowNewList);
            }
        }

        // 修改申请同样需要走流程
        this.audit(incomeMainCommonDto, userInfo, reqParam);
    }

    /**
     * 更新保存的入库信息
     *
     * @param incomeMainCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author Qiugm
     * @date 2019-11-20
     */
    protected void updateIncomeSave(IncomeWareCommonDto incomeMainCommonDto, UserInfo userInfo, ReqParam reqParam) {
        IncomeMainCommonSave incomeMainCommon = new IncomeMainCommonSave();
        BeanUtils.copyProperties(incomeMainCommonDto, incomeMainCommon);
        incomeMainCommon.setCorpId(reqParam.getCorpId());
        incomeMainCommon.setUpdateBy(userInfo.getUserId());
        incomeMainCommon.setUpdateTime(DateUtil.date().toTimestamp());
        incomeMainCommon.setSaveStatus(SaveStatusEnum.SAVE.getCode());
        // 添加申请单信息
        this.incomeMainCommonSaveService.updateById(incomeMainCommon);

        // 若有采购合同号，保存采购相关信息
        if (!StringUtils.isEmpty(incomeMainCommonDto.getContId())) {
            IncomeMainPurchaseSave incomeMainPurchaseSave = new IncomeMainPurchaseSave();
            BeanUtils.copyProperties(incomeMainCommonDto, incomeMainPurchaseSave);
            incomeMainPurchaseSave.setId(incomeMainCommon.getId());
            incomeMainPurchaseSaveService.updateById(incomeMainPurchaseSave);
        }

        // 销账明细
        List<IncomeDetailReversedSave> incomeDetailReversedSaveList = new ArrayList<>();
        incomeMainCommonDto.getIncomeDetailCommonSaveDtoList().forEach(incomeDetailCommonSaveDto -> {
            incomeDetailCommonSaveDto.setId(KeyUtil.getId());
            if (CollectionUtil.isNotEmpty(incomeDetailCommonSaveDto.getBookIdList())) {
                incomeDetailCommonSaveDto.getBookIdList().forEach(bookId -> {
                    IncomeDetailReversedSave incomeDetailReversedSave = new IncomeDetailReversedSave();
                    incomeDetailReversedSave.setId(KeyUtil.getId());
                    incomeDetailReversedSave.setDetailId(incomeDetailCommonSaveDto.getId());
                    incomeDetailReversedSave.setBookId(bookId);
                    incomeDetailReversedSaveList.add(incomeDetailReversedSave);
                });
            }
        });
        List<IncomeDetailCommonSave> incomeDetailCommonSaveList =
                JsonUtil.parseArray(JsonUtil.toJson(incomeMainCommonDto.getIncomeDetailCommonSaveDtoList()),
                        IncomeDetailCommonSave.class);

        // 删除销账明细
        this.incomeDetailReversedSaveService.deleteByIncomeId(incomeMainCommonDto.getId());
        // 批量添加销账明细
        this.incomeDetailReversedSaveService.saveBatch(incomeDetailReversedSaveList);
        // 删除申请单明细
        this.incomeDetailCommonSaveService.deleteByIncomeId(incomeMainCommonDto.getId());
        // 添加申请单明细信息
        this.incomeDetailCommonSaveService.saveBatch(incomeDetailCommonSaveList);
    }

    /**
     * 检查表单数据
     *
     * @param incomeMainCommonDto
     * @return void
     * @author Qiugm
     * @date 2019-11-13
     */
    protected void checkIncomeData(IncomeWareCommonDto incomeMainCommonDto) {
        if (incomeMainCommonDto == null) {
            throw new AppException("表单信息不能为空。");
        }

        if (LongUtil.isZero(incomeMainCommonDto.getDepotId())) {
            throw new AppException("入库库房不能为空。");
        }

        if (LongUtil.isZero(incomeMainCommonDto.getPropertyRight())) {
            throw new AppException("物料产权不能为空。");
        }

        if (LongUtil.isZero(incomeMainCommonDto.getFlowInstanceId())) {
            if (CollectionUtil.isEmpty(incomeMainCommonDto.getIncomeDetailCommonSaveDtoList())) {
                throw new AppException("物料明细不能为空。");
            }
        }

        StringBuilder detailTips = new StringBuilder(64);
        if (CollectionUtil.isNotEmpty(incomeMainCommonDto.getIncomeDetailCommonSaveDtoList())) {
            for (int i = 0; i < incomeMainCommonDto.getIncomeDetailCommonSaveDtoList().size(); i++) {
                StringBuilder rowTip = new StringBuilder(32);
                IncomeDetailCommonSave incomeDetailCommonSave =
                        incomeMainCommonDto.getIncomeDetailCommonSaveDtoList().get(i);
                if (LongUtil.isZero(incomeDetailCommonSave.getModelId())) {
                    rowTip.append("物料型号为空，");
                }
                if (IntUtil.isZero(incomeDetailCommonSave.getQuantity())) {
                    rowTip.append("物料数量为空，");
                }
                if (IntUtil.isZero(incomeDetailCommonSave.getStatus())) {
                    rowTip.append("物料状态为空，");
                }
                if (rowTip.length() > 0) {
                    detailTips.append("第").append(i++).append("行明细：").append(rowTip.substring(0,
                            rowTip.length() - 1)).append("；");
                }
            }
        } else {
            if (LongUtil.isZero(incomeMainCommonDto.getModelId())) {
                throw new AppException("物料型号不能为空。");
            }
            if (IntUtil.isZero(incomeMainCommonDto.getQuantity())) {
                throw new AppException("物料数量不能为空。");
            }
            if (IntUtil.isZero(incomeMainCommonDto.getStatus())) {
                throw new AppException("物料状态不能为空。");
            }
        }

    }

}
