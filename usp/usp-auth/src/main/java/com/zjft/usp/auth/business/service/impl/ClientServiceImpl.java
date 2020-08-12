package com.zjft.usp.auth.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.auth.business.dto.ClientDto;
import com.zjft.usp.auth.business.dto.ClientFilter;
import com.zjft.usp.auth.business.mapper.ClientMapper;
import com.zjft.usp.auth.business.model.Client;
import com.zjft.usp.auth.business.service.ClientService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.redis.template.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CK
 * @date 2019-09-23 10:26
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void saveClient(ClientDto clientDto) {
        List<Client> clientList = this.list(new QueryWrapper<Client>()
                .eq("client_id", clientDto.getClientId()));
        if (clientList.isEmpty() || clientList == null) {
            Client client = new Client();
            client.setCorpId(clientDto.getCorpId());
            client.setClientId(clientDto.getClientId());
            client.setClientSecret(passwordEncoder.encode(clientDto.getClientSecret()));
            client.setWebServerRedirectUri(clientDto.getWebServerRedirectUri());
            save(client);
        } else {
            throw new AppException("clientId已经存在，请重新命名");
        }
    }

    @Override
    public ListWrapper<Client> listClient(ClientFilter clientFilter) {
        QueryWrapper<Client> wrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(clientFilter.getClientId())) {
            wrapper.like("client_id", clientFilter.getClientId());
        }
        if (!StringUtils.isEmpty(clientFilter.getCorpId())) {
            wrapper.eq("corp_id", clientFilter.getCorpId());
        }
        Page page = new Page(clientFilter.getPageNum(), clientFilter.getPageSize());
        IPage<Client> clientPageList = this.baseMapper.selectPage(page, wrapper);
        return ListWrapper.<Client>builder().list(clientPageList.getRecords()).total(clientPageList.getTotal()).build();

    }

    @Override
    public void deleteClient(String clientId, Long corpId) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("client_id", clientId);
        columnMap.put("corp_id", corpId);
        this.baseMapper.deleteByMap(columnMap);
        redisRepository.del("oauth_client_details:" + clientId);
    }

    @Override
    public void updateClient(ClientDto clientDto) {
        Client clientOld = this.getOne(new QueryWrapper<Client>()
                .eq("client_id", clientDto.getClientId())
                .eq("corp_id", clientDto.getCorpId()));
        if (clientOld == null) {
            throw new AppException("clientId不存在，请重新确认提交信息");
        } else {
            // 为空则为不需要修改
            if (!StringUtils.isEmpty(clientDto.getClientSecret())) {
                clientOld.setClientSecret(passwordEncoder.encode(clientDto.getClientSecret()));
            }
            clientOld.setWebServerRedirectUri(clientDto.getWebServerRedirectUri());
            this.updateById(clientOld);
        }
    }
}
