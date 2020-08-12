package com.zjft.usp.anyfix.settle.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.settle.dto.SettleBranchDto;
import com.zjft.usp.anyfix.settle.enums.SettleStatusEnum;
import com.zjft.usp.anyfix.settle.filter.SettleBranchFilter;
import com.zjft.usp.anyfix.settle.mapper.SettleBranchMapper;
import com.zjft.usp.anyfix.settle.model.SettleBranch;
import com.zjft.usp.anyfix.settle.model.SettleBranchDetail;
import com.zjft.usp.anyfix.settle.service.SettleBranchDetailService;
import com.zjft.usp.anyfix.settle.service.SettleBranchService;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网点结算单 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Service
@Transactional(rollbackFor = AppException.class)
public class SettleBranchServiceImpl extends ServiceImpl<SettleBranchMapper, SettleBranch> implements SettleBranchService {

    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private SettleBranchDetailService settleBranchDetailService;
    @Autowired
    private ServiceBranchService serviceBranchService;

    @Resource
    private UasFeignService uasFeignService;

    @Override
    public Result batchAddBranchSettle(SettleBranchDto settleBranchDto, Long curUserId){
        if(settleBranchDto == null || settleBranchDto.getBranchId() == null || settleBranchDto.getBranchId() == 0){
            throw new AppException("参数解析失败！");
        }
        SettleBranch settleBranch = new SettleBranch();
        // 批量生成结算单和结算单明细
        QueryWrapper<WorkDeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_status", WorkStatusEnum.CLOSED.getCode());
        queryWrapper.eq("service_corp", settleBranchDto.getServiceCorp());
        queryWrapper.inSql("work_id", "select work_id from work_request where service_branch="+settleBranchDto.getBranchId());
        queryWrapper.le("finish_time", settleBranchDto.getEndDate());
        queryWrapper.ge("finish_time", settleBranchDto.getStartDate());
        List<WorkDeal> workDealList = this.workDealService.list(queryWrapper);
        BeanUtils.copyProperties(settleBranchDto, settleBranch);
        settleBranch.setSettleId(KeyUtil.getId());
        settleBranch.setOperator(curUserId);
        settleBranch.setOperateTime(DateTime.now().toTimestamp());
        settleBranch.setWorkQuantity(workDealList == null || workDealList.size() <= 0 ? 0 : workDealList.size());
        settleBranch.setStatus(SettleStatusEnum.UNCHECKED.getCode());
        settleBranch.setResult(SettleStatusEnum.UNCHECKED.getCode());
        save(settleBranch);
        List<SettleBranchDetail> details = new ArrayList<>();
        if(workDealList != null && workDealList.size() > 0){
            for(WorkDeal workDeal: workDealList){
                SettleBranchDetail settleBranchDetail = new SettleBranchDetail();
                settleBranchDetail.setSettleId(settleBranch.getSettleId());
                settleBranchDetail.setWorkId(workDeal.getWorkId());
                details.add(settleBranchDetail);
            }
            this.settleBranchDetailService.saveBatch(details, details.size());
        }
        return Result.succeed("结算成功！");
    }

    @Override
    public ListWrapper<SettleBranchDto> pageByFilter(SettleBranchFilter filter){
        if(filter == null || LongUtil.isZero(filter.getServiceCorp())){
            throw new AppException("服务商企业编号不能为空！");
        }
        Page page = new Page(filter.getCurrent(), filter.getSize());
        QueryWrapper<SettleBranch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", filter.getServiceCorp());
        if(!StringUtil.isNullOrEmpty(filter.getBranchName())){
            queryWrapper.inSql("branch_id", "select branch_id from service_branch where branch_name like '%" +filter.getBranchName()+ "%'");
        }
        if(LongUtil.isNotZero(filter.getBranchId())){
            queryWrapper.eq("branch_id", filter.getBranchId());
        }
        IPage<SettleBranch> settleBranchIPage = this.page(page, queryWrapper);
        List<SettleBranchDto> dtoList = new ArrayList<>();
        ListWrapper<SettleBranchDto> listWrapper = new ListWrapper<>();
        if(settleBranchIPage != null && settleBranchIPage.getRecords() != null && settleBranchIPage.getRecords().size() > 0){
            List<Long> userIdList = new ArrayList<>();
            for (SettleBranch settleBranch: settleBranchIPage.getRecords()){
                userIdList.add(settleBranch.getOperator());
            }
            Map<Long, String> userIdAndNameMap = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList)).getData();
            Map<Long, ServiceBranch> serviceBranchMap = this.serviceBranchService.mapServiceBranchByCorp(filter.getServiceCorp());
            for(SettleBranch settleBranch: settleBranchIPage.getRecords()){
                SettleBranchDto dto = new SettleBranchDto();
                BeanUtils.copyProperties(settleBranch, dto);
                dto.setBranchName(serviceBranchMap.get(settleBranch.getBranchId()) == null ? "" : serviceBranchMap.get(settleBranch.getBranchId()).getBranchName());
                dto.setOperatorName(userIdAndNameMap.get(settleBranch.getOperator()));
                dto.setStatusName(SettleStatusEnum.lookup(settleBranch.getStatus()));
                dtoList.add(dto);
            }
            listWrapper.setList(dtoList);
            listWrapper.setTotal(settleBranchIPage.getTotal());
        }
        return listWrapper;
    }

    @Override
    public void deleteById(Long settleId) {
        if(LongUtil.isZero(settleId)){
            throw new AppException("主键不能为空！");
        }
        SettleBranch settleBranch = this.getById(settleId);
        if(settleBranch == null){
            throw new AppException("结算单不存在！");
        }
        this.settleBranchDetailService.remove(new UpdateWrapper<SettleBranchDetail>().eq("settle_id", settleId));
        this.removeById(settleId);
    }

}
