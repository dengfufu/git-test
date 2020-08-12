package com.zjft.usp.anyfix.settle.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.settle.dto.SettleCustomDto;
import com.zjft.usp.anyfix.settle.enums.SettleStatusEnum;
import com.zjft.usp.anyfix.settle.filter.SettleCustomFilter;
import com.zjft.usp.anyfix.settle.mapper.SettleCustomMapper;
import com.zjft.usp.anyfix.settle.model.SettleCustom;
import com.zjft.usp.anyfix.settle.model.SettleCustomDetail;
import com.zjft.usp.anyfix.settle.service.SettleCustomDetailService;
import com.zjft.usp.anyfix.settle.service.SettleCustomService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.service.UasFeignService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户结算单 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Service
@Transactional(rollbackFor = AppException.class)
public class SettleCustomServiceImpl extends ServiceImpl<SettleCustomMapper, SettleCustom> implements SettleCustomService {

    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private SettleCustomDetailService settleCustomDetailService;
    @Autowired
    private UasFeignService uasFeignService;

    @Override
    public Result addRecord(SettleCustomDto customSettleDto, Long curUserId){
        Assert.notNull(customSettleDto, "参数解析错误，请重试！");
        if(customSettleDto.getCustomCorp() == null || customSettleDto.getCustomCorp() == 0
                || customSettleDto.getServiceCorp() == null || customSettleDto.getServiceCorp() == 0){
            return Result.failed("客户或服务商不能为空！");
        }
        Assert.notNull(customSettleDto.getStartDate(), "结算开始时间不能为空！");
        Assert.notNull(customSettleDto.getStartDate(), "结算结束时间不能为空！");
        //根据服务商、客户和结算时间范围查询已完成工单
        QueryWrapper<WorkDeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_status", WorkStatusEnum.CLOSED.getCode());
        queryWrapper.eq("service_corp", customSettleDto.getServiceCorp());
        queryWrapper.eq("demander_corp", customSettleDto.getCustomCorp());
        queryWrapper.le("finish_time", customSettleDto.getEndDate());
        queryWrapper.ge("finish_time", customSettleDto.getStartDate());
        List<WorkDeal> workDealList = this.workDealService.list(queryWrapper);
        if(workDealList != null && workDealList.size() > 0){
            SettleCustom customSettle = new SettleCustom();
            customSettle.setSettleId(KeyUtil.getId());
            BeanUtils.copyProperties(customSettleDto, customSettle);
            customSettle.setWorkQuantity(workDealList.size());
            customSettle.setStatus(SettleStatusEnum.UNCHECKED.getCode());
            customSettle.setOperator(curUserId);
            customSettle.setOperateTime(DateTime.now().toTimestamp());
            this.save(customSettle);
            List<SettleCustomDetail> customSettleDetails = new ArrayList<>();
            for(WorkDeal workDeal:workDealList){
                SettleCustomDetail customSettleDetail = new SettleCustomDetail();
                customSettleDetail.setSettleId(customSettle.getSettleId());
                customSettleDetail.setWorkId(workDeal.getWorkId());
                customSettleDetails.add(customSettleDetail);
            }
            //暂定先批量插入
            this.settleCustomDetailService.saveBatch(customSettleDetails, customSettleDetails.size());
        }else{
            return Result.failed("所选时间范围内该客户无可结算工单!");
        }
        return Result.succeed("结算成功！");
    }

    @Override
    public ListWrapper<SettleCustomDto> pageByFilter(SettleCustomFilter filter){
        if(filter == null || filter.getServiceCorp() == null || filter.getServiceCorp() == 0){
            throw new AppException("服务商企业编号不能为空！");
        }
        QueryWrapper<SettleCustom> queryWrapper = new QueryWrapper<>();
        Page page = new Page(filter.getCurrent(), filter.getSize());
        queryWrapper.eq("service_corp", filter.getServiceCorp());
        if(!StringUtil.isNullOrEmpty(filter.getCustomName())){
            String jsonFilter = JsonUtil.toJsonString("corpName", filter.getCustomName());
            List<Long> corpIdList = this.uasFeignService.listCorpIdByFilter(jsonFilter).getData();
            if(corpIdList != null && corpIdList.size() > 0){
                queryWrapper.in("cunstom_corp", corpIdList);
            }
        }
        if(LongUtil.isNotZero(filter.getCustomCorp())){
            queryWrapper.eq("custom_corp", filter.getCustomCorp());
        }
        queryWrapper.orderByDesc("operate_time");
        IPage<SettleCustom> settleCustomIPage = this.page(page, queryWrapper);
        ListWrapper<SettleCustomDto> list = new ListWrapper<>();
        List<SettleCustomDto> dtoList = new ArrayList<>();
        if(settleCustomIPage != null && settleCustomIPage.getRecords() != null && settleCustomIPage.getRecords().size() > 0){
            List<Long> customIdList = new ArrayList<>();
            List<Long> userIdList = new ArrayList<>();
            for (SettleCustom settleCustom: settleCustomIPage.getRecords()){
                customIdList.add(settleCustom.getCustomCorp());
                userIdList.add(settleCustom.getOperator());
            }
            Map<Long, String> corpIdAndNameMap = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(customIdList)).getData();
            Map<Long, String> userIdAndNameMap = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList)).getData();
            for (SettleCustom settleCustom: settleCustomIPage.getRecords()){
                SettleCustomDto customDto = new SettleCustomDto();
                BeanUtils.copyProperties(settleCustom, customDto);
                customDto.setCustomName(corpIdAndNameMap.get(settleCustom.getCustomCorp()));
                customDto.setStatusName(SettleStatusEnum.lookup(customDto.getStatus()));
                customDto.setOperatorName(userIdAndNameMap.get(settleCustom.getOperator()));
                dtoList.add(customDto);
            }
        }
        list.setList(dtoList);
        list.setTotal(settleCustomIPage.getTotal());
        return list;
    }

    @Override
    public void deleteById(Long settleId) {
        if(LongUtil.isZero(settleId)){
            throw new AppException("主键不能为空！");
        }
        SettleCustom settleCustom = this.getById(settleId);
        Assert.notNull(settleCustom, "结算单不存在！");
        this.settleCustomDetailService.remove(new UpdateWrapper<SettleCustomDetail>().eq("settle_id", settleId));
        this.removeById(settleId);
    }

}
