package com.zjft.usp.wms.business.outcome.strategy.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.book.model.BookVendorCommon;
import com.zjft.usp.wms.business.book.service.BookVendorCommonService;
import com.zjft.usp.wms.business.outcome.dto.OutcomeWareCommonDto;
import com.zjft.usp.wms.business.outcome.model.OutcomeDetailCommonSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeMainSaleSave;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareReturnVendor;
import com.zjft.usp.wms.business.outcome.service.OutcomeDetailReturnSaveVendorService;
import com.zjft.usp.wms.business.outcome.service.OutcomeWareReturnVendorService;
import com.zjft.usp.wms.business.outcome.strategy.common.OutcomeStrategy;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 物料归厂商有确认到货处理
 * @Author: JFZOU
 * @Date: 2019-11-21 9:56
 */
@Service(OutcomeStrategy.OUTCOME_RETURN_VENDOR)
public class OutcomeReturnVendorStrategy extends AbstractOutcomeStrategy {

    @Autowired
    private OutcomeWareReturnVendorService outcomeWareReturnVendorService;
    @Autowired
    private AnyfixFeignService anyfixFeignService;
    @Autowired
    private BookVendorCommonService bookVendorCommonService;
    @Autowired
    private OutcomeDetailReturnSaveVendorService outcomeDetailReturnSaveVendorService;

    @Override
    public List<OutcomeDetailCommonSave> save(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        List<OutcomeDetailCommonSave> outcomeDetailCommonSaveList = super.save(outcomeWareCommonDto,userInfo,reqParam);
//        if(CollectionUtil.isNotEmpty(outcomeDetailCommonSaveList)){
//            OutcomeMainSaleSave outcomeMainSaleSave = new OutcomeMainSaleSave();
//            outcomeMainSaleSave.setAssistUserId(outcomeWareCommonDto.getAssistUserId());
//            outcomeMainSaleSave.setId(outcomeDetailCommonSaveList.get(0).getId());
//            this.outcomeWareReturnVendorService.save(outcomeMainSaleSave);
//        }
        return outcomeDetailCommonSaveList;
    }

    @Override
    public List<OutcomeWareCommon> add(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        List<OutcomeWareCommon> outcomeWareCommonList = super.add(outcomeWareCommonDto, userInfo, reqParam);
        if(CollectionUtil.isNotEmpty(outcomeWareCommonList)){
            List<OutcomeWareReturnVendor> outcomeWareReturnVendorList = new ArrayList<>();
            OutcomeWareReturnVendor outcomeWareReturnVendor = new OutcomeWareReturnVendor();
            outcomeWareReturnVendor.setSupplierId(outcomeWareCommonDto.getSupplierId());
            if(LongUtil.isNotZero(outcomeWareCommonDto.getWorkId())){
                outcomeWareReturnVendor.setWorkId(outcomeWareCommonDto.getWorkId());

                Result workResult = this.anyfixFeignService.findWorkById(outcomeWareCommonDto.getWorkId());
                if(workResult != null){
                    String json =  JsonUtil.toJson(workResult.getData());
                    if(StringUtils.isNotEmpty(json)){
                        outcomeWareReturnVendor.setDeviceSn(JsonUtil.parseValue(json,"serial").toString());
                        String modelId = JsonUtil.parseValue(json,"model").toString();
                        if(StringUtils.isNumeric(modelId)){
                            outcomeWareReturnVendor.setDeviceModelId(Long.parseLong(modelId));
                        }
                    }
                }else{
                    throw new AppException("工单信息查询失败，请重试！");
                }
                BookVendorCommon bookVendorCommon = this.bookVendorCommonService.getOne(new QueryWrapper<BookVendorCommon>().eq("work_id",outcomeWareCommonDto.getWorkId()));
                if(bookVendorCommon != null){
                    outcomeWareReturnVendor.setIncomeId(bookVendorCommon.getId());
                }
            }
            for(OutcomeWareCommon outcomeWareCommon : outcomeWareCommonList){
                OutcomeWareReturnVendor outcomeSaved = new OutcomeWareReturnVendor();
                BeanUtils.copyProperties(outcomeWareReturnVendor,outcomeSaved);
                outcomeSaved.setId(outcomeWareCommon.getId());
                outcomeWareReturnVendorList.add(outcomeSaved);
            }
            this.outcomeWareReturnVendorService.saveBatch(outcomeWareReturnVendorList);
        }
        return outcomeWareCommonList;
    }

    @Override
    public void delete(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam){
        super.delete(outcomeWareCommonDto,userInfo,reqParam);
        this.outcomeWareReturnVendorService.removeByIds(outcomeWareCommonDto.getIdList());
    }

    @Override
    public void update(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.update(outcomeWareCommonDto,userInfo,reqParam);
        OutcomeWareReturnVendor outcomeWareReturnVendor = this.outcomeWareReturnVendorService.getById(outcomeWareCommonDto.getId());
        BeanUtils.copyProperties(outcomeWareCommonDto,outcomeWareReturnVendor);
        if(LongUtil.isNotZero(outcomeWareCommonDto.getWorkId())){
            outcomeWareReturnVendor.setWorkId(outcomeWareCommonDto.getWorkId());

            Result workResult = this.anyfixFeignService.findWorkById(outcomeWareCommonDto.getWorkId());
            JSONObject jsonObject = JsonUtil.parseObject(JsonUtil.toJson(workResult.getData()),JSONObject.class);
            outcomeWareReturnVendor.setDeviceSn(jsonObject.get("serial").toString());
            String modelId = jsonObject.get("model").toString();
            if(StringUtils.isNumeric(modelId)){
                outcomeWareReturnVendor.setDeviceModelId(Long.parseLong(modelId));
            }

            BookVendorCommon bookVendorCommon = this.bookVendorCommonService.getOne(new QueryWrapper<BookVendorCommon>().eq("work_id",outcomeWareCommonDto.getWorkId()));
            if(bookVendorCommon != null){
                outcomeWareReturnVendor.setIncomeId(bookVendorCommon.getId());
            }
        }
        this.outcomeWareReturnVendorService.updateById(outcomeWareReturnVendor);
    }

    @Override
    public void batchUpdate(OutcomeWareCommonDto outcomeWareCommonDto, UserInfo userInfo, ReqParam reqParam) {
        super.batchUpdate(outcomeWareCommonDto,userInfo,reqParam);
        // 根据workid获取设备和厂商相关信息
        if(LongUtil.isNotZero(outcomeWareCommonDto.getWorkId())){
            Result workResult = this.anyfixFeignService.findWorkById(outcomeWareCommonDto.getWorkId());
            JSONObject jsonObject = JsonUtil.parseObject(JsonUtil.toJson(workResult.getData()),JSONObject.class);
            outcomeWareCommonDto.setDeviceSn(jsonObject.get("serial").toString());
            String modelId = jsonObject.get("model").toString();
            if(StringUtils.isNumeric(modelId)){
                outcomeWareCommonDto.setDeviceModelId(Long.parseLong(modelId));
            }
            BookVendorCommon bookVendorCommon = this.bookVendorCommonService.getOne(new QueryWrapper<BookVendorCommon>().eq("work_id",outcomeWareCommonDto.getWorkId()));
            if(bookVendorCommon != null){
                outcomeWareCommonDto.setIncomeId(bookVendorCommon.getId());
            }
        }
        if(CollectionUtil.isNotEmpty(outcomeWareCommonDto.getIdList())){
            Collection<OutcomeWareReturnVendor> outcomeWareReturnVendorList =  this.outcomeWareReturnVendorService.listByIds(outcomeWareCommonDto.getIdList());
            if(CollectionUtil.isNotEmpty(outcomeWareReturnVendorList)){
                for(OutcomeWareReturnVendor outcomeWareReturnVendor : outcomeWareReturnVendorList){
                    BeanUtils.copyProperties(outcomeWareCommonDto,outcomeWareReturnVendor);
                }
                this.outcomeWareReturnVendorService.updateBatchById(outcomeWareReturnVendorList);
            }
        }
    }
}
