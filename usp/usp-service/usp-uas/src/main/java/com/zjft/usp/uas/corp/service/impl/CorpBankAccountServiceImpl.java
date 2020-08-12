package com.zjft.usp.uas.corp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.corp.dto.CorpBankAccountDto;
import com.zjft.usp.uas.corp.mapper.CorpBankAccountMapper;
import com.zjft.usp.uas.corp.model.CorpBankAccount;
import com.zjft.usp.uas.corp.service.CorpBankAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 企业银行账户表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-07-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpBankAccountServiceImpl extends ServiceImpl<CorpBankAccountMapper, CorpBankAccount> implements CorpBankAccountService {

    /**
     * 获得企业默认银行账户
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/7/3 16:00
     **/
    @Override
    public CorpBankAccount findDefaultBankAccount(Long corpId) {
        List<CorpBankAccount> corpBankAccountList = this.list(
                new QueryWrapper<CorpBankAccount>().eq("corp_id", corpId)
                        .eq("is_default", "Y"));
        if (CollectionUtil.isNotEmpty(corpBankAccountList)) {
            return corpBankAccountList.get(0);
        }
        return null;
    }

    /**
     * 根据企业编号获取默认银行账户Dto
     *
     * @param corpId
     * @return
     */
    @Override
    public CorpBankAccountDto finDefaultDtoByCorpId(Long corpId) {
        CorpBankAccountDto corpBankAccountDto = new CorpBankAccountDto();
        CorpBankAccount corpBankAccount = this.findDefaultBankAccount(corpId);
        if (corpBankAccount == null) {
            return null;
        }
        BeanUtils.copyProperties(corpBankAccount, corpBankAccountDto);
        return corpBankAccountDto;
    }

    /**
     * 根据企业编号列表获取与银行账号的映射
     *
     * @param corpIdList
     * @return
     */
    @Override
    public Map<Long, CorpBankAccountDto> mapDefaultByCorpIdList(List<Long> corpIdList) {
        Map<Long, CorpBankAccountDto> map = new HashMap<>();
        if (CollectionUtil.isEmpty(corpIdList)) {
            return map;
        }
        List<CorpBankAccount> corpBankAccountList = this.list(
                new QueryWrapper<CorpBankAccount>().in("corp_id", corpIdList)
                        .eq("is_default", "Y"));
        if (CollectionUtil.isNotEmpty(corpBankAccountList)) {
            corpBankAccountList.forEach(corpBankAccount -> {
                CorpBankAccountDto corpBankAccountDto = new CorpBankAccountDto();
                BeanUtils.copyProperties(corpBankAccount, corpBankAccountDto);
                map.put(corpBankAccount.getCorpId(), corpBankAccountDto);
            });
        }
        return map;
    }

}
