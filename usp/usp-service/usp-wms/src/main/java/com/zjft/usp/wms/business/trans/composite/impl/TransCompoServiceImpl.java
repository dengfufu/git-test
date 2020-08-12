package com.zjft.usp.wms.business.trans.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import com.zjft.usp.wms.baseinfo.enums.SituationEnum;
import com.zjft.usp.wms.baseinfo.enums.SmallClassEnum;
import com.zjft.usp.wms.baseinfo.model.*;
import com.zjft.usp.wms.baseinfo.service.*;
import com.zjft.usp.wms.business.common.enums.SaveStatusEnum;
import com.zjft.usp.wms.business.consign.composite.ConsignCompoService;
import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.stock.filter.StockCommonFilter;
import com.zjft.usp.wms.business.stock.model.StockCommon;
import com.zjft.usp.wms.business.stock.service.StockCommonService;
import com.zjft.usp.wms.business.trans.composite.TransCompoService;
import com.zjft.usp.wms.business.trans.dto.TransDetailCommonSaveDto;
import com.zjft.usp.wms.business.trans.dto.TransStatCountDto;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.enums.CountEnum;
import com.zjft.usp.wms.business.trans.enums.TransStatusEnum;
import com.zjft.usp.wms.business.trans.filter.TransFilter;
import com.zjft.usp.wms.business.trans.model.TransDetailCommonSave;
import com.zjft.usp.wms.business.trans.model.TransMainCommonSave;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import com.zjft.usp.wms.business.trans.service.TransDetailCommonSaveService;
import com.zjft.usp.wms.business.trans.service.TransMainCommonSaveService;
import com.zjft.usp.wms.business.trans.service.TransWareCommonService;
import com.zjft.usp.wms.business.trans.strategy.factory.TransStrategyFactory;
import com.zjft.usp.wms.flow.enums.NodeTypeEnum;
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
 * 物料调拨服务类
 *
 * @Author: JFZOU
 * @Date: 2019-11-13 15:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TransCompoServiceImpl implements TransCompoService {

    @Autowired
    private TransDetailCommonSaveService transDetailCommonSaveService;
    @Autowired
    private TransMainCommonSaveService transMainCommonSaveService;
    @Autowired
    private TransWareCommonService transWareCommonService;
    @Autowired
    private TransStrategyFactory transStrategyFactory;
    @Autowired
    private LargeClassService largeClassService;
    @Autowired
    private SmallClassService smallClassService;
    @Autowired
    private WareModelService wareModelService;
    @Autowired
    private WareCatalogService wareCatalogService;
    @Autowired
    private WareBrandService wareBrandService;
    @Autowired
    private WareDepotService wareDepotService;
    @Autowired
    private WareStatusService wareStatusService;
    @Autowired
    private FlowInstanceNodeService flowInstanceNodeService;
    @Autowired
    private FlowInstanceTraceService flowInstanceTraceService;
    @Autowired
    private ConsignCompoService consignCompoService;
    @Autowired
    private StockCommonService stockCommonService;
    @Resource
    private UasFeignService uasFeignService;


    /**
     * 保存调拨单申请信息
     *
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    @Override
    public void save(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        this.transStrategyFactory.getStrategy(transWareCommonDto.getSmallClassId().toString()).save(transWareCommonDto,
                userInfo, reqParam);
    }

    /**
     * 添加调拨单申请信息并自动拆单
     *
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    @Override
    public void add(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        this.transStrategyFactory.getStrategy(transWareCommonDto.getSmallClassId().toString()).add(transWareCommonDto,
                userInfo, reqParam);
    }

    /**
     * 审批申请单
     *
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     * @author Qiugm
     * @date 2019-11-21
     */
    @Override
    public void audit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        this.transStrategyFactory.getStrategy(transWareCommonDto.getSmallClassId().toString()).audit(transWareCommonDto,
                userInfo, reqParam);
    }

    /**
     * 分页查询调拨单
     *
     * @param transFilter
     * @return
     * @author canlei
     * @date 2019-12-09
     */
    @Override
    public ListWrapper<TransWareCommonDto> queryTrans(TransFilter transFilter) {
        ListWrapper<TransWareCommonDto> listWrapper = new ListWrapper<>();
        if (transFilter == null || LongUtil.isZero(transFilter.getCorpId())) {
            return listWrapper;
        }
        Page page = new Page(transFilter.getPageNum(), transFilter.getPageSize());
        List<TransWareCommonDto> dtoList = this.transWareCommonService.queryByPage(page, transFilter);

        // 查询库存转拨审批节点列表
        if(transFilter.getSmallClassId() == SmallClassEnum.TRANS_WARE_TRANSFER.getCode()
                && transFilter.getFlowNodeTypeList()!= null &&
                transFilter.getFlowNodeTypeList().get(0) == NodeTypeEnum.COMMON_APPROVAL.getCode()){

            setActualQty(dtoList,transFilter);
        }
        // 增加附加属性
        this.addExtraAttribute(dtoList, transFilter.getCorpId());
        listWrapper.setTotal(page.getTotal());
        listWrapper.setList(dtoList);
        return listWrapper;
    }

    @Override
    public TransWareCommonDto queryDetail(Long transId,ReqParam reqParam) {
        // 直接调用已有的接口
        Page page = new Page(1,1);
        TransFilter transFilter = new TransFilter();
        transFilter.setTransId(transId);
        List<TransWareCommonDto> dtoList = this.transWareCommonService.queryByPage(page, transFilter);
        TransWareCommonDto transWareCommonDto = null;
        if(CollectionUtil.isNotEmpty(dtoList)){
            // 增加附加属性
            this.addExtraAttribute(dtoList, reqParam.getCorpId());
            transWareCommonDto = dtoList.get(0);
            // 查询流程信息
            if (LongUtil.isNotZero(transWareCommonDto.getFlowInstanceId())) {
                transWareCommonDto.setFlowInstanceNodeList(this.flowInstanceNodeService.listAllBy(transWareCommonDto.getFlowInstanceId()));
                transWareCommonDto.setFlowInstanceTraceDtoList(this.flowInstanceTraceService.listSortBy(transWareCommonDto.getFlowInstanceId(), reqParam));
            }
            // 查询发货信息
            transWareCommonDto.setConsignDetailDtoList(this.consignCompoService.findByFormDetailId(transId,reqParam));
        }
        return transWareCommonDto;
    }

    /**
     * 分页查询保存的调拨单
     *
     * @author canlei
     * @date 2019-12-09
     * @param transFilter
     * @return
     */
    @Override
    public ListWrapper<TransWareCommonDto> querySavedTrans(TransFilter transFilter, UserInfo userInfo, ReqParam reqParam) {
        ListWrapper<TransWareCommonDto> listWrapper = new ListWrapper<>();
        if(transFilter == null) {
            return listWrapper;
        }
        Page page = new Page(transFilter.getPageNum(), transFilter.getPageSize());
        List<TransWareCommonDto> dtoList = this.transMainCommonSaveService
                .queryByPage(page, userInfo.getUserId(), reqParam.getCorpId());

        this.addExtraAttribute(dtoList, reqParam.getCorpId());
        listWrapper.setTotal(page.getTotal());
        listWrapper.setList(dtoList);
        return listWrapper;
    }

    /**
     * 查询单个已提交的调拨单信息
     * 保存和提交共用，保存的明细放在transDetailCommonSaveDtoList
     *
     * @param transId
     * @param reqParam
     * @return
     */
    @Override
    public TransWareCommonDto viewTrans(Long transId, ReqParam reqParam) {
        TransWareCommonDto transWareCommonDto = new TransWareCommonDto();
        if (LongUtil.isZero(transId)) {
            throw new AppException("调拨单编号不能为空");
        }
        TransMainCommonSave transMainCommonSave = this.transMainCommonSaveService.getById(transId);
        if (transMainCommonSave != null && SaveStatusEnum.SAVE.getCode() == transMainCommonSave.getSaveStatus()) {
            BeanUtils.copyProperties(transMainCommonSave, transWareCommonDto);
            List<TransDetailCommonSaveDto> detailDtoList = new ArrayList<>();
            List<TransDetailCommonSave> detailList = this.transDetailCommonSaveService.listAllSortBy(transId);
            if (CollectionUtil.isNotEmpty(detailList)) {
                detailDtoList = detailList.stream().map(transDetailCommonSave -> (TransDetailCommonSaveDto)transDetailCommonSave)
                        .collect(Collectors.toList());
            }
            transWareCommonDto.setTransDetailCommonSaveDtoList(detailDtoList);
        } else {
            TransWareCommon transWareCommon = this.transWareCommonService.getById(transId);
            if (transWareCommon == null) {
                throw new AppException("查询的调拨单不存在，请检查");
            }
            BeanUtils.copyProperties(transWareCommon, transWareCommonDto);

            // 查询流程节点信息
            transWareCommonDto.setFlowInstanceNodeList(this.flowInstanceNodeService.listAllBy(transWareCommon.getFlowInstanceId()));
            // 查询流程历史信息
            transWareCommonDto.setFlowInstanceTraceDtoList(this.flowInstanceTraceService.listSortBy(transWareCommon.getFlowInstanceId()
                    , reqParam));
            // 查询发货列表
            transWareCommonDto.setConsignDetailDtoList(this.consignCompoService.findByFormDetailId(transWareCommon.getId(), reqParam));
        }
        // 增加附加属性
        this.addExtraAttribute(transWareCommonDto, reqParam.getCorpId());
        return transWareCommonDto;
    }

    @Override
    public void batchAudit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam,Integer status) {
        this.transStrategyFactory.getStrategy(transWareCommonDto.getSmallClassId().toString()).batchAudit(transWareCommonDto,
                userInfo, reqParam, status);
    }

    @Override
    public Map<Integer, Long> countByWareStatus(TransFilter transFilter, UserInfo userInfo, ReqParam reqParam) {
        // 所有状态都需要查
        transFilter.setTransStatusList(null);

        List<TransStatCountDto> transStatCountDtoList = this.transWareCommonService.countByWareStatus(transFilter);
        Map<Integer,Long> returnMap = new HashMap<>();
        Long count = 0L;
        //TODO 计算暂存单
        for(TransStatCountDto transStatCountDto : transStatCountDtoList){
            count = transStatCountDto.getTransNumber() + count;
            if(transStatCountDto.getFlowNodeType() != null){
                returnMap.put(transStatCountDto.getFlowNodeType(),transStatCountDto.getTransNumber());
            } else {
                if(transStatCountDto.getTransStatus() == TransStatusEnum.COMPLETE_ALLOCATION.getCode()){
                    returnMap.put(CountEnum.END.getCode(),transStatCountDto.getTransNumber());
                }
            }
        }
        returnMap.put(CountEnum.ALL_COUNT.getCode(),count);
        return returnMap;
    }


    /**
     * 增加附加属性
     *
     * @author canlei
     * @date 2019-12-09
     * @param dtoList
     * @param corpId
     */
    private void addExtraAttribute(List<TransWareCommonDto> dtoList, Long corpId) {
        if (CollectionUtil.isNotEmpty(dtoList)) {
            List<Long> userIdList = new ArrayList<Long>();
            for (TransWareCommonDto transWareCommonDto : dtoList) {
                userIdList.add(transWareCommonDto.getCreateBy());
                userIdList.add(transWareCommonDto.getUpdateBy());
            }

            Map<Long, String> userMap = new HashMap<Long, String>(16);
            if (CollectionUtil.isNotEmpty(userIdList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
                // 人员映射
                userMap = userMapResult.getData();
            }

            // 业务大类映射
            Map<Integer, String> largeClassIdAndNameMap = this.largeClassService.mapClassIdAndName(corpId);
            // 业务小类映射
            Map<Integer, String> smallClassIdAndNameMap = this.smallClassService.mapClassIdAndName(corpId);
            // 库房映射
            Map<Long, String> depotIdAndNameMap = this.wareDepotService.mapDepotIdAndName(corpId);
            // 物料型号映射
            Map<Long, WareModel> modelMap = this.wareModelService.mapIdAndModel(corpId);
            // 物料分类映射
            Map<Long, String> catalogIdAndNameMap = this.wareCatalogService.mapCatalogIdAndName(corpId);
            // 物料品牌映射
            Map<Long, String> brandIdAndNameMap = this.wareBrandService.mapBrandIdAndName(corpId);
            // 物料状态映射
            Map<Integer, String> wareStatusMap = this.wareStatusService.mapIdAndName(corpId);

            for (TransWareCommonDto transWareCommonDto : dtoList) {
                WareModel model = modelMap.get(transWareCommonDto.getModelId());
                if (model != null) {
                    transWareCommonDto.setModelName(model.getName());
                    transWareCommonDto.setCatalogId(model.getCatalogId());
                    transWareCommonDto.setBrandId(model.getBrandId());
                    transWareCommonDto.setCatalogName(catalogIdAndNameMap.get(model.getCatalogId()));
                    transWareCommonDto.setBrandName(brandIdAndNameMap.get(model.getBrandId()));
                }
                transWareCommonDto.setCreateByName(userMap.get(transWareCommonDto.getCreateBy()));
                transWareCommonDto.setUpdateByName(userMap.get(transWareCommonDto.getUpdateBy()));
                transWareCommonDto.setLargeClassName(largeClassIdAndNameMap.get(transWareCommonDto.getLargeClassId()));
                transWareCommonDto.setSmallClassName(smallClassIdAndNameMap.get(transWareCommonDto.getSmallClassId()));
                transWareCommonDto.setFromDepotName(depotIdAndNameMap.get(transWareCommonDto.getFromDepotId()));
                transWareCommonDto.setToDepotName(depotIdAndNameMap.get(transWareCommonDto.getToDepotId()));
                transWareCommonDto.setStatusName(wareStatusMap.get(transWareCommonDto.getStatus()));
            }
        }
    }

    /**
     * 增加附加属性
     *
     * @author canlei
     * @date 2019-12-09
     * @param transWareCommonDto
     * @param corpId
     */
    private void addExtraAttribute(TransWareCommonDto transWareCommonDto, Long corpId) {
        if (transWareCommonDto != null) {
            List<Long> userIdList = new ArrayList<Long>();
            userIdList.add(transWareCommonDto.getCreateBy());
            userIdList.add(transWareCommonDto.getUpdateBy());

            Map<Long, String> userMap = new HashMap<Long, String>(16);
            if (CollectionUtil.isNotEmpty(userIdList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
                // 人员映射
                userMap = userMapResult.getData();
            }

            // 业务大类映射
            Map<Integer, String> largeClassIdAndNameMap = this.largeClassService.mapClassIdAndName(corpId);
            // 业务小类映射
            Map<Integer, String> smallClassIdAndNameMap = this.smallClassService.mapClassIdAndName(corpId);
            // 库房映射
            Map<Long, String> depotIdAndNameMap = this.wareDepotService.mapDepotIdAndName(corpId);
            // 物料型号映射
            Map<Long, WareModel> modelMap = this.wareModelService.mapIdAndModel(corpId);
            // 物料分类映射
            Map<Long, String> catalogIdAndNameMap = this.wareCatalogService.mapCatalogIdAndName(corpId);
            // 物料品牌映射
            Map<Long, String> brandIdAndNameMap = this.wareBrandService.mapBrandIdAndName(corpId);
            // 物料状态映射
            Map<Integer, String> wareStatusMap = this.wareStatusService.mapIdAndName(corpId);

            WareModel model = modelMap.get(transWareCommonDto.getModelId());
            if (model != null) {
                transWareCommonDto.setModelName(model.getName());
                transWareCommonDto.setCatalogId(model.getCatalogId());
                transWareCommonDto.setBrandId(model.getBrandId());
                transWareCommonDto.setCatalogName(catalogIdAndNameMap.get(model.getCatalogId()));
                transWareCommonDto.setBrandName(brandIdAndNameMap.get(model.getBrandId()));
            }
            transWareCommonDto.setCreateByName(userMap.get(transWareCommonDto.getCreateBy()));
            transWareCommonDto.setUpdateByName(userMap.get(transWareCommonDto.getUpdateBy()));
            transWareCommonDto.setLargeClassName(largeClassIdAndNameMap.get(transWareCommonDto.getLargeClassId()));
            transWareCommonDto.setSmallClassName(smallClassIdAndNameMap.get(transWareCommonDto.getSmallClassId()));
            transWareCommonDto.setFromDepotName(depotIdAndNameMap.get(transWareCommonDto.getFromDepotId()));
            transWareCommonDto.setToDepotName(depotIdAndNameMap.get(transWareCommonDto.getToDepotId()));
            transWareCommonDto.setStatusName(wareStatusMap.get(transWareCommonDto.getStatus()));

            // 若有保存的明细，则增加附加属性
            if (CollectionUtil.isNotEmpty(transWareCommonDto.getTransDetailCommonSaveDtoList())) {
                transWareCommonDto.getTransDetailCommonSaveDtoList().forEach(transDetailCommonSaveDto -> {
                    WareModel wareModel = modelMap.get(transDetailCommonSaveDto.getModelId());
                    if (wareModel != null) {
                        transDetailCommonSaveDto.setCatalogId(wareModel.getCatalogId());
                        transDetailCommonSaveDto.setCatalogName(catalogIdAndNameMap.get(wareModel.getCatalogId()));
                        transDetailCommonSaveDto.setBrandId(wareModel.getBrandId());
                        transDetailCommonSaveDto.setBrandName(brandIdAndNameMap.get(wareModel.getBrandId()));
                        transDetailCommonSaveDto.setModelName(wareModel.getName());
                    }
                    transDetailCommonSaveDto.setStatusName(wareStatusMap.get(transDetailCommonSaveDto.getStatus()));
                });
            }
        }

    }

    public void setActualQty(List<TransWareCommonDto> dtoList,TransFilter transFilter){
        if(CollectionUtil.isEmpty(dtoList)){
            return;
        }
        Map<String,Object> map = new HashMap<>(3);
        map.put("corpId",transFilter.getCorpId());
        map.put("depotId",transFilter.getFromDepotId());
        map.put("largeClassId",transFilter.getLargeClassId());
        map.put("smallClassId",transFilter.getSmallClassId());
        map.put("situation", SituationEnum.STOCK.getCode());
        List<Map<String,Object>> list = new ArrayList<>();
        for(TransWareCommonDto dto : dtoList) {
            Map<String,Object> modelMap = new HashMap<>();
            modelMap.put("modelId",dto.getModelId());
            // todo设置normsValue
            list.add(modelMap);
        }
        map.put("list",list);
        List<StockCommon> stockCommonList = stockCommonService.selectQtyByTrans(map);
        for(StockCommon stockCommon : stockCommonList){
            for(int i=0;i< dtoList.size(); i++ ){
                if(dtoList.get(i).getModelId().longValue() == stockCommon.getModelId().longValue()){
                    dtoList.get(i).setDepotActualQty(stockCommon.getIncomeQty());
                    break;
                }
            }
        }
    }

}
