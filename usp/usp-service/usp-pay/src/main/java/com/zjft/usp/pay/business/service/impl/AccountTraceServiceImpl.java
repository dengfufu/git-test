package com.zjft.usp.pay.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.pay.business.dto.AccountTraceDto;
import com.zjft.usp.pay.business.filter.AccountTraceFilter;
import com.zjft.usp.pay.business.enums.AccountTraceEnum;
import com.zjft.usp.pay.business.enums.CommonEnum;
import com.zjft.usp.pay.business.model.AccountTrace;
import com.zjft.usp.pay.business.mapper.AccountTraceMapper;
import com.zjft.usp.pay.business.service.AccountTraceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 账户流水表  服务实现类
 * </p>
 *
 * @author CK
 * @since 2020-05-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountTraceServiceImpl extends ServiceImpl<AccountTraceMapper, AccountTrace> implements AccountTraceService {

    @Override
    public void addAccountTrace(AccountTraceDto accountTraceDTO) {
        AccountTrace accountTrace = new AccountTrace();
        accountTrace.setId(KeyUtil.getId());
        BeanUtils.copyProperties(accountTraceDTO, accountTrace);
        this.save(accountTrace);
    }

    @Override
    public ListWrapper<AccountTrace> query(AccountTraceFilter accountTraceFilter) {
        if (StringUtils.isEmpty(accountTraceFilter.getAccountId())) {
            throw new AppException("accountId不能为空");
        }
        ListWrapper<AccountTrace> listWrapper = new ListWrapper<>();
        QueryWrapper<AccountTrace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_id", accountTraceFilter.getAccountId());
        if (!StringUtils.isEmpty(accountTraceFilter.getDirection())) {
            queryWrapper.eq("direction", accountTraceFilter.getDirection());
        }
        if (!StringUtils.isEmpty(accountTraceFilter.getTraceType())) {
            queryWrapper.eq("trace_type", accountTraceFilter.getTraceType());
        }
        if (!StringUtils.isEmpty(accountTraceFilter.getStarTime())) {
            queryWrapper.ge("time", accountTraceFilter.getStarTime());
        }
        if (!StringUtils.isEmpty(accountTraceFilter.getEndTime())) {
            queryWrapper.le("time", accountTraceFilter.getEndTime());
        }
        queryWrapper.orderByDesc("time");
        Page page = new Page(accountTraceFilter.getPageNum(), accountTraceFilter.getPageSize());
        IPage<AccountTrace> accountTraceIPage = this.page(page, queryWrapper);
        List<AccountTrace> accountTraceList = accountTraceIPage.getRecords();
        if (CollectionUtil.isNotEmpty(accountTraceList)) {
            accountTraceList.forEach((AccountTrace accountTrace) -> {
                accountTrace.setTraceTypeName(CommonEnum.TraceType.getNameByCode(accountTrace.getTraceType()));
                accountTrace.setApplySourceName(AccountTraceEnum.ApplySource.getNameByCode(accountTrace.getApplySource()));
                accountTrace.setDirectionName(AccountTraceEnum.Direction.getNameByCode(accountTrace.getDirection()));
            });
            listWrapper.setList(accountTraceList);
        }
        listWrapper.setTotal(accountTraceIPage.getTotal());
        return listWrapper;
    }
}
