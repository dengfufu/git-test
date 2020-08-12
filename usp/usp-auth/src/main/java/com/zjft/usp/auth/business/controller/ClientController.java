package com.zjft.usp.auth.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.auth.business.dto.ClientDto;
import com.zjft.usp.auth.business.dto.ClientFilter;
import com.zjft.usp.auth.business.model.Client;
import com.zjft.usp.auth.business.service.ClientService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 应用
 *
 * @author CK
 * @date 2019-07-30 15:26
 */
@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    ClientService clientService;

    @PostMapping("/list")
    public Result<ListWrapper<Client>> list(@RequestBody ClientFilter clientFilter) {
        ListWrapper<Client> clientListWrapper = clientService.listClient(clientFilter);
        return Result.succeed(clientListWrapper);
    }

    @PostMapping("/add")
    public Result<Client> add(@RequestBody ClientDto clientDto) {
        clientService.saveClient(clientDto);
        return Result.succeed();
    }

    @PostMapping("/update")
    public Result<Client> update(@RequestBody ClientDto clientDto) {
        clientService.updateClient(clientDto);
        return Result.succeed();
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody ClientDto clientDto) {
        clientService.deleteClient(clientDto.getClientId(), clientDto.getCorpId());
        return Result.succeed();
    }

    /**
     * 根据clientId获取客户端详细信息
     *
     * @param clientId
     * @return
     */
    @GetMapping("/feign/findByClientId")
    public Client findByClientId(@RequestParam(name = "clientId") String clientId) {
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id", clientId);
        Client client = clientService.getOne(queryWrapper);
        return client;
    }
}
