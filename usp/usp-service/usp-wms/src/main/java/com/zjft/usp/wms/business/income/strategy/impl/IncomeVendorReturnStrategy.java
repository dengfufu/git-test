package com.zjft.usp.wms.business.income.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.enums.LargeClassEnum;
import com.zjft.usp.wms.business.book.model.BookVendorReturn;
import com.zjft.usp.wms.business.book.service.BookVendorReturnService;
import com.zjft.usp.wms.business.income.dto.IncomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.income.dto.IncomeWareCommonDto;
import com.zjft.usp.wms.business.income.enums.IncomeStatusEnum;
import com.zjft.usp.wms.business.income.external.StockExternalService;
import com.zjft.usp.wms.business.income.model.IncomeDetailCommonSave;
import com.zjft.usp.wms.business.income.model.IncomeWareCommon;
import com.zjft.usp.wms.business.income.model.IncomeWareReversed;
import com.zjft.usp.wms.business.income.service.IncomeDetailCommonSaveService;
import com.zjft.usp.wms.business.income.service.IncomeMainCommonSaveService;
import com.zjft.usp.wms.business.income.service.IncomeWareCommonService;
import com.zjft.usp.wms.business.income.service.IncomeWareReversedService;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.enums.CompletedEnum;
import com.zjft.usp.wms.flow.enums.NodeEndTypeEnum;
import com.zjft.usp.wms.generator.BusinessCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 厂商返还入库策略实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-14 10:09
 **/
@Component(AbstractIncomeStrategy.INCOME_VENDOR_RETURN)
public class IncomeVendorReturnStrategy extends AbstractIncomeStrategy {
    @Autowired
    private IncomeMainCommonSaveService incomeMainCommonSaveService;
    @Autowired
    private IncomeDetailCommonSaveService incomeDetailCommonSaveService;
    @Autowired
    private IncomeWareCommonService incomeWareCommonService;
    @Resource
    private StockExternalService stockAndLogTxExternalService;
    @Autowired
    private FlowInstanceCompoService flowInstanceCompoService;
    @Autowired
    private IncomeWareReversedService incomeWareReversedService;
    @Autowired
    private BookVendorReturnService bookVendorReturnService;
    @Resource
    private BusinessCodeGenerator businessCodeGenerator;

    /***
     * 保存入库申请单
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void save(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.save(incomeWareCommonDto, userInfo, reqParam);
    }

    /***
     * 入库申请提交
     *
     * @author Qiugm
     * @date 2019-11-13
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void add(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        // 检查表单数据
        super.checkIncomeData(incomeWareCommonDto);
        // 添加申请单信息，按明细拆单
        this.addIncome(incomeWareCommonDto, userInfo, reqParam);
    }

    /**
     * 添加申请单信息
     * 按明细拆单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param incomeMainCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    protected void addIncome(IncomeWareCommonDto incomeMainCommonDto, UserInfo userInfo, ReqParam reqParam) {
        List<IncomeWareCommon> incomeWareCommonList = new ArrayList<>();
        List<IncomeWareReversed> incomeWareReversedList = new ArrayList<>();
        List<BookVendorReturn> bookVendorReturnList = new ArrayList<>();

        if (incomeMainCommonDto != null &&
                CollectionUtil.isNotEmpty(incomeMainCommonDto.getIncomeDetailCommonSaveDtoList())) {
            List<IncomeDetailCommonSaveDto> incomeDetailCommonSaveList =
                    incomeMainCommonDto.getIncomeDetailCommonSaveDtoList();
            // 生成流程实例
            List<Long> flowIdList =
                    this.flowInstanceCompoService.createInstanceList(incomeDetailCommonSaveList.size(),
                            reqParam.getCorpId(), incomeMainCommonDto.getLargeClassId(),
                            incomeMainCommonDto.getSmallClassId(), userInfo);
            // 分组号
            Long groupId = KeyUtil.getId();
            for (int i = 0; i < incomeDetailCommonSaveList.size(); i++) {
                long flowId = flowIdList.get(i);
                IncomeDetailCommonSaveDto incomeDetailCommonSaveDto = incomeDetailCommonSaveList.get(i);
                IncomeDetailCommonSave incomeDetailCommonSave = (IncomeDetailCommonSave) incomeDetailCommonSaveDto;
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

                if (CollectionUtil.isNotEmpty(incomeDetailCommonSaveDto.getBookIdList())) {
                    incomeDetailCommonSaveDto.getBookIdList().forEach(bookId -> {
                        IncomeWareReversed incomeWareReversed = new IncomeWareReversed();
                        incomeWareReversed.setId(KeyUtil.getId());
                        incomeWareReversed.setDetailId(incomeWareCommon.getId());
                        incomeWareReversed.setBookId(bookId);
                        incomeWareReversedList.add(incomeWareReversed);

                        BookVendorReturn bookVendorReturn = new BookVendorReturn();
                        bookVendorReturn.setId(bookId);
                        bookVendorReturn.setReverseDetailId(incomeWareCommon.getId());
                        bookVendorReturn.setReversed(CompletedEnum.YES.getCode());
                        bookVendorReturn.setReverseBy(incomeWareCommon.getCreateBy());
                        bookVendorReturn.setReverseTime(DateUtil.date().toTimestamp());
                        bookVendorReturnList.add(bookVendorReturn);
                    });
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
                super.endCurrentNode(incomeWareCommonDto, userInfo);

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

        if (CollectionUtil.isNotEmpty(incomeWareReversedList)) {
            this.incomeWareReversedService.saveBatch(incomeWareReversedList);
        }

        if (CollectionUtil.isNotEmpty(bookVendorReturnList)) {
            for (BookVendorReturn bookVendorReturn : bookVendorReturnList) {
                // 设置成已销账
                this.bookVendorReturnService.updateReversed(bookVendorReturn);
            }
        }

        // 删除保存记录
        if (LongUtil.isNotZero(incomeMainCommonDto.getId())) {
            this.incomeMainCommonSaveService.removeById(incomeMainCommonDto.getId());
            this.incomeDetailCommonSaveService.deleteByIncomeId(incomeMainCommonDto.getId());
        }
    }

    /**
     * 审核入库申请
     *
     * @author Qiugm
     * @date 2019-11-14
     * @param incomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void audit(IncomeWareCommonDto incomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.audit(incomeWareCommonDto, userInfo, reqParam);
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
        super.update(incomeWareCommonDto, userInfo, reqParam);
    }
}