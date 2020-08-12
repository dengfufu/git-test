package com.zjft.usp.anyfix.settle.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.settle.dto.SettleStaffDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleStaffDetailFilter;
import com.zjft.usp.anyfix.settle.mapper.SettleStaffDetailMapper;
import com.zjft.usp.anyfix.settle.model.SettleStaffDetail;
import com.zjft.usp.anyfix.settle.service.SettleStaffDetailService;
import com.zjft.usp.anyfix.work.request.enums.WarrantyEnum;
import com.zjft.usp.anyfix.work.request.mapper.WorkRequestMapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 员工结算单明细 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Service
@Transactional(rollbackFor = AppException.class)
public class SettleStaffDetailServiceImpl extends ServiceImpl<SettleStaffDetailMapper, SettleStaffDetail> implements SettleStaffDetailService {

    @Autowired
    private WorkTypeService workTypeService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private WorkRequestMapper workRequestMapper;

    @Override
    public ListWrapper<SettleStaffDetailDto> query(SettleStaffDetailFilter settleStaffDetailFilter, ReqParam reqParam) {
        ListWrapper<SettleStaffDetailDto> listWrapper = new ListWrapper<>();
        if(settleStaffDetailFilter == null || LongUtil.isZero(settleStaffDetailFilter.getSettleId())){
            return listWrapper;
        }
        Page page = new Page(settleStaffDetailFilter.getPageNum(), settleStaffDetailFilter.getPageSize());
        List<SettleStaffDetailDto> dtoList = this.workRequestMapper.querySettleStaffDetailDto(page, settleStaffDetailFilter);
        if(dtoList != null && dtoList.size() > 0){
            Map<Integer, String> workTypeMap = workTypeService.mapWorkType();
            List<Long> customCorpIdList = dtoList.stream().map(e -> e.getCustomCorp()).collect(Collectors.toList());
            Set<Long> set = new HashSet<>();
            set.addAll(customCorpIdList);
            List<Long> corpIdList = new ArrayList<>(set);
            Result corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), Map.class);
            for(SettleStaffDetailDto detailDto: dtoList){
                detailDto.setCustomCorpName(corpMap.get(detailDto.getCustomCorp()));
                detailDto.setWorkTypeName(workTypeMap.get(detailDto.getWorkType()));
                detailDto.setWarrantyName(WarrantyEnum.getNameByCode(detailDto.getWarranty()));
            }
        }
        return ListWrapper.<SettleStaffDetailDto>builder().list(dtoList).total(page.getTotal()).build();
    }
}
