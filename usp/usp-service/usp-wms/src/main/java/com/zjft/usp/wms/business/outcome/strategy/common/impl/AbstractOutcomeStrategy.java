package com.zjft.usp.wms.business.outcome.strategy.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.model.LargeClass;
import com.zjft.usp.wms.baseinfo.service.LargeClassService;
import com.zjft.usp.wms.business.common.enums.SaveStatusEnum;
import com.zjft.usp.wms.business.outcome.composite.OutcomeCompoService;
import com.zjft.usp.wms.business.outcome.dto.OutcomeDetailCommonSaveDto;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.enums.OutcomeStatusEnum;
import com.zjft.usp.wms.business.outcome.external.OutcomeStockExternalService;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeMainCommonSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.business.outcome.service.OutcomeDetailCommonSaveService;
import com.zjft.usp.wms.business.outcome.service.OutcomeMainCommonSaveService;
import com.zjft.usp.wms.business.outcome.service.OutcomeWareCommonService;
import com.zjft.usp.wms.business.outcome.strategy.common.OutcomeStrategy;
import com.zjft.usp.wms.business.outcome.strategy.other.OutcomeConfirmBehavior;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.stock.service.StockCommonService;
import com.zjft.usp.wms.flow.composite.FlowInstanceCompoService;
import com.zjft.usp.wms.flow.dto.FlowInstanceNodeDealResultDto;
import com.zjft.usp.wms.flow.enums.NodeEndTypeEnum;
import com.zjft.usp.wms.flow.enums.SubmitEnum;
import com.zjft.usp.wms.flow.model.FlowInstance;
import com.zjft.usp.wms.flow.model.FlowInstanceNodeHandler;
import com.zjft.usp.wms.flow.service.FlowInstanceService;
import com.zjft.usp.wms.generator.BusinessCodeGenerator;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: JFZOU
 * @Date: 2019-11-21 9:35
 */
public abstract class AbstractOutcomeStrategy implements OutcomeStrategy {


    private OutcomeConfirmBehavior outcomeConfirmBehavior;
    @Autowired
    private FlowInstanceService flowInstanceService;
    @Autowired
    private FlowInstanceCompoService flowInstanceCompoService;
    @Autowired
    private OutcomeWareCommonService outcomeWareCommonService;
    @Autowired
    private OutcomeCompoService outcomeCompoService;
    @Autowired
    private OutcomeMainCommonSaveService outcomeMainCommonSaveService;
    @Autowired
    private OutcomeDetailCommonSaveService outcomeDetailCommonSaveService;
    @Autowired
    private StockCommonService stockCommonService;
    @Autowired
    private OutcomeStockExternalService outcomeStockExternalService;
    @Autowired
    private LargeClassService largeClassService;
    @Resource
    private BusinessCodeGenerator businessCodeGenerator;


    public void confirmReceive(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        outcomeConfirmBehavior.confirmReceive(outcomeWareCommonDto, userInfo, reqParam);
    }


    @Override
    public List<OutcomeDetailCommonSave> save(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        this.saveOutcomeMain(outcomeWareCommonDto,userInfo,reqParam);
        return this.saveOutcomeDetail(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public List<OutcomeWareCommon> add(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
//        this.checkoutcomeData(outcomeWareCommonDto);
        return this.addOutcomeWare(outcomeWareCommonDto,userInfo,reqParam);
    }

    @Override
    public void delete(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam){
        this.outcomeWareCommonService.removeByIds(outcomeWareCommonDto.getIdList());
    }

    @Override
    public void audit(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        if (outcomeWareCommonDto != null && LongUtil.isNotZero(outcomeWareCommonDto.getId())) {
            OutcomeWareCommon outcomeWareCommon = this.outcomeWareCommonService.getById(outcomeWareCommonDto.getId());
            BeanUtils.copyProperties(outcomeWareCommon, outcomeWareCommonDto);
            // 结束当前结点
            this.endCurrentNode(outcomeWareCommonDto, userInfo);
            // 流程结束，调整库存
            if (this.flowInstanceCompoService.isEndFlow(outcomeWareCommonDto.getFlowInstanceId())) {
                //调库
                this.outcomeStockExternalService.adjustStock(outcomeWareCommon, userInfo);
                // 已入库
                outcomeWareCommon.setOutcomeStatus(OutcomeStatusEnum.HAD_OUTCOME.getCode());
                outcomeWareCommon.setUpdateBy(userInfo.getUserId());
                this.outcomeWareCommonService.updateOutcomeStatus(outcomeWareCommon);
            }
        }
    }

    @Override
    public void send(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {

    }

    @Override
    public void update(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        OutcomeWareCommon outcomeWareCommon = this.outcomeWareCommonService.getById(outcomeWareCommonDto.getId());
        BeanUtils.copyProperties(outcomeWareCommonDto,outcomeWareCommon);
        outcomeWareCommon.setUpdateBy(userInfo.getUserId());
        outcomeWareCommon.setUpdateTime(new DateTime());
        this.outcomeWareCommonService.updateById(outcomeWareCommon);
    }

    @Override
    public void batchUpdate(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        if(StringUtil.isNullOrEmpty(outcomeWareCommonDto.getGroupCode())){
            List<OutcomeWareCommon> outcomeWareCommonList = this.outcomeWareCommonService.list(new QueryWrapper<OutcomeWareCommon>().eq("group_code",outcomeWareCommonDto.getGroupCode()));
            if(CollectionUtil.isNotEmpty(outcomeWareCommonList)) {
                List<Long> idList = new ArrayList<>();
                for(OutcomeWareCommon outcomeWareCommon : outcomeWareCommonList){
                    BeanUtils.copyProperties(outcomeWareCommonDto,outcomeWareCommon);
                    outcomeWareCommon.setUpdateTime(new DateTime());
                    outcomeWareCommon.setUpdateBy(userInfo.getUserId());
                    idList.add(outcomeWareCommon.getId());
                }
                this.outcomeWareCommonService.updateBatchById(outcomeWareCommonList);
                // 用于出库其他表相对应的信息修改
                outcomeWareCommonDto.setIdList(idList);
            }
        }
    }

    @Override
    public void endCurrentNode(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo) {
        FlowInstanceNodeHandler flowInstanceNodeHandler = null;
        FlowInstance flowInstance = this.flowInstanceService.getById(outcomeWareCommonDto.getFlowInstanceId());
        if (flowInstance != null) {
            flowInstanceNodeHandler = flowInstanceCompoService.getBy(flowInstance.getCurrentNodeId(),
                    userInfo.getUserId());
        }
        if (flowInstanceNodeHandler != null) {
            FlowInstanceNodeDealResultDto flowInstanceNodeDealResultDto = new FlowInstanceNodeDealResultDto();
            flowInstanceNodeDealResultDto.setNodeHandlerId(flowInstanceNodeHandler.getId());
            flowInstanceNodeDealResultDto.setIsSubmit(SubmitEnum.YES.getCode());
            flowInstanceNodeDealResultDto.setNodeEndTypeId(outcomeWareCommonDto.getNodeEndTypeId());
            flowInstanceNodeDealResultDto.setNodeEndTypeName(NodeEndTypeEnum.getNameByCode(flowInstanceNodeDealResultDto.getNodeEndTypeId()));
            flowInstanceNodeDealResultDto.setDoDescription(outcomeWareCommonDto.getAuditNote());
            this.flowInstanceCompoService.endCurrentNode(flowInstanceNodeDealResultDto, userInfo);
        }
    }
    /**
     * 检查表单数据
     *
     * @author zphu
     * @date 2019-11-21
     * @param outcomeWareCommonDto
     * @return void
     */
    protected void checkoutcomeData(OutcomeWareCommonDto outcomeWareCommonDto) {
        if (outcomeWareCommonDto == null) {
            throw new AppException("表单信息不能为空。");
        }

        if (LongUtil.isZero(outcomeWareCommonDto.getDepotId())) {
            throw new AppException("出库库房不能为空。");
        }

        StringBuilder detailTips = new StringBuilder(64);
        if (CollectionUtil.isNotEmpty(outcomeWareCommonDto.getOutcomeDetailCommonSaveDtoList())) {
            for (int i = 0; i < outcomeWareCommonDto.getOutcomeDetailCommonSaveDtoList().size(); i++) {
                StringBuilder rowTip = new StringBuilder(32);
                OutcomeDetailCommonSaveDto outcomeDetailCommonSaveDto =
                        outcomeWareCommonDto.getOutcomeDetailCommonSaveDtoList().get(i);
                if (IntUtil.isZero(outcomeWareCommonDto.getQuantity())) {
                    rowTip.append("物料数量为空，");
                }
                if (LongUtil.isZero(outcomeWareCommonDto.getStockId())) {
                    rowTip.append("物料库存ID为空，");
                }
                if (rowTip.length() > 0) {
                    detailTips.append("第").append(i++).append("行明细：").append(rowTip.substring(0,
                            rowTip.length() - 1)).append("；");
                }
            }
        }
    }

    /**
     * 添加出库信息
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/21 17:34
     * @throws
    **/
    protected List<OutcomeWareCommon> addOutcomeWare(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam){
        List<OutcomeWareCommon> outcomeWareCommonList = new ArrayList<>();

        if (outcomeWareCommonDto != null &&
                CollectionUtil.isNotEmpty(outcomeWareCommonDto.getOutcomeDetailCommonSaveDtoList())) {
            List<OutcomeDetailCommonSaveDto> outcomeDetailCommonSaveDtoList = outcomeWareCommonDto.getOutcomeDetailCommonSaveDtoList();
            // 生成流程实例
            List<Long> flowIdList =
                    this.flowInstanceCompoService.createInstanceList(outcomeDetailCommonSaveDtoList.size(),
                            reqParam.getCorpId(), outcomeWareCommonDto.getLargeClassId(),
                            outcomeWareCommonDto.getSmallClassId(), userInfo);
            //库存信息
            List<Long> stockIdList = outcomeDetailCommonSaveDtoList.stream().map(e -> e.getStockId()).collect(Collectors.toList());
            Map<Long, StockCommon> mapIdAndObject = this.stockCommonService.mapIdAndObject(stockIdList);
            // 单号前缀
            LargeClass largeClass  = this.largeClassService.getOne(new QueryWrapper<LargeClass>().eq("corp_id",reqParam.getCorpId()).eq("large_class_id",outcomeWareCommonDto.getLargeClassId()));
            // 分组号
            String groupCode = this.businessCodeGenerator.getOutcomeGroupCode(reqParam.getCorpId(),largeClass.getCodePrefix());
            for (int i = 0; i < outcomeDetailCommonSaveDtoList.size(); i++) {
                long flowId = flowIdList.get(i);
                OutcomeDetailCommonSave outcomeDetailCommonSave = outcomeDetailCommonSaveDtoList.get(i);
                OutcomeWareCommon outcomeWareCommon = this.makeOutcomeWareCommon(outcomeWareCommonDto,mapIdAndObject.get(outcomeDetailCommonSave.getStockId()),userInfo,reqParam);
                if(outcomeWareCommon != null){
                    outcomeWareCommon.setId(KeyUtil.getId());
                    outcomeWareCommon.setFlowInstanceId(flowId);
                    outcomeWareCommon.setGroupCode(groupCode);
                    outcomeWareCommon.setOutcomeCode(this.businessCodeGenerator.getOutcomeWareCode(reqParam.getCorpId(),largeClass.getCodePrefix()));
                    outcomeWareCommon.setQuantity(outcomeDetailCommonSave.getQuantity());
                    outcomeWareCommonList.add(outcomeWareCommon);
                }
            }
        }

        if (CollectionUtil.isNotEmpty(outcomeWareCommonList)) {
            // 批量添加申请单信息
            this.outcomeWareCommonService.saveBatch(outcomeWareCommonList);

            for (OutcomeWareCommon outcomeWareCommon : outcomeWareCommonList) {

                OutcomeWareCommonDto outcomeWareCommonDtoFor = new OutcomeWareCommonDto();
                BeanUtils.copyProperties(outcomeWareCommon,outcomeWareCommonDtoFor);
                outcomeWareCommonDtoFor.setNodeEndTypeId(NodeEndTypeEnum.PASS.getCode());
                outcomeWareCommonDtoFor.setAuditNote(outcomeWareCommon.getDescription());
                // 结束当前结点
                this.endCurrentNode(outcomeWareCommonDtoFor, userInfo);

                // 流程结束，调整库存
                if (this.flowInstanceCompoService.isEndFlow(outcomeWareCommon.getFlowInstanceId())) {
                    // 调整库存
                    this.outcomeStockExternalService.adjustStock(outcomeWareCommon,userInfo);
                    // 已入库
                    outcomeWareCommon.setOutcomeStatus(OutcomeStatusEnum.HAD_OUTCOME.getCode());
                    outcomeWareCommon.setUpdateBy(userInfo.getUserId());
                    this.outcomeWareCommonService.updateOutcomeStatus(outcomeWareCommon);
                }
            }
            // 删除保存记录
            this.outcomeCompoService.deleteSave(outcomeWareCommonDto.getMainId());
            return outcomeWareCommonList;
        }
        return null;
    }

    /**
     * 保存出库信息物料明细信息
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/22 15:27
     * @throws
    **/
    protected List<OutcomeDetailCommonSave> saveOutcomeDetail(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam){
        if (outcomeWareCommonDto != null &&
                CollectionUtil.isNotEmpty(outcomeWareCommonDto.getOutcomeDetailCommonSaveDtoList())) {
            List<OutcomeDetailCommonSave> outcomeDetailCommonSaveList =
                    JsonUtil.parseArray(JsonUtil.toJson(outcomeWareCommonDto.getOutcomeDetailCommonSaveDtoList()),
                            OutcomeDetailCommonSave.class);
            if (CollectionUtil.isNotEmpty(outcomeDetailCommonSaveList)) {
                for (OutcomeDetailCommonSave outcomeDetailCommonSave : outcomeDetailCommonSaveList) {
                    outcomeDetailCommonSave.setId(KeyUtil.getId());
                    outcomeDetailCommonSave.setOutcomeId(outcomeWareCommonDto.getId());
                    outcomeDetailCommonSave.setStockId(outcomeDetailCommonSave.getStockId());
                }
            }
            // 添加申请明细信息
            this.outcomeDetailCommonSaveService.saveBatch(outcomeDetailCommonSaveList);
            return outcomeDetailCommonSaveList;
        }
        return null;
    }

    /**
     * 保存出库基本信息
     *
     * @param outcomeWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author zphu
     * @date 2019/11/22 15:30
     * @throws
    **/
    protected void saveOutcomeMain(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam){
        if(outcomeWareCommonDto != null){
            OutcomeMainCommonSave outcomeMainCommonSave = new OutcomeMainCommonSave();
            BeanUtils.copyProperties(outcomeWareCommonDto, outcomeMainCommonSave);
            outcomeMainCommonSave.setId(KeyUtil.getId());
            outcomeMainCommonSave.setFlowInstanceId(0L);
            outcomeMainCommonSave.setCorpId(reqParam.getCorpId());
            outcomeMainCommonSave.setCreateBy(userInfo.getUserId());
            outcomeMainCommonSave.setApplyDate(DateUtil.format(new Date(),"yyyy-MM-dd"));
            outcomeMainCommonSave.setCreateTime(DateUtil.date().toTimestamp());
            outcomeMainCommonSave.setSaveStatus(SaveStatusEnum.SAVE.getCode());
            outcomeWareCommonDto.setId(outcomeMainCommonSave.getId());
            // 添加申请单信息
            this.outcomeMainCommonSaveService.save(outcomeMainCommonSave);
        }
    }

    protected OutcomeWareCommon makeOutcomeWareCommon(OutcomeWareCommonDto outcomeWareCommonDto,
                                                      StockCommon stockCommon, UserInfo userInfo,
                                                    ReqParam reqParam) {
        OutcomeWareCommon outcomeWareCommon = null;
        if (outcomeWareCommonDto != null && stockCommon != null) {
            outcomeWareCommon = new OutcomeWareCommon();
            BeanUtils.copyProperties(outcomeWareCommonDto, outcomeWareCommon);
            outcomeWareCommon.setCorpId(reqParam.getCorpId());
            outcomeWareCommon.setCreateBy(userInfo.getUserId());
            outcomeWareCommon.setCreateTime(DateUtil.date());
            outcomeWareCommon.setApplyDate(DateUtil.format(new Date(),"yyyy-MM-dd"));
            outcomeWareCommon.setOutcomeStatus(OutcomeStatusEnum.NO_OUTCOME.getCode());
            // 设置物料详细信息
            BeanUtils.copyProperties(stockCommon,outcomeWareCommon,"corpId","createBy","createTime","id","largeClassId","smallClassId");
            outcomeWareCommon.setStockId(stockCommon.getId());
        }
        return outcomeWareCommon;
    }


}
