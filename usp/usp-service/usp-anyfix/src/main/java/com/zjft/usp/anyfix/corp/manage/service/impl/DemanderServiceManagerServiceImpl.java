package com.zjft.usp.anyfix.corp.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.corp.manage.mapper.DemanderServiceManagerMapper;
import com.zjft.usp.anyfix.corp.manage.model.DemanderServiceManager;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 委托商与服务商客户经理关系表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-06-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DemanderServiceManagerServiceImpl extends ServiceImpl<DemanderServiceManagerMapper, DemanderServiceManager> implements DemanderServiceManagerService {

    /**
     * 获得客户经理的委托商列表
     *
     * @param serviceCorp
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/6/17 10:30
     **/
    @Override
    public List<Long> listDemanderCorpByManager(Long serviceCorp, Long userId) {
        List<DemanderServiceManager> demanderServiceManagerList = this.list(
                new QueryWrapper<DemanderServiceManager>().eq("manager", userId)
                        .eq("service_corp", serviceCorp));
        List<Long> demanderCorpList = demanderServiceManagerList.stream().map(e -> e.getDemanderCorp()).collect(Collectors.toList());
        return demanderCorpList;
    }

}
