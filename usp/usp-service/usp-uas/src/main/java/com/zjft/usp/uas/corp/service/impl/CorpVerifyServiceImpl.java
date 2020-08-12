package com.zjft.usp.uas.corp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.corp.dto.CorpVerifyDto;
import com.zjft.usp.uas.corp.mapper.CorpVerifyMapper;
import com.zjft.usp.uas.corp.model.CorpRegistry;
import com.zjft.usp.uas.corp.model.CorpVerify;
import com.zjft.usp.uas.corp.model.CorpVerifyApp;
import com.zjft.usp.uas.corp.service.CorpRegistryService;
import com.zjft.usp.uas.corp.service.CorpVerifyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业认证实现类
 * @author canlei
 * @date 2019-08-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpVerifyServiceImpl extends ServiceImpl<CorpVerifyMapper, CorpVerify> implements CorpVerifyService {

    @Resource
    private CorpVerifyMapper corpVerifyMapper;
    @Autowired
    private CorpRegistryService corpRegistryService;

    @Override
    public Map<Long, CorpVerify> mapCorpIdAndVerify(List<Long> corpIdList){
        List<CorpVerify> corpVerifyList = (List<CorpVerify>)listByIds(corpIdList);
        Map<Long, CorpVerify> map = new HashMap<>();
        if(corpVerifyList != null && corpVerifyList.size() > 0){
            for(CorpVerify corpVerify:corpVerifyList){
                map.put(corpVerify.getCorpId(), corpVerify);
            }
        }
        return map;
    }

    @Override
    public int createVerifyByCheck(CorpVerifyApp corpVerifyApp){
        CorpVerify corpVerify = new CorpVerify();
        BeanUtils.copyProperties(corpVerifyApp, corpVerify);
        save(corpVerify);
        return 0;
    }

    /**
     * 添加租户，自动认证
     * @date 2020/6/11
     * @param corpId
     * @return int
     */
    @Override
    public int createVerifyByCorpId(Long corpId) {
        CorpVerify corpVerify = this.getById(corpId);
        if(StringUtils.isEmpty(corpVerify)) {
            CorpVerify verify = new CorpVerify();
            verify.setCorpId(corpId);
            save(verify);
        }
        return 0;
    }

    /**
     * 获取企业认证详细信息
     * @date 2020/6/14
     * @param corpId
     * @param userInfo
     * @return com.zjft.usp.uas.corp.dto.CorpVerifyDto
     */
    @Override
    public CorpVerifyDto queryCorpVerify(Long corpId, UserInfo userInfo) {
        CorpVerify corpVerify = corpVerifyMapper.selectById(corpId);
        CorpVerifyDto dto = new CorpVerifyDto();
        if (!StringUtils.isEmpty(corpVerify)) {
            BeanUtils.copyProperties(corpVerify, dto);
        }
        return dto;
    }

    /**
     * 更改企业认证信息
     * @date 2020/6/14
     * @param corpVerifyDto
     * @param user
     * @return void
     */
    @Override
    public void updateCorpVerify(CorpVerifyDto corpVerifyDto, UserInfo user) {
        if (corpVerifyDto == null || corpVerifyDto.getCorpId() == null) {
            throw new AppException("参数解析错误！");
        }
        // 若是认证，则自动添加认证信息
        if ("verify".equals(corpVerifyDto.getKey())) {
            //自动认证
            CorpRegistry corpRegistry = corpRegistryService.getCorpInfoById(corpVerifyDto.getCorpId());
            corpRegistry.setVerify(1);
            corpRegistryService.saveOrUpdate(corpRegistry);
            //添加自动认证信息
            CorpVerify verify = this.getById(corpVerifyDto.getCorpId());
            if (verify == null) {
                verify = new CorpVerify();
                verify.setCorpId(corpVerifyDto.getCorpId());
                BeanUtils.copyProperties(corpVerifyDto, verify, "corpId");
                this.save(verify);
            }

        } else if ("unverify".equals(corpVerifyDto.getKey())) {
            //撤消认证
            CorpRegistry corpRegistry = corpRegistryService.getCorpInfoById(corpVerifyDto.getCorpId());
            corpRegistry.setVerify(0);
            corpRegistryService.saveOrUpdate(corpRegistry);

        }else {
            CorpVerify corpVerify = this.getById(corpVerifyDto.getCorpId());
            BeanUtils.copyProperties(corpVerifyDto, corpVerify,"corpId");
            this.updateById(corpVerify);
        }


    }
}
