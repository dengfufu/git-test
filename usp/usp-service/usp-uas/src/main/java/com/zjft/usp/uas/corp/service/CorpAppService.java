package com.zjft.usp.uas.corp.service;

import com.zjft.usp.uas.corp.dto.CorpAppDto;

/**
 * 企业应用
 */
public interface CorpAppService {

    /**
     * 获取企业应用入口
     * @param corpId 公司 ID
     * @param userId 用户 ID
     * @return
     */
    CorpAppDto getExternalApp(long corpId, long userId);
}
