package com.zjft.usp.anyfix.settle.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.settle.dto.SettleCustomDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleCustomDetailFilter;
import com.zjft.usp.anyfix.settle.mapper.SettleCustomDetailMapper;
import com.zjft.usp.anyfix.settle.model.SettleCustomDetail;
import com.zjft.usp.anyfix.settle.service.SettleCustomDetailService;
import com.zjft.usp.anyfix.work.request.enums.WarrantyEnum;
import com.zjft.usp.anyfix.work.request.mapper.WorkRequestMapper;
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
 * 客户结算单明细 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Service
@Transactional(rollbackFor = AppException.class)
public class SettleCustomDetailServiceImpl extends ServiceImpl<SettleCustomDetailMapper, SettleCustomDetail> implements SettleCustomDetailService {

    @Autowired
    private WorkTypeService workTypeService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private WorkRequestMapper workRequestMapper;

    @Override
    public ListWrapper<SettleCustomDetailDto> pageByFilter(SettleCustomDetailFilter settleCustomDetailFilter){
        if(settleCustomDetailFilter == null || LongUtil.isZero(settleCustomDetailFilter.getSettleId())){
            throw new AppException("客户结算单编号不能为空！");
        }
        Page page = new Page(settleCustomDetailFilter.getPageNum(), settleCustomDetailFilter.getPageSize());
        List<SettleCustomDetailDto> dtoList = this.workRequestMapper.querySettleCustomDetailDto(page, settleCustomDetailFilter);
        if(dtoList != null && dtoList.size() > 0){
            Map<Integer, String> workTypeMap = workTypeService.mapWorkType();
            List<Long> customCorpIdList = dtoList.stream().map(e -> e.getCustomCorp()).collect(Collectors.toList());
            Set<Long> set = new HashSet<>();
            set.addAll(customCorpIdList);
            List<Long> corpIdList = new ArrayList<>(set);
            Result corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), Map.class);
            for(SettleCustomDetailDto detailDto: dtoList){
                detailDto.setCustomCorpName(corpMap.get(detailDto.getCustomCorp()));
                detailDto.setWorkTypeName(workTypeMap.get(detailDto.getWorkType()));
                detailDto.setWarrantyName(WarrantyEnum.getNameByCode(detailDto.getWarranty()));
            }
        }
        return ListWrapper.<SettleCustomDetailDto>builder().list(dtoList).total(page.getTotal()).build();
    }

}
