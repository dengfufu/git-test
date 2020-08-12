package com.zjft.usp.wms.business.trans.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.enums.SmallClassEnum;
import com.zjft.usp.wms.business.trans.dto.TransDetailCommonSaveDto;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.enums.TransStatusEnum;
import com.zjft.usp.wms.business.trans.external.TransConvertToStockService;
import com.zjft.usp.wms.business.trans.model.TransDetailCommonSave;
import com.zjft.usp.wms.business.trans.model.TransMainCommonSave;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import com.zjft.usp.wms.business.trans.service.TransDetailCommonSaveService;
import com.zjft.usp.wms.business.trans.service.TransMainCommonSaveService;
import com.zjft.usp.wms.business.trans.service.TransWareCommonService;
import com.zjft.usp.wms.business.trans.strategy.TransStrategy;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.enums.CompletedEnum;
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
import java.util.Map;

/**
 * 调拨策略抽象类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-21 11:21
 **/
public abstract class AbstractTransStrategy implements TransStrategy {
    @Autowired
    private TransDetailCommonSaveService transDetailCommonSaveService;
    @Autowired
    private TransMainCommonSaveService transMainCommonSaveService;
    @Autowired
    protected TransWareCommonService transWareCommonService;
    @Autowired
    protected TransConvertToStockService stockAndLogTxExternalService;
    @Autowired
    protected FlowInstanceCompoService flowInstanceCompoService;
    @Autowired
    private FlowInstanceService flowInstanceService;
    @Resource
    private BusinessCodeGenerator businessCodeGenerator;


    /**
     * 物料库存调度
     */
    public static final String TRANS_WARE_TRANSFER = "110";

    /**
     * 物料快速转库
     */
    public static final String TRANS_WARE_SHIFT = "120";

    /**
     * 待修物料返还
     */
    public static final String TRANS_WARE_BAD_RETURN = "130";

    /**
     * 保存申请单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void save(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        // 检查表单数据
        this.checkTransData(transWareCommonDto);
        // 保存调拨单信息
        this.saveTrans(transWareCommonDto, userInfo, reqParam);
        // 保存调拨明细信息
        this.saveTransDetail(transWareCommonDto);
    }

    /**
     * 保存申请单信息
     *
     * @author Qiugm
     * @date 2019-11-22
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    protected void saveTrans(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        TransMainCommonSave transMainCommonSave = new TransMainCommonSave();
        BeanUtils.copyProperties(transWareCommonDto, transMainCommonSave);
        /**设置暂存主单，首次保存时新生成主单ID，第2次及后面的保存，保留首次保存时生成的KEY*/
        if (LongUtil.isZero(transMainCommonSave.getId())) {
            transMainCommonSave.setId(KeyUtil.getId());
        }
        transWareCommonDto.setId(transMainCommonSave.getId());
        transMainCommonSave.setCreateBy(userInfo.getUserId());
        transMainCommonSave.setCreateTime(DateUtil.date().toTimestamp());
        // 保存申请信息
        this.transMainCommonSaveService.save(transMainCommonSave);
    }

    /**
     * 保存申请单明细信息
     *
     * @author Qiugm
     * @date 2019-11-22
     * @param transWareCommonDto
     * @return void
     */
    protected void saveTransDetail(TransWareCommonDto transWareCommonDto) {
        if (transWareCommonDto != null) {
            List<TransDetailCommonSave> transDetailCommonSaveList =
                    JsonUtil.parseArray(JsonUtil.toJson(transWareCommonDto.getTransDetailCommonSaveDtoList()),
                            TransDetailCommonSave.class);
            if (CollectionUtil.isNotEmpty(transDetailCommonSaveList)) {
                for (TransDetailCommonSave transDetailCommonSave : transDetailCommonSaveList) {
                    transDetailCommonSave.setId(KeyUtil.getId());
                    transDetailCommonSave.setTransId(transWareCommonDto.getId());
                }
            }
            // 批量插入申请明细信息
            this.transDetailCommonSaveService.saveBatch(transDetailCommonSaveList);
        }
    }

    /**
     * 添加调拨单申请信息并自动拆单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void add(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        // 检查表单信息
        //this.checkTransData(transWareCommonDto);
        // 添加调拨信息，按明细拆单
        this.addTransInfo(transWareCommonDto, userInfo, reqParam);
    }

    /**
     * 提交调拨申请
     * 按调拨明细拆单
     *
     * @author Qiugm
     * @date 2019-11-22
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    protected void addTransInfo(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        /**修改暂存单状态*/
        //this.transMainCommonSaveService.updateSaveStatusToSubmit(transWareCommonDto.getId());
        List<TransDetailCommonSaveDto> transDetailCommonSaveDtoList =
                transWareCommonDto.getTransDetailCommonSaveDtoList();
        /**生成流程实例列表*/
        List<Long> createInstanceList = flowInstanceCompoService.createInstanceList(transDetailCommonSaveDtoList.size(),
                reqParam.getCorpId(), transWareCommonDto.getLargeClassId(),
                transWareCommonDto.getSmallClassId(), userInfo);

        /**调拨单提交后自动拆单*/
        List<TransWareCommon> transWareCommonList = new ArrayList<>();
        for (int count = 0; count < transDetailCommonSaveDtoList.size(); count++) {
            TransDetailCommonSaveDto transDetailCommonSaveDto = transDetailCommonSaveDtoList.get(count);
            if (transDetailCommonSaveDto == null) {
                throw new AppException("获得明细出现例外异常，请重试！");
            }
            Long flowInstanceId = createInstanceList.get(count);

            if (LongUtil.isZero(flowInstanceId)) {
                throw new AppException("生成流程实例ID出现例外异常，请重试！");
            }

            TransWareCommon transWareCommon = new TransWareCommon();

            /**复制*/
            BeanUtils.copyProperties(transWareCommonDto, transWareCommon);
            BeanUtils.copyProperties(transDetailCommonSaveDto, transWareCommon);
            /**纠正部分属性*/
            transWareCommon.setId(KeyUtil.getId());
            transWareCommon.setCorpId(reqParam.getCorpId());
            transWareCommon.setTransCode(this.businessCodeGenerator.getTransWareCode(reqParam.getCorpId(), ""));
            transWareCommon.setGroupCode(this.businessCodeGenerator.getTransGroupCode(reqParam.getCorpId(), ""));
            transWareCommon.setFlowInstanceId(flowInstanceId);
            transWareCommon.setTransStatus(TransStatusEnum.FOR_ALLOCATION.getCode());
            transWareCommonList.add(transWareCommon);
        }

        this.transWareCommonService.saveBatch(transWareCommonList);
        for (TransWareCommon transWareCommon : transWareCommonList) {
            // 完成发起人节点
            transWareCommonDto.setFlowInstanceId(transWareCommon.getFlowInstanceId());
            transWareCommonDto.setNodeTypeId(NodeEndTypeEnum.PASS.getCode());
            transWareCommonDto.setDoDescried(transWareCommonDto.getDescription());
            this.endCurrentNode(transWareCommonDto, userInfo);

            // 流程结束，调整库存
            if (this.flowInstanceCompoService.isEndFlow(transWareCommon.getFlowInstanceId())) {
                // 调整库存
                this.stockAndLogTxExternalService.adjustStock(transWareCommon);
                // 已入库
                transWareCommon.setTransStatus(TransStatusEnum.FOR_ALLOCATION.getCode());
                this.transWareCommonService.updateTransStatus(transWareCommon);
            }
        }
    }

    /**
     * 审批申请单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void audit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        TransWareCommon transWareCommon = this.transWareCommonService.getById(transWareCommonDto.getId());
        // 结束当前结点
        this.endCurrentNode(transWareCommonDto, userInfo);
        // 流程结束，调整库存
        if (this.flowInstanceCompoService.isEndFlow(transWareCommon.getFlowInstanceId())) {
            this.stockAndLogTxExternalService.adjustStock(transWareCommonDto);
            transWareCommon.setTransStatus(TransStatusEnum.COMPLETE_ALLOCATION.getCode());

        } else {
            transWareCommon.setTransStatus(TransStatusEnum.IN_ALLOCATION.getCode());
        }

        this.transWareCommonService.updateTransStatus(transWareCommon);

    }

    /**
     * 结束当前流程结点
     *
     * @author Qiugm
     * @date 2019-11-22
     * @param transWareCommonDto
     * @param userInfo
     * @return void
     */
    protected void endCurrentNode(TransWareCommonDto transWareCommonDto, UserInfo userInfo) {
        FlowInstanceNodeHandler flowInstanceNodeHandler = null;
        FlowInstance flowInstance = this.flowInstanceService.getById(transWareCommonDto.getFlowInstanceId());
        if (flowInstance != null) {
            flowInstanceNodeHandler = flowInstanceCompoService.getBy(flowInstance.getCurrentNodeId(),
                    userInfo.getUserId());
        }
        if (flowInstanceNodeHandler != null) {
            FlowInstanceNodeDealResultDto flowInstanceNodeDealResultDto = new FlowInstanceNodeDealResultDto();
            flowInstanceNodeDealResultDto.setNodeHandlerId(flowInstanceNodeHandler.getId());
            flowInstanceNodeDealResultDto.setIsSubmit(SubmitEnum.YES.getCode());
            flowInstanceNodeDealResultDto.setNodeEndTypeId(transWareCommonDto.getNodeTypeId());
            flowInstanceNodeDealResultDto.setNodeEndTypeName(NodeEndTypeEnum.getNameByCode(flowInstanceNodeDealResultDto.getNodeEndTypeId()));
            flowInstanceNodeDealResultDto.setDoDescription(transWareCommonDto.getDoDescried());
            this.flowInstanceCompoService.endCurrentNode(flowInstanceNodeDealResultDto, userInfo);
        }
    }

    /**
     * 检查表单信息
     *
     * @author Qiugm
     * @date 2019-11-22
     * @param transWareCommonDto
     * @return void
     */
    protected void checkTransData(TransWareCommonDto transWareCommonDto) {
        if (transWareCommonDto == null) {
            throw new AppException("表单信息不能为空。");
        }

        if (LongUtil.isZero(transWareCommonDto.getFromDepotId())) {
            throw new AppException("出库库房不能为空。");
        }

        if (StringUtils.isEmpty(transWareCommonDto.getApplyDate())) {
            throw new AppException("申请日期不能为空。");
        }

        if (LongUtil.isZero(transWareCommonDto.getPriorityLevel())) {
            throw new AppException("优先级别不能为空。");
        }

        if (LongUtil.isZero(transWareCommonDto.getFlowInstanceId())) {
            if (CollectionUtil.isEmpty(transWareCommonDto.getTransDetailCommonSaveDtoList())) {
                throw new AppException("物料明细不能为空。");
            }
        }
    }

    @Override
    public void batchAudit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam,Integer status) {
        // 批量审批不区分业务小类
        if (CollectionUtil.isEmpty(transWareCommonDto.getIdList())) {
            throw new AppException("请选择需要审批的入库单");
        }
        Map<Long, TransWareCommon> idAndWareMap = this.transWareCommonService.mapIdAndObject(transWareCommonDto.getIdList());
        List<TransWareCommon> updateStatusList = new ArrayList<>();

        //TODO
        for(Long id : transWareCommonDto.getIdList()){
            TransWareCommon transWareCommon = idAndWareMap.get(id);
            TransWareCommonDto transWareCommonDtoSingle = new TransWareCommonDto();

            transWareCommon.setTransStatus(status);
            BeanUtils.copyProperties(transWareCommon,transWareCommonDtoSingle);
            transWareCommonDtoSingle.setNodeTypeId(NodeEndTypeEnum.PASS.getCode());
            transWareCommonDto.setDoDescried(transWareCommonDto.getDescription());
            // 结束当前结点
            this.endCurrentNode(transWareCommonDtoSingle, userInfo);
            // 流程结束，调整库存
            if (this.flowInstanceCompoService.isEndFlow(transWareCommon.getFlowInstanceId())) {
                // 判读是否进行调库
                if(transWareCommonDto.getIsAdjust()){
                    this.stockAndLogTxExternalService.adjustStock(transWareCommonDto);
                }

                if(transWareCommonDto.getSmallClassId() == SmallClassEnum.TRANS_WARE_SHIFT.getCode()){
                    transWareCommon.setTransStatus(TransStatusEnum.COMPLETE_ALLOCATION.getCode());
                }
                updateStatusList.add(transWareCommon);
            }
        }
        if(CollectionUtil.isNotEmpty(updateStatusList)) {
            this.transWareCommonService.updateBatchById(updateStatusList);
        }
    }
}
