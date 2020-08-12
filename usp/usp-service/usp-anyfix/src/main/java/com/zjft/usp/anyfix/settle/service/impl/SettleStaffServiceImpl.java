package com.zjft.usp.anyfix.settle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.settle.dto.SettleStaffDto;
import com.zjft.usp.anyfix.settle.filter.SettleStaffFilter;
import com.zjft.usp.anyfix.settle.mapper.SettleStaffMapper;
import com.zjft.usp.anyfix.settle.model.SettleStaff;
import com.zjft.usp.anyfix.settle.model.SettleStaffDetail;
import com.zjft.usp.anyfix.settle.service.SettleStaffDetailService;
import com.zjft.usp.anyfix.settle.service.SettleStaffService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
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
 * 员工结算单 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Service
@Transactional(rollbackFor = AppException.class)
public class SettleStaffServiceImpl extends ServiceImpl<SettleStaffMapper, SettleStaff> implements SettleStaffService {

    @Autowired
    private SettleStaffDetailService settleStaffDetailService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private SettleStaffMapper settleStaffMapper;

    @Override
    public ListWrapper<SettleStaffDto> pageByFilter(SettleStaffFilter settleStaffFilter, ReqParam reqParam) {
        ListWrapper<SettleStaffDto> listWrapper = new ListWrapper<>();
        if(settleStaffFilter == null){
            return listWrapper;
        }
        if(LongUtil.isZero(settleStaffFilter.getServiceCorp())){
            settleStaffFilter.setServiceCorp(reqParam.getCorpId());
        }
        Page page = new Page(settleStaffFilter.getPageNum(), settleStaffFilter.getPageSize());
        List<SettleStaffDto> dtoList = this.settleStaffMapper.querySettleStaffDto(page, settleStaffFilter);
        if(dtoList != null && dtoList.size() > 0){
            List<Long> userIdList = new ArrayList<>();
            for(SettleStaffDto settleStaffDto: dtoList){
                userIdList.add(settleStaffDto.getUserId());
                userIdList.add(settleStaffDto.getOperator());
            }
            Result result = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
            Map<Long, String> userIdAndNameMap = JsonUtil.parseObject(JsonUtil.toJson(result.getData()), Map.class);
            userIdAndNameMap = userIdAndNameMap == null ? new HashMap<>() : userIdAndNameMap;
            for(SettleStaffDto dto: dtoList){
                dto.setUserName(userIdAndNameMap.get(dto.getUserId()));
                dto.setOperatorName(userIdAndNameMap.get(dto.getOperator()));
            }
            listWrapper.setList(dtoList);
            listWrapper.setTotal(page.getTotal());
        }
        return listWrapper;
    }

    @Override
    public void deleteById(Long settleId) {
        if(LongUtil.isZero(settleId)){
            throw new AppException("主键不能为空！");
        }
        SettleStaff settleStaff = this.getById(settleId);
        Assert.notNull(settleStaff, "结算单不存在！");
        this.settleStaffDetailService.remove(new UpdateWrapper<SettleStaffDetail>().eq("settle_id", settleId));
        this.removeById(settleId);
    }

}
