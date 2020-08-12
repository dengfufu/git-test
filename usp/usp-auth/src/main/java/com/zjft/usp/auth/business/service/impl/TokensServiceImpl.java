package com.zjft.usp.auth.business.service.impl;

import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.auth.business.model.Token;
import com.zjft.usp.auth.business.service.TokensService;
import com.zjft.usp.redis.template.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CK
 * @date 2019-09-19 09:07
 */
@Slf4j
@Service
public class TokensServiceImpl implements TokensService {

    private static final String AUTH = "auth:";
    private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    private static final String UNAME_TO_ACCESS = "uname_to_access:";

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public IPage<Token> listTokens(Page page, String logonId, String clientId) {
        Integer current = Math.toIntExact(page.getCurrent());
        Integer size = Math.toIntExact(page.getSize());
        int[] startEnds = PageUtil.transToStartEnd(current, size);
        log.debug(JSON.toJSONString(startEnds));

        //根据请求参数生成redis的key
        String redisKey = getRedisKey(logonId, clientId);

        // 获取总数
        long total = redisRepository.length(redisKey);
        log.debug(JSON.toJSONString(total));

        // 分页查询
        List<Token> tokenList = new ArrayList(size);
        List<Object> tokenObjs = redisRepository.getList(redisKey, startEnds[0], startEnds[1] - 1);
        if (tokenObjs != null) {
            for (Object obj : tokenObjs) {
                DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken) obj;
                //构造token对象
                Token tokenVo = new Token();
                tokenVo.setTokenValue(accessToken.getValue());
                tokenVo.setExpiration(accessToken.getExpiration());

                //获取用户信息
                Object authObj = redisRepository.get(AUTH + accessToken.getValue());

                OAuth2Authentication authentication = (OAuth2Authentication) authObj;
                if (authentication != null) {
                    OAuth2Request request = authentication.getOAuth2Request();
                    tokenVo.setUsername(authentication.getName());
                    tokenVo.setClientId(request.getClientId());
                    tokenVo.setGrantType(request.getGrantType());
                }
                tokenList.add(tokenVo);
            }
        }
        return new Page<Token>()
                .setRecords(tokenList)
                .setCurrent(current)
                .setSize(size)
                .setTotal(total);
    }

    /**
     * 根据请求参数生成redis的key
     */
    private String getRedisKey(String username, String clientId) {
        String result;
        if (StrUtil.isNotEmpty(username)) {
            result = UNAME_TO_ACCESS + clientId + ":" + username;
        } else {
            result = CLIENT_ID_TO_ACCESS + clientId;
        }
        log.info("redisKey:" + result);
        return result;
    }
}
