package com.zjft.usp.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.auth.business.dto.ClientFilter;
import com.zjft.usp.auth.business.model.Client;
import com.zjft.usp.auth.business.service.ClientService;
import com.zjft.usp.common.model.ListWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author CK
 * @date 2019-09-23 11:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceTest {

    @Autowired
    ClientService clientService;

    @Test
    public void query() {
        ClientFilter clientFilter = new ClientFilter();
        clientFilter.setPageNum(0);
        clientFilter.setPageSize(2);
        clientFilter.setCorpId(1229327791726825475L);
        ListWrapper<Client> clientIPage = clientService.listClient(clientFilter);
        System.out.println(JSON.toJSONString(clientIPage));
    }

    @Test
    public void add() {
        Client client = new Client();
        client.setClientId("10004");
        client.setClientSecret("usp");
        clientService.save(client);
    }

    @Test
    public void view() {
        System.out.println(JSON.toJSONString(clientService.getById("1")));
    }
}
