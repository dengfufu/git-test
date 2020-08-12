package com.zjft.usp.anyfix.settle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.settle.dto.SettleBranchDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleBranchDetailFilter;
import com.zjft.usp.anyfix.settle.mapper.SettleBranchDetailMapper;
import com.zjft.usp.anyfix.settle.model.SettleBranchDetail;
import com.zjft.usp.anyfix.settle.service.SettleBranchDetailService;
import com.zjft.usp.anyfix.work.request.enums.WarrantyEnum;
import com.zjft.usp.anyfix.work.request.mapper.WorkRequestMapper;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 网点结算单明细 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Service
@Transactional(rollbackFor = AppException.class)
public class SettleBranchDetailServiceImpl extends ServiceImpl<SettleBranchDetailMapper, SettleBranchDetail> implements SettleBranchDetailService {

    @Autowired
    private WorkTypeService workTypeService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private WorkRequestMapper workRequestMapper;

    @Override
    public ListWrapper<SettleBranchDetailDto> pageByFilter(SettleBranchDetailFilter settleBranchDetailFilter){
        if(settleBranchDetailFilter == null || LongUtil.isZero(settleBranchDetailFilter.getSettleId())){
            throw new AppException("结算单号不能为空！");
        }
        Page page = new Page(settleBranchDetailFilter.getPageNum(), settleBranchDetailFilter.getPageSize());
        QueryWrapper<WorkRequest> queryWrapper = new QueryWrapper<>();
        List<SettleBranchDetailDto> dtoList = this.workRequestMapper.querySettleBranchDetailDto(page, settleBranchDetailFilter);
        if(dtoList != null && dtoList.size() > 0){
            Map<Integer, String> workTypeMap = workTypeService.mapWorkType();
            List<Long> customCorpIdList = dtoList.stream().map(e -> e.getCustomCorp()).collect(Collectors.toList());
            Set<Long> set = new HashSet<>();
            set.addAll(customCorpIdList);
            List<Long> corpIdList = new ArrayList<>(set);
            Result corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), Map.class);
            for(SettleBranchDetailDto detailDto: dtoList){
                detailDto.setCustomCorpName(corpMap.get(detailDto.getCustomCorp()));
                detailDto.setWorkTypeName(workTypeMap.get(detailDto.getWorkType()));
                detailDto.setWarrantyName(WarrantyEnum.getNameByCode(detailDto.getWarranty()));
            }
        }
        return ListWrapper.<SettleBranchDetailDto>builder().list(dtoList).total(page.getTotal()).build();
    }

}
