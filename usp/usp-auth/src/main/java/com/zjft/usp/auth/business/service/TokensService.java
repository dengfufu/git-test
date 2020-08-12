package com.zjft.usp.auth.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.auth.business.model.Token;

/**
 * @author CK
 * @date 2019-09-19 09:07
 */
public interface TokensService {

    IPage<Token> listTokens(Page page, String logonId, String clientId);
}
