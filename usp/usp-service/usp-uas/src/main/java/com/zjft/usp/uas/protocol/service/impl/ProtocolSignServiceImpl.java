package com.zjft.usp.uas.protocol.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.uas.protocol.dto.ProtocolCheckDto;
import com.zjft.usp.uas.protocol.filter.ProtocolFilter;
import com.zjft.usp.uas.protocol.model.ProtocolDef;
import com.zjft.usp.uas.protocol.model.ProtocolSign;
import com.zjft.usp.uas.protocol.mapper.ProtocolSignMapper;
import com.zjft.usp.uas.protocol.service.ProtocolDefService;
import com.zjft.usp.uas.protocol.service.ProtocolSignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.right.service.SysTenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户签订协议  服务实现类
 * </p>
 *
 * @author CK
 * @since 2020-06-18
 */
@Transactional
@Service
@Slf4j
public class ProtocolSignServiceImpl extends ServiceImpl<ProtocolSignMapper, ProtocolSign> implements ProtocolSignService {

    @Autowired
    ProtocolDefService protocolDefService;

    @Autowired
    SysTenantService sysTenantService;

    public void sign(Long userId, Long corpId, Long operator, List<Integer> protocolIds) {
        QueryWrapper<ProtocolSign> queryWrapper = new QueryWrapper();
        if(userId!=null) {
            queryWrapper.eq("user_id", userId);
        } else if(corpId!=null) {
            queryWrapper.eq("corp_id", corpId);
        }

        List<ProtocolSign> protocolSignSignedList = this.list(queryWrapper);
        Map<Integer, ProtocolSign> protocolIdAndProtocolUserMap = protocolSignSignedList.stream().collect(Collectors.toMap(ProtocolSign::getProtocolId, Function.identity()));
        Set<Integer> signedProtocolIds = protocolIdAndProtocolUserMap.keySet();

        List<ProtocolDef> protocolDefIdList = protocolDefService.list();
        List<Integer> protocolIdList = protocolDefIdList.stream().map(ProtocolDef::getId).collect(Collectors.toList());

        Date currentDate = DateUtil.date();
        protocolIds.forEach((Integer protocolId) -> {
            if (!protocolIdList.contains(protocolId)) {
                throw new AppException("id:" + protocolId + ",不存在该协议");
            }
            if (signedProtocolIds.contains(protocolId)) {
                // 1. 已经签约过的则更新签约时间
                this.update(new UpdateWrapper<ProtocolSign>()
                        .set("sign_date", currentDate)
                        .eq("id", protocolIdAndProtocolUserMap.get(protocolId).getId()));
            } else {
                // 2. 没有则添加签约记录
                ProtocolSign protocolSign = new ProtocolSign();
                if(userId!=null) {
                    protocolSign.setUserId(userId);
                } else if(corpId!=null) {
                    protocolSign.setCorpId(corpId);
                    protocolSign.setOperator(operator);
                }
                protocolSign.setProtocolId(protocolId);
                protocolSign.setSignDate(currentDate);
                this.save(protocolSign);
            }
        });
    }

    /**
     * 签订协议
     *
     * @param corpId
     * @param operator
     * @param protocolIds
     */
    @Override public void signToB(Long corpId, Long operator, List<Integer> protocolIds) {
        this.sign(null, corpId, operator, protocolIds);
    }

    /**
     * 签订协议
     *
     * @param userId
     * @param protocolIds
     */
    @Override public void signToC(Long userId, List<Integer> protocolIds) {
        this.sign(userId, null, null, protocolIds);
    }

    @Override
    public List<ProtocolCheckDto> checkSign(Long userId, Long corpId, String module) {
        return this.baseMapper.checkSign(new ProtocolFilter()
            .setCorpId(corpId)
            .setUserId(userId)
            .setModule(module));
    }
}
