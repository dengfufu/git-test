package com.zjft.usp.anyfix.settle.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.settle.dto.SettleStaffRecordDto;
import com.zjft.usp.anyfix.settle.enums.SettleStatusEnum;
import com.zjft.usp.anyfix.settle.filter.SettleStaffRecordFilter;
import com.zjft.usp.anyfix.settle.mapper.SettleStaffRecordMapper;
import com.zjft.usp.anyfix.settle.model.SettleStaff;
import com.zjft.usp.anyfix.settle.model.SettleStaffDetail;
import com.zjft.usp.anyfix.settle.model.SettleStaffRecord;
import com.zjft.usp.anyfix.settle.service.SettleStaffDetailService;
import com.zjft.usp.anyfix.settle.service.SettleStaffRecordService;
import com.zjft.usp.anyfix.settle.service.SettleStaffService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工结算记录 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Service
@Transactional(rollbackFor = AppException.class)
public class SettleStaffRecordServiceImpl extends ServiceImpl<SettleStaffRecordMapper, SettleStaffRecord> implements SettleStaffRecordService {

    @Resource
    private UasFeignService uasFeignService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private SettleStaffService settleStaffService;
    @Autowired
    private SettleStaffDetailService settleStaffDetailService;

    @Override
    public ListWrapper<SettleStaffRecordDto> pageByFilter(SettleStaffRecordFilter settleStaffRecordFilter){
        if(settleStaffRecordFilter == null || LongUtil.isZero(settleStaffRecordFilter.getServiceCorp())){
            throw new AppException("服务商企业编号不能为空!");
        }
        Page page = new Page(settleStaffRecordFilter.getPageNum(), settleStaffRecordFilter.getPageSize());
        QueryWrapper<SettleStaffRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", settleStaffRecordFilter.getServiceCorp());
        if(!StrUtil.isBlank(settleStaffRecordFilter.getRecordName())){
            queryWrapper.like("record_name", settleStaffRecordFilter.getRecordName());
        }
        queryWrapper.orderByDesc("operate_time");
        IPage<SettleStaffRecord> settleStaffRecordIPage = this.page(page, queryWrapper);
        ListWrapper<SettleStaffRecordDto> listWrapper = new ListWrapper<>();
        if(settleStaffRecordIPage != null && settleStaffRecordIPage.getRecords() != null && settleStaffRecordIPage.getRecords().size() > 0) {
            List<SettleStaffRecordDto> dtoList = new ArrayList<>();
            List<Long> userIdList = new ArrayList<>();
            for(SettleStaffRecord settleStaffRecord: settleStaffRecordIPage.getRecords()){
                userIdList.add(settleStaffRecord.getOperator());
            }
            Map<Long, String> corpUserNameMap = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList)).getData();
            for(SettleStaffRecord settleStaffRecord: settleStaffRecordIPage.getRecords()){
                SettleStaffRecordDto dto = new SettleStaffRecordDto();
                BeanUtils.copyProperties(settleStaffRecord, dto);
                dto.setOperatorName(corpUserNameMap.get(dto.getOperator()));
                dtoList.add(dto);
            }
            listWrapper.setList(dtoList);
        }
        listWrapper.setTotal(settleStaffRecordIPage.getTotal());
        return listWrapper;
    }

    @Override
    public void add(SettleStaffRecordDto settleStaffRecordDto, Long curUserId){
        if(settleStaffRecordDto == null || LongUtil.isZero(settleStaffRecordDto.getServiceCorp())){
            throw new AppException("服务商企业编号不能为空！");
        }
        if(settleStaffRecordDto.getUserIdList() == null || settleStaffRecordDto.getUserIdList().size() <= 0){
            throw new AppException("请先选择结算员工！");
        }
        if(settleStaffRecordDto.getStartDate() ==  null || settleStaffRecordDto.getEndDate() == null){
            throw new AppException("清先选择结算起止时间！");
        }
        SettleStaffRecord settleStaffRecord = new SettleStaffRecord();
        BeanUtils.copyProperties(settleStaffRecordDto, settleStaffRecord);
        settleStaffRecord.setRecordId(KeyUtil.getId());
        settleStaffRecord.setOperator(curUserId);
        QueryWrapper<WorkDeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", settleStaffRecordDto.getServiceCorp());
        queryWrapper.in("engineer", settleStaffRecordDto.getUserIdList());
        queryWrapper.eq("work_status", WorkStatusEnum.CLOSED);
        queryWrapper.le("finish_time", settleStaffRecordDto.getEndDate());
        queryWrapper.lt("finish_time", settleStaffRecordDto.getStartDate());
        List<WorkDeal> workDeals = this.workDealService.list(queryWrapper);
        Map<Long, List<Long>> userIdAndWorkIdsMap = new HashMap<>();
        if(workDeals != null && workDeals.size() > 0){
            for(WorkDeal workDeal: workDeals){
                if(userIdAndWorkIdsMap.containsKey(workDeal.getEngineer())){
                    List<Long> workIdList = userIdAndWorkIdsMap.get(workDeal.getEngineer());
                    workIdList.add(workDeal.getWorkId());
                    userIdAndWorkIdsMap.put(workDeal.getEngineer(), workIdList);
                }else{
                    List<Long> workIdList = new ArrayList<>();
                    workIdList.add(workDeal.getWorkId());
                    userIdAndWorkIdsMap.put(workDeal.getEngineer(), workIdList);
                }
            }
        }
        List<SettleStaff> settleStaffs = new ArrayList<>();
        List<SettleStaffDetail> settleStaffDetails = new ArrayList<>();
        for(Long userId: settleStaffRecordDto.getUserIdList()){
            List<Long> workIdList = userIdAndWorkIdsMap.get(userId);
            SettleStaff settleStaff = new SettleStaff();
            settleStaff.setSettleId(KeyUtil.getId());
            settleStaff.setRecordId(settleStaffRecord.getRecordId());
            settleStaff.setUserId(userId);
            settleStaff.setWorkQuantity(workIdList == null ? 0 : workIdList.size());
            settleStaff.setStatus(SettleStatusEnum.UNCHECKED.getCode());
            settleStaff.setResult(SettleStatusEnum.UNCHECKED.getCode());
            settleStaffs.add(settleStaff);
            if(workIdList != null && settleStaffs.size() > 0){
                for(Long workId: workIdList){
                    SettleStaffDetail settleStaffDetail = new SettleStaffDetail();
                    settleStaffDetail.setSettleId(settleStaff.getSettleId());
                    settleStaffDetail.setWorkId(workId);
                    settleStaffDetails.add(settleStaffDetail);
                }
            }
        }
        this.save(settleStaffRecord);
        this.settleStaffService.saveBatch(settleStaffs);
        this.settleStaffDetailService.saveBatch(settleStaffDetails);
    }

    @Override
    public void deleteById(Long recordId) {
        if(LongUtil.isZero(recordId)){
            throw new AppException("主键不能为空！");
        }
        SettleStaffRecord settleStaffRecord = this.getById(recordId);
        Assert.notNull(settleStaffRecord, "结算记录不存在！");
        UpdateWrapper<SettleStaffDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("settle_id", "select settle_id from settle_staff where record_id="+recordId);
        this.settleStaffDetailService.remove(updateWrapper);
        this.settleStaffService.remove(new UpdateWrapper<SettleStaff>().eq("record_id", recordId));
        this.removeById(recordId);
    }

}
