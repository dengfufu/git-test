package com.zjft.usp.uas.corp.service;

import com.zjft.usp.uas.corp.dto.CorpBankAccountDto;
import com.zjft.usp.uas.corp.model.CorpBankAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 企业银行账户表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-07-03
 */
public interface CorpBankAccountService extends IService<CorpBankAccount> {

    /**
     * 获得企业默认银行账户
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/7/3 16:00
     **/
    CorpBankAccount findDefaultBankAccount(Long corpId);

    /**
     * 获取Dto
     *
     * @param corpId
     * @return
     */
    CorpBankAccountDto finDefaultDtoByCorpId(Long corpId);

    /**
     * 根据企业编号列表获取与银行账号的映射
     *
     * @param corpIdList
     * @return
     */
    Map<Long, CorpBankAccountDto> mapDefaultByCorpIdList(List<Long> corpIdList);

}
