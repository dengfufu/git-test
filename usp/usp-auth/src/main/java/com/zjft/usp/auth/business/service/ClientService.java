package com.zjft.usp.auth.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.auth.business.dto.ClientDto;
import com.zjft.usp.auth.business.dto.ClientFilter;
import com.zjft.usp.auth.business.model.Client;
import com.zjft.usp.common.model.ListWrapper;

/**
 * @author CK
 * @date 2019-09-23 10:25
 */
public interface ClientService extends IService<Client> {

    ListWrapper<Client> listClient(ClientFilter clientFilter);

    void saveClient(ClientDto clientDto);

    void deleteClient(String clientId, Long corpId);

    void updateClient(ClientDto clientDto);
}
