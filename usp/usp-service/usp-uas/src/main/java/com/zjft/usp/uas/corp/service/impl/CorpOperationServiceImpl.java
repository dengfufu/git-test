package com.zjft.usp.uas.corp.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.mapper.CorpOperationMapper;
import com.zjft.usp.uas.corp.model.CorpOperation;
import com.zjft.usp.uas.corp.service.CorpOperationService;
import com.zjft.usp.uas.corp.enums.CorpOperationEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 企业操作记录实现类
 * @author canlei
 * @date 2019-08-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpOperationServiceImpl extends ServiceImpl<CorpOperationMapper, CorpOperation> implements CorpOperationService {

    @Override
    public int addCorpOperation(CorpOperationEnum corpOperationEnum, long corpId, ReqParam reqParam, long curUserId, String clientId){
        CorpOperation corpOperation = new CorpOperation();
        corpOperation.setClientId(clientId);
        corpOperation.setCorpId(corpId);
        corpOperation.setTxId(reqParam.getTxId());
        corpOperation.setUserId(curUserId);
        corpOperation.setOperTime(DateTime.now());
        corpOperation.setOperType(corpOperationEnum.getType());
        corpOperation.setDetails(corpOperationEnum.getName());
        //经纬度获取
        corpOperation.setLat(reqParam.getLat());
        corpOperation.setLon(reqParam.getLon());
        save(corpOperation);
        return 0;
    }

}
